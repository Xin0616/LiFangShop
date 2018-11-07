package com.enation.app.shop.component.pagecreator.plugin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.enation.app.shop.core.decorate.plugin.IDecorateSaveEvent;
import com.enation.app.shop.core.pagecreator.service.impl.GeneralPageCreator;
import com.enation.eop.processor.HttpCopyWrapper;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.plugin.AutoRegisterPlugin;
import com.enation.framework.util.StringUtil;


/**
 * 商城首页静态页生成
 * @author Sylow
 * @version v1.0,2016年7月21日
 * @since v6.1
 */
@Component
public class IndexCreatePlugin extends AutoRegisterPlugin implements IDecorateSaveEvent {

	private final static String pageName = "/index.html";
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.decorate.plugin.IDecorateSaveEvent#onSave(java.lang.String, java.lang.Integer)
	 */
	@Override
	public void onSave(String type, Integer id) {
		// TODO Auto-generated method stub
		try {
			this.createIndexPage();
		} catch(RuntimeException e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 创建首页静态页
	 */
	private void createIndexPage(){
		// 包装新的request
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		
		// 这里楼层参数有冲突，临时remove掉，后期可以在包装时remove掉所有参数
		request.removeAttribute("title");
		HttpCopyWrapper newrequest = new HttpCopyWrapper(request);

		String rootPath = StringUtil.getRootPath();
		newrequest.setServletPath(pageName);
		ThreadContextHolder.setHttpRequest(newrequest);

		String pagePath = rootPath + "/html/" + pageName;
		GeneralPageCreator pageCreator = new GeneralPageCreator(pagePath);
		pageCreator.parse(pageName);
		
	}

}
