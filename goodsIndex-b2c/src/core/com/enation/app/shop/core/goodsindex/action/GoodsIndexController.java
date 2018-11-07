package com.enation.app.shop.core.goodsindex.action;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.base.core.service.ProgressContainer;
import com.enation.app.shop.core.goodsindex.service.impl.GoodsIndexManager;
import com.enation.framework.action.JsonResult;
import com.enation.framework.jms.EopJmsMessage;
import com.enation.framework.jms.EopProducer;
import com.enation.framework.util.JsonResultUtil;

/**
 * 商品索引生成action
 * @author kingapex
 *2015-5-14
 */
@Controller
@RequestMapping("/shop/admin/goods-index")
public class GoodsIndexController {
	protected final Logger logger = Logger.getLogger(getClass());
	@Autowired
	private EopProducer eopProducer;

	/**
	 * 转向生成页面
	 */
	@RequestMapping(value="input")
	public String execute(){
		
		return "/shop/admin/goodsindex/create";
	}
	
	
	/**
	 * 生成索引
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="create")
	public JsonResult create(){
		
		try {
			
			if (ProgressContainer.getProgress(GoodsIndexManager.PRGRESSID)!=null ){
				ProgressContainer.remove(GoodsIndexManager.PRGRESSID);
				return JsonResultUtil.getErrorJson("有索引任务正在进行中，需等待本次任务完成后才能再次生成。");
			} else{
				
				EopJmsMessage jmsMessage = new EopJmsMessage();
				jmsMessage.setProcessorBeanId("goodsIndexManager");
				eopProducer.send(jmsMessage);
				return JsonResultUtil.getSuccessJson("索引任务下达成功");
			}
		} catch (Exception e) {
			this.logger.error("生成出错", e);
			 return JsonResultUtil.getErrorJson("生成出错");
		}
	}

	
}
