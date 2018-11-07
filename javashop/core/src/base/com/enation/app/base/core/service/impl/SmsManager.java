package com.enation.app.base.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.enation.app.base.core.model.SmsPlatform;
import com.enation.app.base.core.plugin.sms.ISmsSendEvent;
import com.enation.app.base.core.service.ISmsManager;
import com.enation.eop.processor.core.freemarker.FreeMarkerPaser;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.plugin.IPlugin;

import net.sf.json.JSONObject;

/**
 * 短信管理Manager
 * @author xulipeng	
 * @author wangxin 6.0升级改造
 */
@Service("smsManager")
@SuppressWarnings("rawtypes")
public class SmsManager  implements ISmsManager {
	
	@Autowired
	private IDaoSupport daoSupport;
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.base.core.service.ISmsManager#getList()
	 */
	@Override
	public List getList() {
		List list = this.daoSupport.queryForList("select * from es_sms_platform");
		return list;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.base.core.service.ISmsManager#addSmsPlatform(com.enation.app.base.core.model.SmsPlatform)
	 */
	@Override
	public void addSmsPlatform(SmsPlatform smsPlatform) {
		this.daoSupport.insert("es_sms_platform", smsPlatform);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.base.core.service.ISmsManager#getSmsPlatformHtml(java.lang.String, java.lang.Integer)
	 */
	@Override
	public String getSmsPlatformHtml(String pluginid,Integer smsid) {
		
		FreeMarkerPaser fp =  FreeMarkerPaser.getInstance();
		IPlugin installPlugin = null;
		installPlugin = SpringContextHolder.getBean(pluginid);
		fp.setClz(installPlugin.getClass());
		
		Map<String,String> params = this.getConfigParams(smsid);
		fp.putData(params);
		return fp.proessPageContent();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.base.core.service.ISmsManager#setParam(java.lang.Integer, java.util.Map)
	 */
	@Override
	public void setParam(Integer id, Map<String,String> param) {
		String sql ="update es_sms_platform set config=? where id=?";
		this.daoSupport.execute(sql, JSONObject.fromObject(param).toString(),id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.base.core.service.ISmsManager#get(java.lang.Integer)
	 */
	@Override
	public SmsPlatform get(Integer id) {
		String sql = "select * from es_sms_platform where id=?";
		SmsPlatform platform =  (SmsPlatform) this.daoSupport.queryForObject(sql, SmsPlatform.class, id);
		return platform;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.base.core.service.ISmsManager#send(java.lang.String, java.lang.String, java.util.Map)
	 */
	@Override
	@SuppressWarnings("static-access")
	public boolean send(String phone, String content,Map param) {
		boolean flag = false;
		try {
			String sql ="select * from es_sms_platform where is_open=1";
			SmsPlatform platform =  (SmsPlatform) this.daoSupport.queryForObject(sql, SmsPlatform.class);
			
			//判断是否设置了短信网关组件 add by DMRain 2016-3-17
			if (platform != null) {
				String config = platform.getConfig();
				JSONObject jsonObject = JSONObject.fromObject( config ); 
				
				String code = "";
				if(param!=null){
					code = (String) param.get("code");
				}
				
				Map itemMap = (Map)jsonObject.toBean(jsonObject, Map.class);
				itemMap.put("code", code);
				
				ISmsSendEvent smsSendEvent = SpringContextHolder.getBean(platform.getCode());
				flag = smsSendEvent.onSend(phone, content, itemMap);
			} else {
				flag = false;
			}
			
		} catch (RuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		return flag;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.base.core.service.ISmsManager#open(java.lang.Integer)
	 */
	@Override
	public void open(Integer id) {
		this.daoSupport.execute("update es_sms_platform set is_open=0");
		this.daoSupport.execute("update es_sms_platform set is_open=1 where id=?", id);
	}
	
	
	private Map<String, String> getConfigParams(Integer id) {
		SmsPlatform platform = this.get(id);
		String config  = platform.getConfig();
		if(null == config ) return new HashMap<String,String>();
		JSONObject jsonObject = JSONObject.fromObject( config );  
		Map itemMap = (Map)jsonObject.toBean(jsonObject, Map.class);
		return itemMap;
	}
	
	 //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    static final String accessKeyId = "LTAIJ4oqOLoZLplz";
    static final String accessKeySecret = "kk2lkgaiwoI4IRTWsUi3m4puSrNbWf";

    public static SendSmsResponse sendSms(String phone, String content, String code ,int j) throws ClientException {
    	String template="SMS_75970199";
    	String templateParam="{\"customer\":\""+code+"\" }";
    	//1是登录 ，2是注册，3找回密码，4绑定账号，5修改密码，6普通校验
    	if(j==1){
    		template="SMS_75970198";//登录确认验证码
    		templateParam="{\"code\":\""+code+"\",\"product\":\"荔方生活\"}";
    	}else if(j==2){
    		template="SMS_75970196";//用户注册验证码
    		templateParam="{\"code\":\""+code+"\",\"product\":\"荔方生活\"}";
    	}else if(j==3){
    		template="SMS_75970200";//身份验证验证码
    		templateParam="{\"code\":\""+code+"\",\"product\":\"荔方生活\"}";
    	}else if(j==4){
    		template="SMS_75970193";//信息变更验证码
    		templateParam="{\"code\":\""+code+"\",\"product\":\"荔方生活\"}";
    	}else if(j==5){
    		template="SMS_75970194";//修改密码验证码
    		templateParam="{\"code\":\""+code+"\",\"product\":\"荔方生活\"}";
    	}else {
    		template="SMS_75970200";//身份验证验证码
    		templateParam="{\"code\":\""+code+"\",\"product\":\"荔方生活\"}";
    	}
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phone);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName("阿里云短信测试专用");
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(template);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam(templateParam);
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        //request.setOutId("yourOutId");

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        return sendSmsResponse;
    }


    public static QuerySendDetailsResponse querySendDetails(String bizId ,String phone, String content, String code ,int j) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber(phone);
        //可选-流水号
        request.setBizId(bizId);
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        //必填-页大小
        request.setPageSize(10L);
        //必填-当前页码从1开始计数
        request.setCurrentPage(1L);

        //hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);

        return querySendDetailsResponse;
    }

    public  void sends(String phone, String content, String code ,int j) throws InterruptedException, ClientException {

        //发短信
        SendSmsResponse response = sendSms( phone,  content,  code ,j);
        System.out.println("短信接口返回的数据----------------");
        System.out.println("Code=" + response.getCode());
        System.out.println("Message=" + response.getMessage());
        System.out.println("RequestId=" + response.getRequestId());
        System.out.println("BizId=" + response.getBizId());

        Thread.sleep(3000L);

        //查明细
        if(response.getCode() != null && response.getCode().equals("OK")) {
            QuerySendDetailsResponse querySendDetailsResponse = querySendDetails(response.getBizId(),phone,  content,  code ,j);
            System.out.println("短信明细查询接口返回数据----------------");
            System.out.println("Code=" + querySendDetailsResponse.getCode());
            System.out.println("Message=" + querySendDetailsResponse.getMessage());
            int i = 0;
            for(QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : querySendDetailsResponse.getSmsSendDetailDTOs())
            {
                System.out.println("SmsSendDetailDTO["+i+"]:");
                System.out.println("Content=" + smsSendDetailDTO.getContent());
                System.out.println("ErrCode=" + smsSendDetailDTO.getErrCode());
                System.out.println("OutId=" + smsSendDetailDTO.getOutId());
                System.out.println("PhoneNum=" + smsSendDetailDTO.getPhoneNum());
                System.out.println("ReceiveDate=" + smsSendDetailDTO.getReceiveDate());
                System.out.println("SendDate=" + smsSendDetailDTO.getSendDate());
                System.out.println("SendStatus=" + smsSendDetailDTO.getSendStatus());
                System.out.println("Template=" + smsSendDetailDTO.getTemplateCode());
            }
            System.out.println("TotalCount=" + querySendDetailsResponse.getTotalCount());
            System.out.println("RequestId=" + querySendDetailsResponse.getRequestId());
        }

    }

}
