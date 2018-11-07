/**
 * 
 */
package com.enation.app.shop.core.pagecreator.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.base.core.service.ProgressContainer;
import com.enation.app.shop.core.pagecreator.service.IPageCreateManager;
import com.enation.eop.processor.HttpCopyWrapper;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.jms.EopJmsMessage;
import com.enation.framework.jms.EopProducer;

/**
 * 静态页面生成管理
 * @author kingapex
 *2015-3-26
 */
@Service("pageCreateManager")
public class PageCreateManager implements IPageCreateManager {

	@Autowired
	private EopProducer eopProducer;
 
	
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.pagecreator.service.IPageCreateManager#startCreate(java.lang.String[])
	 */
	@Override
	public boolean startCreate(String[] choose_pages) {
		
		if (ProgressContainer.getProgress(PageCreateProcessor.PRGRESSID) != null) {
			return false;
		} // 有任务正在生成中

		EopJmsMessage jmsMessage = new EopJmsMessage();
		Map<String, Object> data = new HashMap<String, Object>();
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		data.put("myrequest", new HttpCopyWrapper(request));
		data.put("choose_pages", choose_pages);

		jmsMessage.setData(data);
		jmsMessage.setProcessorBeanId("pageCreateProcessor");
		eopProducer.send(jmsMessage);
		System.out.println("下达任务成功");
		return true;
	}

 

}
