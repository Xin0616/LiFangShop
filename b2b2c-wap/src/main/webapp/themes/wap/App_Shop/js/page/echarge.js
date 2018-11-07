function goToPay(type){
	var apply_num = $("#apply_num").val();
	if(apply_num == ""){
		mui.alert("请输入充值金额");
	}else{
		$myAjax({
		    url:ctx + '/api/shop/recharge/add.do',
		    data:{
		    	apply_num: apply_num,
		    	paymentId: type
		    },
		    dataType:'text',
		    type:'post',
		    timeout:10000,//超时时间设置为10秒；
		    success:function(data){
		        $("body").html(data);
		    },
		    error:function(xhr,type,errorThrown){
		        //异常处理；
		    	module.message.error('出现错误，请重试！');
		    }
		});
	}
}