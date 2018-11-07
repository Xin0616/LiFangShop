/**
 * Created by Andste on 2016/12/8.
 */
$(function () {
    var module = new Module();
    var url = $("#url").val();
    init()
    function init() {
        bindEvents();
    }
    backHistory();
    function backHistory() {
        module.navigator.init({
            title: '购物车',
            left : {
                click: function () {
                	location.href = ctx + url;
                }
            }
        });
    }
	  //手机物理后退键按钮
    if(isAndroid){
     	//手机物理后退键按钮
    	document.addEventListener("backbutton", onBackKeyDownCart, false);
    	function onBackKeyDownCart(e) {
    	   e.preventDefault();
    	   location.href = ctx + url;
    	}
	}
	

    function bindEvents() {

        //  初始加载全选
        doCheckAll(true);

        //  加载商品总价
        loadTotalPage();

        //  店铺商品选择
        storeCheck();

        //  商品选择
        goodsCheck();

        //  全选商品
        checkAll();

        //  商品数量更新
        goodsNumUpdate();

        //  滑动删除
        deleteGoods();

        //  去结算
        goBill();
    }


    function loadTotalPage() {
        $('.total-bar').load('./cart/cart-total.html');
    }

    //  店铺商品选择
    function storeCheck() {
        $('.checkbox-store').on('tap', function () {
            var $this    = $(this),
                _checked = $this.is('.checked'),
                data_id  = $this.attr('data-id');
            var goodsCheckBoxs = $('.goods-store-' + data_id);
            _checked ? goodsCheckBoxs.removeClass('checked') : goodsCheckBoxs.addClass('checked');
            _checked ? $this.removeClass('checked') : $this.addClass('checked');
            $('.checkbox-store').length == $('.checkbox-store.checked').length ? $('#goods-check-all').addClass('checked') : $('#goods-check-all').removeClass('checked');
            _ajaxCheckStoreAll(!_checked, data_id);
            return false
        })
    }

    //  商品选择
    function goodsCheck() {
        $('.checkbox-goods').on('tap', function () {
            var $this      = $(this),
                _checked   = $this.is('.checked'),
                data_id    = $this.attr('data-id'),
                product_id = $this.attr('product-id'),
                exchange   = $this.attr('exchange') == 1;
            _checked ? $this.removeClass('checked') : $this.addClass('checked');
            if($('.goods-store-' + data_id).length == $('.goods-store-' + data_id + '.checked').length){
                $('#store-' + data_id).addClass('checked');
                if($('.checkbox-store').length == $('.checkbox-store.checked').length) {
                    $('#goods-check-all').addClass('checked');
                }
            }else {
                $('#store-' + data_id).removeClass('checked');
                $('#goods-check-all').removeClass('checked');
            }
            _ajaxCheckdGoods(!_checked, product_id, exchange);
            return false
        })
    }

    //  全选商品
    function checkAll() {
        $(document).on('tap', '#goods-check-all', function () {
            var $this    = $(this),
                _checked = $this.is('.checked');
            doCheckAll(!_checked);
            return false
        })
    }

    //  全选操作
    function doCheckAll(_checked) {
        var stores = $('.checkbox-store');
        for(var i = 0; i < stores.length; i++){
            var store_id = stores.eq(i).attr('data-id');
            _ajaxCheckStoreAll(_checked, store_id);
        }
        _checked ? $('.checked-item').addClass('checked') : $('.checked-item').removeClass('checked');
        _checked ? $('#goods-check-all').addClass('checked') : $('#goods-check-all').removeClass('checked');
    }

    //  商品数量更新
    function goodsNumUpdate() {
        var input;
        $('.content-store-item').on('tap', '.goods-symbol', function () {
            var $this     = $(this),
                _disabled = $this.is('.disabled'),
                _isAdd    = $this.is('.symbol-add');
            if(_disabled){return false}
            input = $this.siblings('.goods-num');
            var _val  = parseInt(input.val());
            _val = _isAdd ? _val + 1 : _val - 1;

            _ajaxUpdateNum(_val)
            return false
        })

        $('.goods-num').on('blur', function () {
            var $this = $(this),
                _val  = $this.val();
            input = $this;
            _val = module.regExp.integer.test(_val) ? _val : 1;
            _val = _val > 200 ? 1 : _val;
            _val = parseInt(_val);
            _ajaxUpdateNum(_val, 'blur');
        })

        function _ajaxUpdateNum(_val, type) {
            module.loading.open();
            $.ajax({
                url : ctx + '/api/store/store-cart/update-num.do',
                data: {
                    type     : Math.random(),
                    cartid   : input.attr('cart-id'),
                    productid: input.attr('product-id'),
                    num      : _val
                },
                timeoutTimer: 8000,
                type: 'POST',
                success: function (res) {
                    module.loading.close();
                    if(res.result == 1 && _val <= res.data.store){
                        _val < 2 ? _updateLessSymbol(true) : _updateLessSymbol(false);
                        _updateInputNum(_val)
                    }else {
                    	$("#hintinfo").text('抱歉，超出库存数量！');
                        popup.show();
        				$("#closepopup").on("tap",function(){
        					popup.hide();
        				});	
                        type == 'blur' ? _ajaxUpdateNum(1) : _ajaxUpdateNum(_val-1);
                    }
                },
                error: function () {
                    module.loading.close();
                    $("#hintinfo").text('出现错误，请重试！');
                    popup.show();
    				$("#closepopup").on("tap",function(){
    					popup.hide();
    				});	
                }
            })
        }

        //  更新input框内数字
        function _updateInputNum(num) {
            if(input){input.val(num)}
            loadTotalPage();
        }

        //  更新减号禁用状态
        function _updateLessSymbol(bool) {
            var sysmbolLess = input.siblings('.symbol-less')
            bool ? sysmbolLess.addClass('disabled') : sysmbolLess.removeClass('disabled');
        }
    }

    //  店铺商品选择ajax
    function _ajaxCheckStoreAll(checked, store_id) {
        module.loading.open();
        $.ajax({
            url : ctx + '/api/store/store-cart/check-store-all.do',
            data: {
                checked : checked,
                store_id: store_id
            },
            type: 'POST',
            timeoutTimer: 8000,
            success: function (res) {
                module.loading.close()
                if(res.result == 1){
                	
                    loadTotalPage();
                }
            },
            error: function () {
                module.loading.close();
                $("#hintinfo").text('出现错误，请重试！');
                popup.show();
				$("#closepopup").on("tap",function(){
					popup.hide();
				});	
            }
        })
    }

    //  商品单个选择ajax
    function _ajaxCheckdGoods(checked, product_id, exchange) {
        module.loading.open();
        $.ajax({
            url : ctx + '/api/store/store-cart/check-product.do',
            data: {
                checked   : checked,
                product_id: product_id,
                exchange  : exchange
            },
            type: 'POST',
            timeoutTimer: 8000,
            success: function (res) {
                module.loading.close();
                if(res.result == 1){
                    loadTotalPage();
                }
            },
            error: function () {
                module.loading.close();
                $("#hintinfo").text('出现错误，请重试！');
                popup.show();
				$("#closepopup").on("tap",function(){
					popup.hide();
				});	
            }
        })
    }

    //  滑动删除
    function deleteGoods() {
        var goodsItems = $('.goods-item');
        goodsItems.hammer().bind('swipeleft',function(){
            var $this = $(this);
            goodsItems.removeClass('active');
            $this.addClass('active');
            return false
        })

        goodsItems.hammer().bind('swiperight',function(){
            $(this).removeClass('active');
            return false
        })

        $('.delete-box').on('tap', function (e) {
            e.stopPropagation();
            var $this   = $(this),
                data_id = $this.attr('data-id');
            layer.open({
                content: '确定要移除该商品吗？',
                btn: ['确定', '取消'],
                yes: function(index){
                    _ajaxDeleteGoods(data_id);
                    layer.close(index);
                }
            });
            return false
        })

        $(document).on('click', function () {
            goodsItems.removeClass('active');
        })
    }

    //  移除一个商品
    function _ajaxDeleteGoods(data_id) {
        $.ajax({
            url : ctx + '/api/store/store-cart/delete.do',
            data: {
                cartid: data_id
            },
            type: 'POST',
            timeoutTimer: 800,
            success: function (res) {
                if(res.result == 1){
                    location.reload();
                }else {
                	$("#hintinfo").text(res.message);
                    popup.show();
					$("#closepopup").on("tap",function(){
						popup.hide();
					});	
                }
            },
            error: function () {
            	$("#hintinfo").text('出现错误，请重试！');
                popup.show();
				$("#closepopup").on("tap",function(){
					popup.hide();
				});	
            }
        })
    }

    //  去结算
    function goBill() {
        $('.go-bill').on('tap', function () {
            //  判断是否有失效商品
            if($('.goods-item').length < 1){
                module.message.error('购物车内没有商品哦，先去逛逛吧！', function () {
                    history.back();
                });
                return false
            };

            if($('.checked-item.checked').length < 1){
                module.message.error('您没有勾选商品哦！');
                return false
            }
            if($('.checked-item.checked').length > 0){
            	var str = "";
            	var result;
            	$($('.checked-item.checked').find('input[type="hidden"]')).each(function(){	
            		str += $(this).val();
            		var reg = /(.)(?=.*\1)/g;
                    result = str.replace(reg, "");   
                    return result;
            	})
            	
            	if(result != "0" && result != "1"){
            		$("#hintinfo").text('选择商品必须都为普通商品或者都为团购商品！');
            		popup.show();
					$("#closepopup").on("tap",function(){
						popup.hide();
					});	
            	}else{
            		$.ajax({
                        url: ctx + '/api/store/store-cart/invalid-goods.do',
                        dataType: "json",
                        success: function (res) {
                            if (res.result == 1) {
                            	$("#hintinfo").text('有商品已失效，即将刷新购物车！');
                                popup.show();
            					$("#closepopup").on("tap",function(){
            						popup.hide();
            					});	
                            } else {
                                location.href = ctx + "/checkout/checkout.html?type="+result;
                                
                            }
                        },
                        error: function () {
                        	$("#hintinfo").text('出现错误，请重试！');
                            popup.show();
        					$("#closepopup").on("tap",function(){
        						popup.hide();
        					});	
                        }
                    });
            	}
            	
            }
            return false
        })
    }

})