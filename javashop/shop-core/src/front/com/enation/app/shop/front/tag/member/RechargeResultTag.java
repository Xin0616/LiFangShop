package com.enation.app.shop.front.tag.member;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.shop.component.payment.plugin.alipay.JavashopAlipayUtil;
import com.enation.app.shop.component.payment.plugin.alipay.sdk33.config.AlipayConfig;
import com.enation.app.shop.core.member.model.Recharge;
import com.enation.app.shop.core.member.model.Wealth;
import com.enation.app.shop.core.member.service.IRechargeManager;
import com.enation.app.shop.core.member.service.IWealthManager;
import com.enation.app.shop.core.order.model.PaymentResult;
import com.enation.app.shop.core.order.plugin.payment.IPaymentEvent;
import com.enation.app.shop.core.order.service.IPaymentManager;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.RequestUtil;

import freemarker.template.TemplateModelException;

/**
 * 支付结果标签
 * @author 沈忱
 */
@Component
@Scope("prototype")
public class RechargeResultTag extends BaseFreeMarkerTag {

	@Autowired
	private IMemberManager memberManager;
	@Autowired
	private IRechargeManager rechargeManager;
	@Autowired
	private IWealthManager wealthManager;
	@Autowired
	private IPaymentManager paymentManager;
	
	/**
	 * 支付结果标签
	 * 些标签必须写在路径为：/recharge_result.html的模板中。用于第三方支付后，显示支付结果。
	 * @param 无
	 * @return 支付结果，PaymentResult型
	 * {@link PaymentResult}
	 */
	@Override
	protected Object exec(Map p) throws TemplateModelException {
		PaymentResult paymentResult = new PaymentResult();
		
		try{
			HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
			Map<String,String> paramscfg = paymentManager.getConfigParams("alipayWapPlugin");
			//卖家支付宝帐户
			String partner =paramscfg.get("partner");
			String key =  paramscfg.get("key");
			AlipayConfig.key=key;
			AlipayConfig.partner=partner;
			String param_encoding = paramscfg.get("param_encoding");  

			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			//商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

			//支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

			//交易状态
			String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

			//买家支付宝账号
			//String buyer_logon_id = new String(request.getParameter("buyer_logon_id").getBytes("ISO-8859-1"),"UTF-8");

//			if(JavashopAlipayUtil.verify(param_encoding)){//验证成功
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
					paymentResult.setResult(1);
//				}else{
//					paymentResult.setResult(0);
//					paymentResult.setError("支付失败" + trade_status);
//				}
			}else{//验证失败
				paymentResult.setResult(0);
				paymentResult.setError("验证失败");
			}
		}catch(Exception e){
			this.logger.error("支付失败",e);
			paymentResult.setResult(0);
			paymentResult.setError(e.getMessage());
		}		
		
		return paymentResult;
	}
}
