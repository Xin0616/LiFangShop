package com.enation.app.b2b2c.core.order.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.b2b2c.core.order.service.ICaseStoreOrderManager;
import com.enation.app.base.core.model.PluginTab;
import com.enation.app.base.core.service.IRegionsManager;
import com.enation.app.shop.core.order.model.DlyCenter;
import com.enation.app.shop.core.order.model.DlyType;
import com.enation.app.shop.core.order.model.Order;
import com.enation.app.shop.core.order.model.PayCfg;
import com.enation.app.shop.core.order.plugin.order.OrderPluginBundle;
import com.enation.app.shop.core.order.service.IDlyCenterManager;
import com.enation.app.shop.core.order.service.IDlyTypeManager;
import com.enation.app.shop.core.order.service.IOrderManager;
import com.enation.app.shop.core.order.service.IPaymentManager;
import com.enation.app.shop.core.order.service.OrderStatus;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;

import net.sf.json.JSONArray;

/**
 * 店铺订单管理
 * @author LiFenLong
 * @version v2.0,2016年3月8日 版本改造  by Sylow
 * @since v6.0
 */
@Controller
@RequestMapping("/b2b2c/admin/case-store-order")
public class CaseStoreOrderController extends GridController{
	@Autowired
	private OrderPluginBundle orderPluginBundle;
	@Autowired
	private IDlyCenterManager dlyCenterManager;
	@Autowired
	private IOrderManager orderManager;
	@Autowired
	private IDlyTypeManager dlyTypeManager;
	@Autowired
	private IPaymentManager paymentManager;
	@Autowired
	private ICaseStoreOrderManager caseStoreOrderManager;
	@Autowired
	private IRegionsManager regionsManager;

	/**
	 * 分页读取订单列表
	 * 根据订单状态 state 检索，如果未提供状态参数，则检索所有
	 * @param statusMap 订单状态集合,Map
	 * @param payStatusMap 订单付款状态集合,Map
	 * @param shipMap,订单配送人状态集合,Map
	 * @param shipTypeList 配送方式集合,List
	 * @param payTypeList 付款方式集合,List
	 * @return 订单列表
	 */
	@RequestMapping(value="list")
	public ModelAndView list(){

		
		ModelAndView view=getGridModelAndView();
		
		
		List statusList=OrderStatus.getOrderStatus();
		
		List payStatusList = OrderStatus.getPayStatus();
			
		List shipList = OrderStatus.getShipStatus();
		
		
		List<DlyType> shipTypeList = dlyTypeManager.list();
		List<PayCfg> payTypeList = paymentManager.list();

		view.addObject("statusList", statusList);
		view.addObject("payStatusList", payStatusList);
		view.addObject("shipList", shipList);
		view.addObject("shipTypeList", shipTypeList);
		view.addObject("payTypeList", payTypeList);
		
		view.addObject("status_Json", JSONArray.fromObject(statusList).toString());
		view.addObject("payStatus_Json", JSONArray.fromObject(payStatusList).toString() );
		view.addObject("ship_Json", JSONArray.fromObject(shipList).toString());
		view.setViewName("/b2b2c/admin/order/case_store_order_list");
		return view;
	}
	
	
	
	
	
	/**
	 * @author LiFenLong
	 * @param stype 搜索状态, Integer
	 * @param keyword 搜索关键字,String
	 * @param start_time 开始时间,String
	 * @param end_time 结束时间,String
	 * @param sn 订单编号,String
	 * @param ship_name 订单收货人姓名,String
	 * @param status 订单状态,Integer
	 * @param paystatus 订单付款状态,Integer
	 * @param shipstatus 订单配送状态,Integer
	 * @param shipping_type 配送方式,Integer
	 * @param payment_id 付款方式,Integer
	 * @param order_state 订单状态_从哪个页面进来搜索的(未付款、代发货、等),String
	 * @param complete 是否订单为已完成,String 
	 * @return 订单列表 json
	 */
	@ResponseBody
	@RequestMapping("list-json")
	public GridJsonResult listJson(@RequestParam(value = "start_time", required = false) String start_time, @RequestParam(value = "end_time", required = false) String end_time) {
		HttpServletRequest requst = ThreadContextHolder.getHttpRequest();
		Map caseMap = new HashMap();
		caseMap.put("end_time", end_time);
		caseMap.put("start_time", start_time);
		
		this.webpage = this.caseStoreOrderManager.listOrder(caseMap, this.getPage(),this.getPageSize(), this.getSort(),this.getOrder());
		return JsonResultUtil.getGridJson(webpage); 
	}
	
	/**
	 * 删除商品
	 * @author LiFenLong
	 * @param goods_id 商品Id数组,Integer[]
	 * @return json
	 * result 1.操作成功。0.操作失败
	 */
	@ResponseBody
	@RequestMapping(value="/edit")
	public JsonResult edit(Integer[] order_id) {
		try {
//			if (order_id != null)
//				for (Integer goodsid : order_id) {
//					if (caseStoreOrderManager.checkGoodsInCart(goodsid)) {
//
//						return JsonResultUtil.getErrorJson("删除失败，此商品已加入购物车");
//					
//					}
//				}
			
			caseStoreOrderManager.edit(order_id);
			return JsonResultUtil.getSuccessJson("收款成功");
		} catch (RuntimeException e) {
			logger.error("收款失败", e);
			return JsonResultUtil.getErrorJson(e.getMessage());
		}
	}
	/**
	  * 跳转至订单详细页面
	  * @param ship_name 收货人姓名,String
	  * @param orderId 订单号,Integer
	  * @param ord 订单,Order
	  * @param provinceList 省列表
	  * @param pluginTabs 订单详细页的选项卡
	  * @param pluginHtmls 订单详细页的内容
	  * @param dlycenterlist 发货信息列表
	  * @return 订单详细页面
	  */
	@RequestMapping(value="/order-detail")
	public ModelAndView orderDetail(String ship_name,Integer orderId,Integer status){
		ModelAndView view = new ModelAndView();
		if(ship_name!=null ) ship_name = StringUtil.toUTF8(ship_name);
		Order ord = this.orderManager.get(orderId);
		List provinceList = this.regionsManager.listProvince();
		 
		List<DlyCenter> dlycenterlist= dlyCenterManager.list();
		view.addObject("provinceList", provinceList);
		view.addObject("dlycenterlist", dlycenterlist);
		view.addObject("orderId", orderId); 
		view.addObject("ship_name", ship_name); 
		view.addObject("status", status); 
		view.addObject("sn", ord.getSn());
		view.addObject("ord", ord); 
		List<PluginTab> pluginTabs = this.orderPluginBundle.getDetailHtml(ord);
		view.addObject("pluginTabs",pluginTabs); 
		view.setViewName("/b2b2c/admin/order/case_order_detail"); 
		return view;
	}
	
}
