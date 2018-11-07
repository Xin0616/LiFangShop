package com.enation.app.b2b2c.front.api.member;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.b2b2c.core.goods.service.IStoreGoodsManager;
import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.model.StoreMemberComment;
import com.enation.app.b2b2c.core.member.service.IStoreMemberCommentManager;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.shop.core.goods.service.IGoodsManager;
import com.enation.app.shop.core.member.model.MemberComment;
import com.enation.app.shop.core.member.model.MemberOrderItem;
import com.enation.app.shop.core.member.service.IMemberCommentManager;
import com.enation.app.shop.core.member.service.IMemberOrderItemManager;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;

/**
 * 商品评论和咨询管理API
 * @author DMRain 2016年3月10日 版本改造
 * @version v2.0 改为spring mvc
 * @since v6.0
 */
@Controller
@Scope("prototype")
@RequestMapping("/api/b2b2c/store-comment-api")
public class StoreCommentApiController {

	protected final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private IGoodsManager goodsManager;

	@Autowired
	private IMemberOrderItemManager memberOrderItemManager;

	@Autowired
	private IStoreMemberCommentManager storeMemberCommentManager;

	@Autowired
	private IStoreMemberManager storeMemberManager;

	@Autowired
	private IStoreGoodsManager storeGoodsManager;
	
	@Autowired
	private IMemberCommentManager memberCommentManager;

