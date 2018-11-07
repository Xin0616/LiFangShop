package com.enation.app.shop.front.api.member;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.shop.component.payment.plugin.alipay.JavashopAlipayUtil;
import com.enation.app.shop.component.payment.plugin.alipay.sdk33.config.AlipayConfig;
import com.enation.app.shop.component.payment.plugin.alipay.sdk33.util.AlipaySubmit;
import com.enation.app.shop.core.member.model.Recharge;
import com.enation.app.shop.core.member.model.Wealth;
import com.enation.app.shop.core.member.service.IRechargeManager;
import com.enation.app.shop.core.member.service.IWealthManager;
import com.enation.app.shop.core.order.model.Order;
import com.enation.app.shop.core.order.model.PayCfg;
import com.enation.app.shop.core.order.model.PaymentLog;
import com.enation.app.shop.core.order.service.IOrderManager;
import com.enation.app.shop.core.order.service.IPaymentLogManager;
import com.enation.app.shop.core.order.service.IPaymentManager;
import com.enation.app.shop.core.order.service.OrderStatus;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;
/**
 * 充值API
 * @author Sylow
 * @version 2.1,2016-07-20
 */
@Controller
@RequestMapping("/api/shop/recharge")
@Scope("prototype")
public class RechargeApiController {
	@Autowired
	private IMemberManager memberManager;
	@Autowired
	private IRechargeManager rechargeManager;
	@Autowired
	private IWealthManager wealthManager;
	@Autowired
	private IPaymentManager paymentManager;
	@Autowired
	private IOrderManager orderManager;


	//添加充值记录
	@ResponseBody
	@RequestMapping(value="/add",produces = MediaType.APPLICATION_JSON_VALUE)
	public String add(Double apply_num, String paymentId){
		Member member = UserConext.getCurrentMember();
		Recharge r = new Recharge();
		r.setApply_date(DateUtil.getDateline());
		r.setApply_num(apply_num);
		r.setMember_id(member.getMember_id());
		r.setStatus(0);
		Integer rid = this.rechargeManager.save(r);
		r.setId(rid);

		return onpay(paymentId, r);
	}

