/**
 * Created by Andste on 2016/11/21.
 */
$(function () {
    var module = new Module();
    var region_id = module.getQueryString('region_id'), payType, shipDay, checkeds = [];
    try {
        payType  = $.cookie('pay_type') || null;
        shipDay = $.cookie('ship_day')  || null;
        var __checkeds = $.cookie('shipChecked') || null;
        checkeds = __checkeds ? __checkeds.split(",") : [];
    } catch (e) {
        module.message.error('请查看您是否禁用了cookie！');
    }

    init();

    var backToCheckout = function () {location.replace('../checkout/checkout.html')};
    function init() {
        module.navigator.init({
            title: '支付配送',
            left : {click: function () {backToCheckout()}}
        });
        bindEvents();
    }

    function bindEvents() {
        //  配送方式选择
        $('.ship-btn').on('tap', function () {
            var $this    = $(this),
                store_id = $this.attr('store_id'),
                type_id  = $this.attr('type_id');
            if($this.is('.checked')){return false}
            if(!region_id || !store_id || !type_id){module.message.error('抱歉，缺少必要参数！'); return false}

            _ajax(type_id, store_id, $this);
            return false
        })

        function _ajax(type_id, store_id, ele) {
            $.ajax({
                url : ctx + '/api/store/store-order/change-args-type.do',
                data: {
                    regionid: region_id,
                    store_id: store_id,
                    type_id : type_id
                },
                type: 'POST',
                success: function (res) {
                    if(res.result == 1){
                        ele.addClass('checked').siblings().removeClass('checked');
                        saveShipType();
                    }else {
                        module.message.error(res.message);
                    }
                },
                error: function () {
                    module.message.error('出现错误，请重试！');
                }
            })
        }

        function saveShipType() {
            var checkeds = $('.ship-btn.checked'), checkedArray = [];
            for(var i = 0; i < checkeds.length; i++){
                var __ = checkeds.eq(i);
                var type_id = __.attr('type_id');
                checkedArray.push(type_id);
            }

            $.cookie('shipChecked', checkedArray);
        }

        //  支付方式
        if (payType && payType != 'null') {
            payType == 0 ? $('.online').addClass('checked') : $('.ship').addClass('checked');
        }else {
            $('.online').addClass('checked');
        }

        //  配送方式
        if(checkeds.length > 1){
            for(var i in checkeds){
                if($(".ship-btn[type_id="+ checkeds[i] +"]").length == 1){
                    var __ = $(".ship-btn[type_id="+ checkeds[i] +"]");
                    __.addClass('checked');
                    _ajax(checkeds[i], __.attr('store_id'), __);
                }
            }
        }else {
            var shipWaps = $('.ship-way');
            for(var i = 0; i < shipWaps.length; i++){
                var __ = shipWaps.eq(i);
                __.find('.ship-btn').eq(0).addClass('checked');
            }
        }


        //  配送日期
        if(shipDay && shipDay != 'null'){
            if(shipDay == '任意日期'){
                $('.any').addClass('checked');
            }else if(shipDay == '双休日'){
                $('.rest').addClass('checked');
            }else if(shipDay == '工作日'){
                $('.work').addClass('checked');
            }
        }else {
            $('.any').addClass('checked');
        }
    }

    //  支付方式选择
    $('.payment-btn').on('tap', function () {
        var $this  = $(this),
            pay_type = $this.attr('pay_type');
        try {
            $.cookie('pay_type', pay_type);
            $this.addClass('checked').siblings().removeClass('checked');
        } catch (e) {
            module.message.error('请查看您是否禁用了浏览器cookie！');
        }
        return false
    });

    //  配送时间
    $('.ship_day').on('tap', function () {
        var $this = $(this),
            ship_day = $this.html();
        try {
            $.cookie('ship_day', ship_day);
            $this.addClass('checked').siblings().removeClass('checked');
        } catch (e) {
            module.message.error('请查看您是否禁用了浏览器cookie！');
        }
        return false
    })

    //  确定
    $('#save-btn').on('tap', function () {
        backToCheckout();
        return false
    });
})