	/**
	 * 添加评论或咨询
	 * 
	 * @param fileFileName
	 *            上传图片名称,String
	 * @param allowTYpe
	 *            上传类型,String
	 * @param file
	 *            上传对象,File
	 * @param memberComment
	 *            会员评论(咨询),StoreMemberComment
	 * @param commenttype
	 *            类型，int
	 * @param content
	 *            评论内容,String
	 * @param grade
	 *            评价等级,int
	 * @return 返回json串 result 为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonResult add(Integer commenttype, Integer goods_id, String content, Integer grade,
			Integer store_deliverycredit, Integer store_desccredit, Integer store_servicecredit) {
		try {
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			StoreMemberComment memberComment = new StoreMemberComment();

			// 判断是不是评论或咨询
			if (commenttype != 1 && commenttype != 2) {
				return JsonResultUtil.getErrorJson("系统参数错误！");
			}

			memberComment.setType(commenttype);

			Map goods = goodsManager.get(goods_id);

			// 判断是否存在此商品
			if (goods == null) {
				return JsonResultUtil.getErrorJson("此商品不存在！");
			}

			memberComment.setGoods_id(goods_id);

			// 判断评论内容
			if (StringUtil.isEmpty(content)) {
				return JsonResultUtil.getErrorJson("评论或咨询内容不能为空！");
			} else if (content.length() > 1000) {
				return JsonResultUtil.getErrorJson("请输入1000以内的内容！");
			}

			content = StringUtil.htmlDecode(content);
			memberComment.setContent(content);

			StoreMember member = storeMemberManager.getStoreMember();
			if (member == null) {
				return JsonResultUtil.getErrorJson("只有登录成功且购买过此商品的用户才能发表评论和咨询！");
			}

			int store_id = (Integer) goods.get("store_id");
			memberComment.setStore_id(store_id);

			if (commenttype == 1) {
				int buyCount = memberOrderItemManager.count(member.getMember_id(), goods_id);
				int commentCount = memberOrderItemManager.count(member.getMember_id(), goods_id, 1);

				// 评论不审核全部通过
				memberComment.setStatus(1);

				if (grade < 1 || grade > 5) {
					return JsonResultUtil.getErrorJson("请选择对商品的评价！");
				} else {
					memberComment.setGrade(grade);
				}
			}

			memberComment.setMember_id(member == null ? 0 : member.getMember_id());
			memberComment.setDateline(System.currentTimeMillis() / 1000);
			memberComment.setIp(request.getRemoteHost());

			memberComment.setStore_deliverycredit(store_deliverycredit == null ? 0 : store_deliverycredit);
			memberComment.setStore_desccredit(store_desccredit == null ? 0 : store_desccredit);
			memberComment.setStore_servicecredit(store_servicecredit == null ? 0 : store_servicecredit);

			storeMemberCommentManager.add(memberComment);

			// 更新已经评论过的商品
			if (commenttype == 1) {
				MemberOrderItem memberOrderItem = memberOrderItemManager.get(member.getMember_id(), goods_id, 0);
				if (memberOrderItem != null) {
					memberOrderItem.setComment_time(System.currentTimeMillis());
					memberOrderItem.setCommented(1);
					memberOrderItemManager.updateComment(memberOrderItem);
				}
			}
			
			
			storeGoodsManager.editStoreGoodsGrade(goods_id);
			return JsonResultUtil.getSuccessJson("发表成功");
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("发表评论出错", e);
			return JsonResultUtil.getErrorJson("发表评论出错：" + e.getMessage());
		}

	}


	/**
	 * 回复咨询
	 * 
	 * @param reply
	 *            回复内容,String
	 * @param comment_id
	 *            评论Id,Integer
	 * @param memberComment
	 *            会员评论,Map
	 * @param member
	 *            店铺会员,StoreMember
	 * @return 返回json串 result 为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value = "/edit")
	public JsonResult edit(Integer status, String reply, Integer comment_id) {
		if (StringUtil.isEmpty(reply)) {
			return JsonResultUtil.getErrorJson("回复不能为空！");
		}
		
		Map memberComment = storeMemberCommentManager.get(comment_id);
		
		if (memberComment == null) {
			return JsonResultUtil.getErrorJson("此评论或咨询不存在！");
		}
		
		try {
			StoreMember member = storeMemberManager.getStoreMember();
			//MemberComment memberComment1 = new MemberComment();
			//memberCommentManager.getGoodsCommentsCount(goods_id);
			Integer goodsid = memberCommentManager.get(comment_id).getGoods_id();
			//memberComment1.getGoods_id();
			memberComment.put("reply", reply);
			memberComment.put("replystatus", 1);
			memberComment.put("replytime", DateUtil.getDateline());
			memberComment.put("status", status);
			storeMemberCommentManager.edit(memberComment, comment_id);
			if(status == 1){
				//如果评论审核通过的话，商品页显示评论数应该加1
			storeGoodsManager.addStoreGoodsComment(goodsid);
			}
			return JsonResultUtil.getSuccessJson("回复成功");
		} catch (Exception e) {
			this.logger.error("回复失败:" + e);
			return JsonResultUtil.getErrorJson("回复失败");
		}
	}

	/**
	 * 添加多条评论
	 * @param commenttype
	 * @param goods_id  商品id
	 * @param product_id  货品id
	 * @param content  评论内容
	 * @param store_deliverycredit  发货速度评论分数
	 * @param store_desccredit 描述相符评论分数
	 * @param store_servicecredit 服务态度评论分数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addComment")
	public JsonResult adds(Integer commenttype[], Integer goods_id[],Integer product_id[], String content[], 
			Integer store_deliverycredit[], Integer store_desccredit[], Integer store_servicecredit[],Integer orderid) {
		try {
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			
			
			StoreMemberComment memberComment = new StoreMemberComment();
			Integer contentotal = 0; //添加字段用于判断该商品是否被评论
			for(int i=0;i<goods_id.length;i++){
				
				memberComment.setType(commenttype[i]);
	
				Map goods = goodsManager.get(goods_id[i]);
	
				// 判断是否存在此商品
				if (goods == null) {
					return JsonResultUtil.getErrorJson("此商品不存在！");
				}
	
				memberComment.setGoods_id(goods_id[i]);
				memberComment.setProduct_id(product_id[i]);
				// 判断评论内容
				Integer contentcount = 0; //添加字段用于判断该货品是否被评论
				if(content.length>0){
					if (!(StringUtil.isEmpty(content[i])||"请在此处输入您的评论".equals(content[i]))) {
						contentcount++;
						contentotal++;
					} 
					
					if (content[i].length() > 1000) {
						return JsonResultUtil.getErrorJson("请输入1000以内的内容！");
					} 
	
					String contents = StringUtil.htmlDecode(content[i]);
					memberComment.setContent(contents);
				}
				StoreMember member = storeMemberManager.getStoreMember();
				if (member == null) {
					return JsonResultUtil.getErrorJson("只有登录成功且购买过此商品的用户才能发表评论和咨询！");
				}
	
				int store_id = (Integer) goods.get("store_id");
				memberComment.setStore_id(store_id);
	
				memberComment.setStatus(0);
				String parameter = request.getParameter("grade_"+product_id[i]);
				Integer grade = StringUtil.toInt(parameter);
				if (grade < 1 || grade> 3) {
					return JsonResultUtil.getErrorJson("请选择对商品的评价！");
				} else {
					memberComment.setGrade(grade);
				}
				
	
				memberComment.setMember_id(member == null ? 0 : member.getMember_id());
				memberComment.setDateline(System.currentTimeMillis() / 1000);
				memberComment.setIp(request.getRemoteHost());
	
				memberComment.setStore_deliverycredit(store_deliverycredit[i] == null ? 0 : store_deliverycredit[i]);
				memberComment.setStore_desccredit(store_desccredit[i] == null ? 0 : store_desccredit[i]);
				memberComment.setStore_servicecredit(store_servicecredit[i] == null ? 0 : store_servicecredit[i]);
				if(contentcount>0){
					storeMemberCommentManager.add(memberComment);
					MemberOrderItem memberOrderItem = memberOrderItemManager.getMemberOrderItem(orderid, product_id[i], 0);
					if (memberOrderItem != null) {
						memberOrderItem.setComment_time(System.currentTimeMillis());
						memberOrderItem.setCommented(1);
						memberOrderItemManager.updateComment(memberOrderItem);
					}
					storeGoodsManager.editStoreGoodsGrade(goods_id[i]);
				}

			}
			//判断订单中是否有商品被评论；
			if(contentotal==0){
				return JsonResultUtil.getErrorJson("请至少评论一个商品！");
			}
			
			return JsonResultUtil.getSuccessJson("发表成功");
				
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("发表评论出错", e);
			return JsonResultUtil.getErrorJson("发表评论出错：" + e.getMessage());
				
		}
		
	}

}
