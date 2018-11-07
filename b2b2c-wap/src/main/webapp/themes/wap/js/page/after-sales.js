/**
 * Created by Andste on 2016/12/23.
 */
$(function () {
    var module = new Module();
    init();


    function init() {
        module.navigator.init('申请售后');
        bindEvents();
    }

    function bindEvents() {

        //  tab切换
        changeTab();

        //  申请退货数量
        salesNum();

        //  提交申请
        submit();

        //  退款账号输入检测
        checkAccountInput();
        
    }

    //  tab切换
    function changeTab() {
        if($('.after-sales-nav').length == 0){return false}
        var contentBox = $('.after-sales-content');
        $('.after-sales-nav').on('tap', '.nav-item', function () {
            var $this = $(this),
                index = $this.index();

            $this.addClass('active').siblings().removeClass('active');
            contentBox.find('.content-item').removeClass('show');
            contentBox.find('.content-item').eq(index).addClass('show');
            return false
        })
    }

    //  申请退货数量
    function salesNum() {
        var input = $('#sales-goods-num'),
            symbolLess = $('.symbol-less'),
            symbolAdd  = $('.symbol-add'),
            maxNum = parseInt(input.siblings('.max-num').val()),
            price  = parseFloat($('#price').val()),
            applyAlltotal = $('#apply-alltotal');

        $('.sales-update-num').on('tap', '.goods-symbol', function () {
            var $this     = $(this),
                _disabled = $this.is('.disabled'),
                _isAdd    = $this.is('.symbol-add');
            if (_disabled) {return false}
            var _val = parseInt(input.val());
            _val     = _isAdd ? _val + 1 : _val - 1;
            _setNum(_val);
            return false
        })

        input.on('blur', function () {
            var $this = $(this),
                _val  = $this.val();
            _val = parseInt(_val);
            _val = module.regExp.integer.test(_val) ? _val : 1;
            _val = _val < 1 ? 1 : _val;
            _val = _val > maxNum ? maxNum : _val;
            _setNum(_val);
        })


        function _setNum(_val) {
            _val > (maxNum - 1) ? symbolAdd.addClass('disabled') : symbolAdd.removeClass('disabled');
            _val < 2 ? (function () {
                    symbolLess.addClass('disabled');
                    return false
                })() : (function () {
                    symbolLess.removeClass('disabled');
                    return false
                })()
            applyAlltotal.val(parseFloat(_val * price).toFixed(2));
            input.val(_val);
        }
    }


    //  提交申请
    function submit() {
        $('#submit-btn').on('tap', function () {
            $('.content-item.refund').is('.show') ? _submitRefund() : _submitReturns();
            return false
        })

        var data = {};
        var isRefund = true;
        var url;

        //  退款
        function _submitRefund(){
            var inputs = $('.content-item.refund').find("*[name]");
            data = _returnData(inputs);
            isRefund = true;
            url = ctx + '/api/shop/sell-back/return-money.do'

            _checkError();
        }

        //  退货
        function _submitReturns(){
            var inputs = $('.content-item.returns').find("*[name]");
            data = _returnData(inputs);
            isRefund = false;
            url = ctx + '/api/shop/sell-back/return-goods.do'

            _checkError();
        }


        function _returnData(_inputs) {
            var _data = {};
            for(var i = 0; i < _inputs.length; i++){
                var _input = _inputs.eq(i),
                    _name  = _input.attr('name'),
                    _val   = _input.val();
                if(_name == 'remark'){_val = $.trim(_val)}
                _data[_name] = _val;
            }
            return _data;
        }

        //  检查错误
        function _checkError() {
            if(!isRefund && data.refund_way == 0){module.message.error('请选择退款方式！'); return false}
            if(!data.return_account){module.message.error('请填写退款账户！'); return false};
            if(!module.regExp.price.test(data.apply_alltotal)){module.message.error('退款金额格式有误！'); return false};
            if(data.reason == 0){module.message.error(isRefund ? '请选择退款原因！' : '请选择退货原因！'); return false}
            if(!data.remark){module.message.error('请填写问题描述！'); return false}

            _ajaxSubmit();
        }

        //  ajax提交
        function _ajaxSubmit() {
            $.ajax({
                url : url,
                data: data,
                type: 'POST',
                success: function (res) {
                    if(res.result == 1){
                        module.message.success(res.message, function () {
                            location.replace('./order-detail.html?ordersn=' + module.getQueryString('ordersn'));
                        })
                    }else {
                        module.message.error(res.message);
                    }
                },
                error: function () {
                    module.message.error('出现错误，请重试！');
                }
            })
        }
    }

    //  退款账号输入移除部分emoji，并且检测输入长度
    function checkAccountInput(){
        $('.sales-account-input').on('input propertychange', function () {
            var $this = $(this),
                _val  = $this.val();
            _val = _val.length > 30 ? _val.substring(0, 29) : _val;
            _val = module.removeEmojiCode(_val);
            $this.val(_val);
        })
    } 
})


	