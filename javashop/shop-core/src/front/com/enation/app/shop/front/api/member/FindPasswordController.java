package com.enation.app.shop.front.api.member;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.model.SmsMessage;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.base.core.service.ISmsManager;
import com.enation.app.base.core.util.SmsTypeKeyEnum;
import com.enation.app.base.core.util.SmsUtil;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.jms.EmailProducer;
import com.enation.framework.util.CurrencyUtil;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;

/**
 * 找回密码api
 * @author liuzy
 * @version 2.0,2016-02-18 wangxin v60版本改造
 */
@Controller
@RequestMapping("/api/shop/findPassword")
@Scope("prototype")
public class FindPasswordController {
	
	@Autowired
	private ISmsManager smsManager;
	@Autowired
	private IMemberManager memberManager;
	
	
	

	
	@ResponseBody
	@RequestMapping(value="/send",produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult send(String mobileNum){
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String flag = (String) request.getSession().getAttribute("flag");
		Long time_stamp = (Long) request.getSession().getAttribute("time_stamp");	//首次点击或者重复发送的时间
		long current_time = DateUtil.getDateline();		//当前时间
		//判断是否是首次发送，或者重新发送 为空或者为0都为首次
		if(flag=="1"){
			//防止重复调用发送短信
			if(CurrencyUtil.sub(current_time, time_stamp)>=60){
				request.getSession().setAttribute("time_stamp", DateUtil.getDateline());
				return sendSmsCode(mobileNum);
			}else{
				return sendSmsCode(mobileNum);
			}
		}else{
			request.getSession().setAttribute("time_stamp", DateUtil.getDateline());
			request.getSession().setAttribute("flag","1");
				return sendSmsCode(mobileNum);
		}
		
	}
	
	/**
	 * 检查邮箱(用户名)并发送短信验证码
	 * 需要传递mobileNum一个参数
	 * 
	 * @param mobileNum 手机号(用户名),String型
	 * 
	 * @return 返回json串
	 * result  为1表示调用成功0表示失败 ，int型
	 * message 为提示信息
	 */
	@ResponseBody
	@RequestMapping(value="/send-sms-code",produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult sendSmsCode(String mobileNum) {
		Member member = null;
		member = memberManager.getMemberByMobile(mobileNum);
		
		if(member==null) {
			return JsonResultUtil.getErrorJson("没有找到用户");	
		} else {
			String code=""+(int)((Math.random()*9+1)*100000);
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			request.getSession().setAttribute("smscode", code);
			request.getSession().setAttribute("smsnum", member.getMember_id());
			
			SmsMessage message = new SmsMessage();
			String content = "您的验证码是：" + code;
			message.setUser_id(member.getMember_id());
			message.setTel(mobileNum);
			message.setMessage_info(content);
			
			Map param = new HashMap();
			param.put("code", code);
			
			if(smsManager.send(mobileNum, content,param)){
				return JsonResultUtil.getSuccessJson("短信发送成功");
			}else{
				return JsonResultUtil.getErrorJson("短信发送失败!\n你可能还未安装任何短信网关组件或配置错误！");
			}
		}
	}
	
	
	
	
	/**
	 * 检查用户输入的验证码
	 * 需要传入mobileNum一个参数
	 * 
	 * @param mobileNum 验证码,String型
	 *  
	 * @return 返回json串
	 * result  为1表示调用成功0表示失败 ，int型
	 * message 为提示信息
	 */
	@ResponseBody
	@RequestMapping(value="/check-sms-code",produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult checkSmsCode(String mobileNum, String validcode ) {
		try {
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
//			String code = (String)request.getSession().getAttribute("smscode");
			Member member = memberManager.getMemberByMobile(mobileNum);
			if(member==null){
				return JsonResultUtil.getErrorJson("没有找到用户");
			}
			if(SmsUtil.validSmsCode(validcode, mobileNum, SmsTypeKeyEnum.BACKPASSWORD.toString())) {
				request.getSession().setAttribute("smscode", "999");
				request.getSession().setAttribute("smsnum", member.getMember_id());
				return JsonResultUtil.getSuccessJson("验证成功");
			} else {
				return JsonResultUtil.getErrorJson("验证失败");
			}
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson(e.getMessage());
		}
		
		
	}
	
	
	/**
	 * 验证通过后重置密码
	 * 需要传入mobileNum一个参数
	 * 
	 * @param mobileNum 新密码,String型
	 * 
	 * @return 返回json串
	 * result  为1表示调用成功0表示失败 ，int型
	 * message 为提示信息
	 */
	@ResponseBody
	@RequestMapping(value="/reset-password",produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult resetPassword(String password) {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		
		String code = (String)request.getSession().getAttribute("smscode");
		Integer memberid = (Integer)request.getSession().getAttribute("smsnum");
		if(memberid!=null && "999".equals(code)) {
			try {
				memberManager.updatePassword(memberid, password);
				request.getSession().setAttribute("smscode", null);
				return JsonResultUtil.getSuccessJson("新密码设置成功");
			} catch(Exception e) {
				return JsonResultUtil.getErrorJson("设置密码出错");
			}
			
		} else {
			return JsonResultUtil.getErrorJson("认证超时，请重新验证");
		}
	}
		
	protected String createRandom(){
		Random random  = new Random();
		StringBuffer pwd=new StringBuffer();
		for(int i=0;i<6;i++){
			pwd.append(random.nextInt(9));
			 
		}
		return pwd.toString();
	}
	
	/**
	 * 忘记密码
	 * @param mobile 手机号
	 * @param sms_code 短信验证码
	 * @param password 新密码
	 * @param re_password 确认密码
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/new-password",produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult newPassword(String mobile, String sms_code, String password, String re_password){
		if (StringUtil.isEmpty(mobile)) {
			return JsonResultUtil.getErrorJson("手机号不能为空！");							
		}
		Member member = memberManager.getMemberByMobile(mobile);
		if(member==null){
			return JsonResultUtil.getErrorJson("该手机号不存在！");
		}else{
			if (!SmsUtil.validSmsCode(sms_code, mobile, SmsTypeKeyEnum.CHECK.toString())) {
				return JsonResultUtil.getErrorJson("短信验证码错误");
			}
			if (StringUtil.isEmpty(password)) {
				return JsonResultUtil.getErrorJson("新密码不能为空！");							
			}
			if(!password.equals(re_password)){
				return JsonResultUtil.getErrorJson("两次密码不一致！");	
			}
			member.setPassword(StringUtil.md5(password));
			memberManager.edit(member);
			
			return JsonResultUtil.getSuccessJson("重置密码成功");
		}
	}
}