	//调取支付接口
	public String onpay(String paymentId, Recharge recharge){
		try {
			PayCfg payCfg = this.paymentManager.get(paymentId);
			Map<String,String> params = paymentManager.getConfigParams(paymentId);

			String seller_email =params.get("seller_email");
			String partner =params.get("partner");
			String key =  params.get("key");
			String content_encoding = params.get("content_encoding");
			String out_trade_no = "r-" + recharge.getId();

			AlipayConfig.key=key;
			AlipayConfig.partner=partner;
			AlipayConfig.seller_email=seller_email;

			//服务器异步通知页面路径
			String notify_url = this.getCallBackUrl(payCfg);
			//需http://格式的完整路径，不能加?id=123这类自定义参数

			//页面跳转同步通知页面路径
			String return_url = this.getReturnWapUrl(payCfg);
			//需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/

			String show_url= this.getWapShowUrl();

			//商户订单号
			out_trade_no = new String(out_trade_no.getBytes("ISO-8859-1"),"UTF-8")  ;
			//商户网站订单系统中唯一订单号，必填

			String sitename = EopSite.getInstance().getSitename();
			//充值名称
			String subject = sitename + "充值";
			if(!StringUtil.isEmpty(content_encoding)){
				subject = new String(subject.getBytes("ISO-8859-1"),content_encoding);
			}

			String body =  ("充值：" + out_trade_no);
			if(!StringUtil.isEmpty(content_encoding)){
				body=new String( body.getBytes("ISO-8859-1"),content_encoding);
			}

			//付款金额
			String price = new String(recharge.getApply_num().toString().getBytes("ISO-8859-1"),"UTF-8");

			//把请求参数打包成数组
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("service", "alipay.wap.create.direct.pay.by.user");
			sParaTemp.put("partner", AlipayConfig.partner);
			sParaTemp.put("seller_id", AlipayConfig.partner);
			sParaTemp.put("_input_charset", AlipayConfig.input_charset);
			sParaTemp.put("payment_type", "1");
			sParaTemp.put("notify_url", notify_url);
			sParaTemp.put("return_url", return_url);
			sParaTemp.put("out_trade_no", out_trade_no);
			sParaTemp.put("subject", subject);
			sParaTemp.put("total_fee", price);
			sParaTemp.put("show_url", show_url);
			sParaTemp.put("body", body);
			sParaTemp.put("it_b_pay", "");
			sParaTemp.put("extern_token", "");
			String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认");
			return sHtmlText;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	//回调url
	protected String getCallBackUrl(PayCfg payCfg){
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String serverName =request.getServerName();
		int port = request.getServerPort();
		String portstr = "";
		if(port!=80){
			portstr = ":"+port;
		}
		String contextPath = request.getContextPath();
		return "http://"+serverName+portstr+contextPath+"/api/shop/recharge/callBack.do";
	}
	//回显url
	protected String getReturnWapUrl(PayCfg payCfg){
		HttpServletRequest request  =  ThreadContextHolder.getHttpRequest();
		String serverName =request.getServerName();
		int port =request.getServerPort();
		String portstr = "";
		if(port!=80){
			portstr = ":"+port;
		}
		String contextPath = request.getContextPath();
		return "http://"+serverName+portstr+contextPath+"/App_Shop/page1/recharge_result.html" ;

	}
	//显示url
	protected String getWapShowUrl(){
		HttpServletRequest request  =  ThreadContextHolder.getHttpRequest();
		String serverName =request.getServerName();	
		int port =request.getServerPort();
		String portstr = "";
		if(port!=80){
			portstr = ":"+port;
		}
		String contextPath = request.getContextPath();		
		return  "http://"+serverName+portstr+contextPath+"/App_Shop/page1/purse.html";
	}

	@ResponseBody
	@RequestMapping(value="/callBack")
	//回调函数
	public String callBack(){
		try {
			Map<String,String> paramscfg = paymentManager.getConfigParams("alipayWapPlugin");
			//卖家支付宝帐户
			String partner =paramscfg.get("partner");
			String key =  paramscfg.get("key");
			AlipayConfig.key=key;
			AlipayConfig.partner=partner;
			String param_encoding = paramscfg.get("param_encoding");  

			HttpServletRequest request  =  ThreadContextHolder.getHttpRequest();

			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			//商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

			//支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

			//交易状态
			String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

			//买家支付宝账号
			//String buyer_logon_id = new String(request.getParameter("buyer_logon_id").getBytes("ISO-8859-1"),"UTF-8");

			if(JavashopAlipayUtil.verify(param_encoding)){//验证成功
				if(trade_status.equals("TRADE_FINISHED")||trade_status.equals("TRADE_SUCCESS")){
					//更改充值记录状态位
					String rid = out_trade_no.split("-")[1];
					Recharge recharge = this.rechargeManager.get(Integer.parseInt(rid));
					recharge.setStatus(1);
					this.rechargeManager.save(recharge);

					//添加明细
					Wealth w = new Wealth();
					w.setConsume(recharge.getApply_num());
					w.setConsume_type(1);
					w.setCreate_time(DateUtil.getDateline());
					w.setMember_id(recharge.getMember_id());
					this.wealthManager.save(w);

					//修改会员账户
					Member m = this.memberManager.get(recharge.getMember_id());
					m.setBalance(m.getBalance() + recharge.getApply_num());
					this.memberManager.edit(m);

				}else {
				}
			}else{//验证失败
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//钱包支付
	@ResponseBody
	@RequestMapping(value="/pay",produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult pay(Integer order_id, String password){
		try{
			Member member = UserConext.getCurrentMember();
			if(StringUtil.isEmpty(password)){
				return JsonResultUtil.getErrorJson("密码不能为空");
			}

			String pwdmd5 = com.enation.framework.util.StringUtil.md5(password);
			if(pwdmd5.equals(member.getPassword())){
				Order order = this.orderManager.get(order_id);
				if(member.getBalance() >= order.getNeed_pay_money()){
					this.rechargeManager.payOrder(order_id);
					return JsonResultUtil.getSuccessJson("支付成功");
				}else{
					return JsonResultUtil.getErrorJson("账户余额不足");
				}
			}else{
				return JsonResultUtil.getErrorJson("密码错误");
			}
		}catch(Exception e){
			return JsonResultUtil.getErrorJson("支付失败");
		}

	}
}
