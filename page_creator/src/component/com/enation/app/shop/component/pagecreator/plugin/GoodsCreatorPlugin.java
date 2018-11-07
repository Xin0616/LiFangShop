package com.enation.app.shop.component.pagecreator.plugin;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.enation.app.shop.core.goods.plugin.IGoodsAfterAddEvent;
import com.enation.app.shop.core.goods.plugin.IGoodsAfterEditEvent;
import com.enation.app.shop.core.goods.plugin.IGoodsStartChange;
import com.enation.app.shop.core.pagecreator.service.impl.GeneralPageCreator;
import com.enation.eop.processor.HttpCopyWrapper;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.plugin.AutoRegisterPlugin;
import com.enation.framework.util.StringUtil;


/**
 * 商品静态页生成插件
 * @author kingapex
 *2015-3-25
 */
@Component
public class GoodsCreatorPlugin extends AutoRegisterPlugin implements
		IGoodsAfterAddEvent, IGoodsAfterEditEvent,IGoodsStartChange{

	/* (non-Javadoc)
	 * @see com.enation.app.shop.core.plugin.goods.IGoodsAfterEditEvent#onAfterGoodsEdit(java.util.Map, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void onAfterGoodsEdit(Map goods, HttpServletRequest request) {
		this.createGoodsPage(goods);
		
	}

	/* (non-Javadoc)
	 * @see com.enation.app.shop.core.plugin.goods.IGoodsAfterAddEvent#onAfterGoodsAdd(java.util.Map, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void onAfterGoodsAdd(Map goods, HttpServletRequest arg1)
			throws RuntimeException {
		this.createGoodsPage(goods);
	}
	
	private void createGoodsPage(Map goods){
		int goodsid = Integer.valueOf( goods.get("goods_id").toString() );
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		HttpCopyWrapper newrequest = new HttpCopyWrapper(request); 
		
		String root_path = StringUtil.getRootPath();
		String folder = root_path +"/html/goods";
		String pagename=("/goods-"+goodsid+".html");
		newrequest.setServletPath(pagename);
		ThreadContextHolder.setHttpRequest(newrequest);
		
		String pagePath =root_path+"/html/goods"+pagename;
		GeneralPageCreator pageCreator = new GeneralPageCreator(pagePath);
		pageCreator.parse(pagename);
		
	}

	@Override
	public void startChange(Map goods) {
		this.createGoodsPage(goods);
		
	}
	

}
