/**
 * Created by Andste on 2016/12/15.
 */
$(function () {
    var module = new Module();
    var url = $("#url").val();
    init();

    function init() {
        module.navigator.init({
            title: '订单详情',
            left : {
                click: function () {
                	location.href = ctx + url;
                }
            }
        });
        toPay();
    }
  //手机物理后退键按�?	
    if(isAndroid){
     	//手机物理后退键按钮
    	document.addEventListener("backbutton", onBackKeyDown, false);
	}
    //  去支�?    function toPay() {
        $('.payment-online').on('tap', '.to-pay', function () {
            var $this      = $(this),
                order_id   = $this.attr('order-id'),
                payment_id = $this.attr('payment-id');
            location.href = ctx + '/api/b2b2c/store-payment-api/pay.do' + '?orderid=' + order_id + '&paymentid=' + payment_id
        });
        
        $('.payment-online').on('tap', '.balance', function () {
            var $this = $(this);
            var order_id = $this.attr('order-id');
            var apply_num = $this.attr('apply-num');
            location.href = ctx + '/App_Shop/page1/pay.html?apply_num=' +  apply_num + '&order_id=' + order_id;
        });
    }
})
