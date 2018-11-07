
var goodsjs = {
		//更新购物车数量
		cart_num:function(){
			var cart_groupbuy = $(".groupbuy").attr("rel");
			if(cart_groupbuy==1){
				$.ajax({
					url:ctx + "/api/shop/cart/get-cart-data.do",
					dataType:"json",
					success:function(result){
						if(result.result==1){
							var goodscartnum = result.data.count;
							$(".my_cart span").empty();
							$(".my_cart span").text(goodscartnum);
						}else{
							$("#hintinfo").text(result.message);
							popup.show();
							setTimeout(function(){
								popup.hide();
	                		},1000);	
						}
					},error:function(){
						$("#hintinfo").text("出现错误，请重试！");
						popup.show();
						setTimeout(function(){
							popup.hide();
                		},1000);	
					}
				});
			}else{
				$.ajax({
					url: ctx + "/api/shop/cart/get-cart-data.do",
					dataType:"json",
					success:function(result){
						if(result.result==1){
							var goodscartnum = result.data.count;
							$(".my_cart span").empty();
							$(".my_cart span").text(goodscartnum);
						}else{
							$("#hintinfo").text(result.message);
							popup.show();
							setTimeout(function(){
								popup.hide();
	                		},1000);	
						}
					},error:function(){
						$("#hintinfo").text("抱歉，购物车数量");
						popup.show();
						setTimeout(function(){
							popup.hide();
                		},1000);	
					}
				});
			}
		},
		
		//增加减少数量
		goods_stock:function(){
			var store = parseInt($(".goods_add").attr("rel"));
			$(".goods_cut").click(function(){
				var goodsnum = parseInt($(".goodsnum").val());
				if(goodsnum>1){
					$(".goodsnum").val(goodsnum-1);
				}
				else{
					$("#hintinfo").text("抱歉，最少需要一件商品");
					popup.show();
					setTimeout(function(){
						popup.hide();
            		},1000);	
				}
			});
			
			$(".goods_add").click(function(){
				var goodsnum = parseInt($(".goodsnum").val());
				if(goodsnum<store){
					$(".goodsnum").val(goodsnum+1);
				}
				else{
					$("#hintinfo").text("抱歉，购买商品不能大于库存");
					popup.show();
					setTimeout(function(){
						popup.hide();
            		},1000);	
				}
			});
			$(".goodsnum").blur(function(){
				var goodsnums = parseInt($(this).val());
				//验证单位
				if(isNaN(goodsnums)){
					$("#hintinfo").text('数量单位只能为数字类型！');
					popup.show();
					setTimeout(function(){
						popup.hide();
            		},1000);
					$(this).val(parseInt(1));
				}
				//验证库存
				if(goodsnums>store){
					$("#hintinfo").text("请输入小于库存的数量");
					popup.show();
					setTimeout(function(){
						popup.hide();
            		},1000);	
					$(this).val(parseInt(1));
				}
				//验证数量
				if(goodsnums<1){
					$("#hintinfo").text("最少买一件哦");
					popup.show();
					setTimeout(function(){
						popup.hide();
            		},1000);	
					$(this).val(parseInt(1));
				}
			});
			
		},
		//立即购买
		buy:function(){
			$(".buy_now").click(function(){
				var isgroupbuy = $(".isgroupbuy").attr("rel");
				var gnum = $(".goodsnum").val();
				var gid = $(".goods_tools").attr("rel");
				var storeid = $(".goods_tools").attr("id");
				
				var productid = $("#productid").val();
				var spec = $(".cart_way [name='havespec']").val();
				if(gnum<1){	
					$("#hintinfo").text("抱歉，库存不足！");
					popup.show();
					setTimeout(function(){
						popup.hide();
            		},1000);	
					return false;
				}
				show.show_cover();       //增加遮罩。来阻止在提交ajax时进行其他操作。
				if(isgroupbuy==1){
					$.ajax({
						url: ctx + "/api/shop/cart/add-goods.do?goodsid="+gid+"&store_id="+storeid+"&num="+gnum,
						dataType:"json",
						success : function(data) {	
							if(data.result==1){																
								location.href = ctx + '/cart.html';
							}
							else{	
								show.close_cover();          //关闭遮罩
								$("#hintinfo").text(data.message);
								popup.show();
								setTimeout(function(){
									popup.hide();
		                		},1000);
							}
						}
					})
				}
				else{
					show.show_cover();       //增加遮罩。来阻止在提交ajax时进行其他操作。
					if(spec==1){
						
						var specLi = parseInt($(".spec_item ul").length);
						var specLiSelected = parseInt($(".sp_txt .hovered").length);
						if(specLi != specLiSelected){
							$("#hintinfo").text("请选择规格");
							popup.show();
							setTimeout(function(){
								popup.hide();
	                		},1000);	
							show.close_cover();       //增加遮罩。来阻止在提交ajax时进行其他操作。
							return false;
						}
						
						$.ajax({
							url: ctx + "/api/shop/cart/add-product.do?goodsid="+gid+"&store_id="+storeid+"&num="+gnum+"&productid="+productid+"&havespec=1&showCartData=0",
							dataType:"json",
							success : function(data) {	
								if(data.result==1){
								    location.href= ctx + "/cart.html";
								    show.close_cover();
								}
								else{	
									show.close_cover();       //增加遮罩。来阻止在提交ajax时进行其他操作。
									$("#hintinfo").text(data.message);
									popup.show();
									setTimeout(function(){
										popup.hide();
			                		},1000);	
								}
							}
						})
					}else{
						$.ajax({
							url: ctx + "/api/shop/cart/add-goods.do?goodsid="+gid+"&store_id="+storeid+"&num="+gnum,
							dataType:"json",
							success : function(data) {	
								if(data.result==1){
								    location.href = ctx + "/cart.html";
								    show.close_cover();
                                }
								else{	
									show.close_cover();       //增加遮罩。来阻止在提交ajax时进行其他操作。
									$("#hintinfo").text(data.message);
									popup.show();
									setTimeout(function(){
										popup.hide();
			                		},1000);	
								}
							}
						})
					}
				}
				
			})
		},
		//加入购物车
		incart:function(){
			$(".in_cart").click(function(){
				var isgroupbuy = $(".isgroupbuy").attr("rel");
				var gnuma = $(".goodsnum").val();
				var gida = $(".goods_tools").attr("rel");
				var addstoreid = $(".goods_tools").attr("id");
				
				var productid = $("#productid").val();
				var spec = $(".cart_way [name='havespec']").val();
				show.show_cover();          //关闭遮罩
				if(isgroupbuy==1){
					$.ajax({
						url: ctx + "/api/store/store-cart/add-goods.do?goodsid="+gida+"&store_id="+addstoreid+"&num="+gnuma,
						dataType:"json",
						success : function(data) {	
							if(data.result==1){	
								show.close_cover();          //关闭遮罩
								$("#hintinfo").text("加入购物车成功");
								popup.show();
								setTimeout(function(){
									popup.hide();
									e.stopPropagation();
		                		},1000);
								$.ajax({
									url: ctx + "/api/shop/cart/get-cart-data.do",
									dataType:"json",
									success:function(result){
										if(result.result==1){
											var goodscartnum = result.data.count;
											$(".my_cart span").empty();
											$(".my_cart span").text(goodscartnum);
										}else{
											show.close_cover();          //关闭遮罩
											$("#hintinfo").text(data.message);
											popup.show();
											setTimeout(function(){
												popup.hide();
					                		},1000);	
										}
									},error:function(){
										show.close_cover();          //关闭遮罩
										$("#hintinfo").text("抱歉，收藏出现意外错误");
										popup.show();
										setTimeout(function(){
											popup.hide();
				                		},1000);	
									}
								});
							}
							else{
								show.close_cover();          //关闭遮罩
								$("#hintinfo").text(data.message);
								popup.show();
								setTimeout(function(){
									popup.hide();
		                		},1000);	
							}
						}
					})
				}
				else{
					show.show_cover();          //关闭遮罩
					
					var specLi = parseInt($(".spec_item ul").length);
					var specLiSelected = parseInt($(".sp_txt .hovered").length);
					//判断选择规格数量与规格数量是否相同，如果不同，表示有未选的商品规格
					if(specLi != specLiSelected){
						$("#hintinfo").text("请选择规格");
						popup.show();
						setTimeout(function(){
							popup.hide();
                		},1000);	
						show.close_cover();       //增加遮罩。来阻止在提交ajax时进行其他操作。
						return false;
					}
					
					if(spec==1){
						$.ajax({
							url: ctx + "/api/store/store-cart/add-product.do?goodsid="+gida+"&store_id="+addstoreid+"&num="+gnuma+"&productid="+productid+"&havespec=1",
							dataType:"json",
							success : function(data) {	
								if(data.result==1){	
									show.close_cover();          //关闭遮罩
									$("#hintinfo").text("加入购物车成功");
									popup.show();
									setTimeout(function(){
										popup.hide();
										e.stopPropagation();
			                		},1000);	
									$.ajax({
										url: ctx + "/api/shop/cart/get-cart-data.do",
										dataType:"json",
										success:function(result){
											if(result.result==1){
												var goodscartnum = result.data.count;
												$(".my_cart span").empty();
												$(".my_cart span").text(goodscartnum);
											}else{
												show.close_cover();          //关闭遮罩
												$("#hintinfo").text(result.message);
												popup.show();
												setTimeout(function(){
													popup.hide();
						                		},1000);	
											}
										},error:function(){
											show.close_cover();
											$("#hintinfo").text("抱歉，收藏出现意外错误");
											popup.show();
											setTimeout(function(){
												popup.hide();
					                		},1000);	
										}
									});
								}
								else{	
									show.close_cover();          //关闭遮罩
									$("#hintinfo").text(result.message);
									popup.show();
									setTimeout(function(){
										popup.hide();
			                		},1000);	
								}
							}
						})
					}else{
						$.ajax({
							url: ctx + "/api/store/store-cart/add-goods.do?goodsid="+gida+"&store_id="+addstoreid+"&num="+gnuma,
							dataType:"json",
							success : function(data) {	
								if(data.result==1){	
									show.close_cover();
									$("#hintinfo").text("加入购物车成功");
									popup.show();
									setTimeout(function(){
										popup.hide();
			                		},1000);				                	
									$.ajax({
										url: ctx + "/api/shop/cart/get-cart-data.do",
										dataType:"json",
										success:function(result){
											if(result.result==1){  
												var goodscartnum = result.data.count;
												$(".my_cart span").empty();
												$(".my_cart span").text(goodscartnum);
											}else{
												show.close_cover();          //关闭遮罩
												$("#hintinfo").text(result.message);
												popup.show();
												setTimeout(function(){
													popup.hide();
						                		},1000);	
											}
										},error:function(){
											show.close_cover();
											$("#hintinfo").text("抱歉，收藏出现意外错误");
											popup.show();
											setTimeout(function(){
												popup.hide();
					                		},1000);	
											
						                	
										}
									});
								}
								else{	
									show.close_cover();          //关闭遮罩
									$("#hintinfo").text(result.message);
									popup.show();
									setTimeout(function(){
										popup.hide();
			                		},1000);	
								}
							}
						})
					}
				}
			})
		},

		//加入收藏
		goodscollect:function(){
			$(".goods_collect").click(function(){
				var collect = $(this).attr("rel");
				$.ajax({
					url: ctx + "/api/shop/collect/add-collect.do?goods_id="+collect,
					dataType:"json",
					success:function(result){
						if(result.result==1){
							$("#hintinfo").text("收藏成功");
							popup.show();
							setTimeout(function(){
								popup.hide();
								e.stopPropagation();
	                		},1000);
						}else{
							$("#hintinfo").text(result.message);
							popup.show();
							setTimeout(function(){
								popup.hide();
	                		},1000);	
						}
					},error:function(){
						$("#hintinfo").text("抱歉，收藏出现意外错误");
						popup.show();
						setTimeout(function(){
							popup.hide();
                		},1000);	
					}
				});
				
			});
		},
		//图片滚动
		goodsimg:function(){
			var goodsnum= $(".goods_images li").length;    //获取大幅图片中li的总个数。
			for(i=1;i<=goodsnum;i++){    //for循环，定i=1,每次循环就加1; 当i的值由1循环到等于result的值一样时（即“文本框的name='text'的值”）就停止循环
				var createobj = $("<a></a>"); //把div定义为变量createobj
				$("#goods_circle").append(createobj); //把createobj这个变量追加到html中的'body'里
				};
				/*具体我也没太读懂*/
				var active=0,                   
				as=document.getElementById('goods_circle').getElementsByTagName('a');	
				for(var i=0;i<as.length;i++){
					(function(){
						var j=i;
						as[i].onclick=function(){
							t2.slide(j);
							return false;
						}
					})();
				};
				var t2=new TouchSlider({id:'slider', speed:600, timeout:4000, before:function(index){
						as[active].className='';
						active=index;
						as[active].className='active';
					}});
			  /*循环滚动结束*/
		}
		
};
