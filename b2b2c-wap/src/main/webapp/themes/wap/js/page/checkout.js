/**
 * Created by Andste on 2016/11/18.
 */
$(function () {
    var module  = new Module();
    var _remark, pay_type, ship_day;
    var url = $("#url").val();
    try {
        _remark  = $.cookie('checkout_remark');
        pay_type = $.cookie('pay_type');
        ship_day = $.cookie('ship_day');
    } catch (error){
        module.message.error('请查看您是否禁用了浏览器cookie！');
    }
    init();

    //  初始化
    function init() {
    	backHistory();
        bindEvents();
    }
    function backHistory() {
        module.navigator.init({
            title: '填写订单',
            left : {
                click: function () {
                	location.href = ctx + url;
                	
                }
            }
        });
    }
    if(isAndroid){
     	//手机物理后退键按钮
    	document.addEventListener("backbutton", onBackKeyDownCheck, false);
    	function onBackKeyDownCheck(e) {
    	   e.preventDefault();
    	   location.href = ctx + url;
    	}
	}
    
    function bindEvents() {

        //  初始选中地址
        initAddress();

        //  跳转到新建地址
        $('.no-address').on('tap', function () {
            location.replace('../member/address-add.html?back=checkout');
            return false
        })

        //  支付方式
        if(pay_type && pay_type != 'null'){
            $('#paymentId').val(pay_type)
            pay_type == 0 ? $('#pay-way').html('在线支付') : $('#pay-way').html('货到付款');
            $('#paymentId').val(pay_type);
        }else {
            $('#paymentId').val(0);
        }

        //  配送时间
        if(ship_day && ship_day != 'null'){
            $('.ship-day').html(ship_day);
            $('#shipDay').val(ship_day);
        }else {
            $('.ship-day').html('任意日期');
            $('#shipDay').val('任意日期');
        }

        //  备注信息
        $('#open-remark').on('tap', function () {
            openRemark();
            return false
        })

        //  读取备注信息
        $('#data-remark')[0].value     = _remark || '';
        $('#view-remark')[0].innerHTML = _remark || '未填写';

        //  提交订单
        $('#cerate-order').on('click', function () {
            cerateOrder();
        })
    }

    //  初始选中地址
    function initAddress() {
        var address_id = $("input[name='addressId']").val();
        if (!address_id) { return false }
        $.ajax({
            url  : ctx + '/api/store/store-order/change-address.do',
            data : {
                address_id: address_id
            },
            type : 'POST',
            success: function (res) {
                if (res.result == 1) {
                    //  console.log(res)
                }else {
                    module.message.error(res.message);
                }
            },
            error: function () {
                module.message.error('初始化地址出错，请稍后重试！');
            }
        })

    }

    //  打开备注输入框
    function openRemark() {
        var _input;
        $.ajax({
            url    : './checkout-remark.html',
            type   : 'GET',
            success: function (html) {
                layer.open({
                    content: html,
                    btn    : ['确定', '取消'],
                    yes    : function (index) {
                        //  本来是用的sessionStorage，遇到无痕浏览也是无奈啊╮(╯_╰)╭
                        try {
                            $.cookie('checkout_remark', _input.val());
                            /*location.reload();*/
                            location.href=ctx + '/checkout/checkout.html';
                        } catch (error){
                            module.message.error('请查看您是否禁用了浏览器cookie！');
                        }
                    },
                    success: function () {
                        _input = $('#input-remark');
                        _remark ? _input.val(_remark) : _input.val('');
                        _input.on('input propertychange', function () {
                            var $this = $(this),
                                _val  = $this.val();
                            $this.val(module.removeEmojiCode(_val));
                        })
                        $('#clean-remark').on('tap', function () {_input.val('').focus(); return false})
                    }
                });
            }
        })
    }

    //  提交订单
    function cerateOrder() {
        var inputs = $('input[name]'), length = inputs.length, data = {};
        var type = GetQueryString("type");
        for (var i = 0; i < length; i++) {
            var _input  = inputs.eq(i)[0],
                _name   = _input.name,
                _val    = _input.value;
            data[_name] = _val;
        }

        if(!data.addressId){module.message.error('请添加一个地址！');return false}

        $.ajax({
            url : ctx + '/api/store/store-order/create.do',
            data:{ 
            	data:data,
            	is_group:type
            	},
            type: 'POST',
            success: function (res) {
                if(res.result == 1){
                    try {
                        $.cookie('checkout_remark', '');
                        $.cookie('pay_type', null);
                        $.cookie('ship_day', null);
                        $.cookie('shipChecked', []);
                    } catch(error) {
                        module.message.error('请查看您是否禁用了浏览器cookie！');
                    }

                    if(res.data.payment_type == 'onlinePay'){
                        module.message.success('订单创建成功！', function () {
                            location.href = ctx + '/member/order-list.html';
                        });
                    }else {
                        location.href = './checkout-success.html?ordersn=' + res.data.sn;
                    }
                }else {
                    module.message.error(res.message, function () {
                        if(res.message.indexOf('购物车不能为空') > -1){
                            location.href = ctx + '/App_Shop/page1/index.html';
                        }
                    });
                }
            },
            error: function () {
                module.message.error('出现错误，请重试！');
            }
        })
    }
})