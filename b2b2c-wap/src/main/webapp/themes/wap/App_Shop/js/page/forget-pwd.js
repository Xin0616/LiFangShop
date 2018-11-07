
/*获取手机验证码,验证码倒计时*/
var getCode = document.getElementById("get_code");
var wait = 60;
getCode.addEventListener("tap", function () {
    var that = getCode;
    var mobileVal = document.getElementById("telephone").value;
    if(!regEx.IsPhoneNumber(mobileVal)){
    	$("#hintinfo").text("请输入正确的手机号码!!!");
        popup.show();
		$("#closepopup").on("tap",function(){
			popup.hide();
		});	
        return false;
    }else{
        $myAjax({
            url:ctx + '/api/shop/sms/send-sms-code.do?key=check&mobile=' + mobileVal,
            dataType:'json',
            type:'post',
            success: function () {
                time(that);
            },
            error: function () {
                console.log(2)
            }
        })
    }
})
/*验证码倒计时*/
function time(btn){
    if (wait===0) {
        btn.removeAttribute("disabled");
        btn.innerHTML = "获取验证码";
        wait = 60;
    }else{
        btn.setAttribute("disabled",true);
        btn.innerHTML = wait + "秒后重试";
        wait--;
        setTimeout(function(){
            time(btn);
        },1000);
    }
}
/***********验证密码**********/
var upwd = false;
var reupwd = false;
$('#pwd').blur(function () {
    var pwd = $(this).val();
    var res = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$/g;
    if (pwd == '') {
    	$("#hintinfo").text("密码不能为空");
        popup.show();
		$("#closepopup").on("tap",function(){
			popup.hide();
		});	
        
    } else if (pwd.length < 6) {
    	$("#hintinfo").text("密码不能少于6位");
        popup.show();
		$("#closepopup").on("tap",function(){
			popup.hide();
		});	
    } else if (!res.test(pwd)) {
    	$("#hintinfo").text('请输入6-20英文数字组合密码');
        popup.show();
		$("#closepopup").on("tap",function(){
			popup.hide();
		});	
    } else {
        upwd = true;
        /***********再次验证密码**********/
        $('#confirm-pwd').blur(function (e) {
        	e.preventDefault();
            var pwd = $('#pwd').val();
            var confirm = $(this).val();
            if(pwd) {
                if (confirm != pwd) {
                	$("#hintinfo").text('俩次输入密码必须相同');
                    popup.show();
            		$("#closepopup").on("tap",function(){
            			popup.hide();
            		});
                } else {
                    reupwd = true;
                }
            }
        });
    }
});
$("#save").on("tap", function () {
    if(upwd && reupwd){
        $myAjax({
            url:ctx + '/api/shop/findPassword/new-password.do',
            data:{
                mobile:$("#telephone").val(),
                sms_code:$("#code").val(),
                password:$("#pwd").val(),
                re_password:$("#confirm-pwd").val()
            },
            dataType:'json',
            type:'post',
            success:function(data){
                //服务器返回响应，根据响应结果，分析是否登录成功；
                if (data.result == 1) {
                	$("#hintinfo").text("修改密码成功！！！");
                    popup.show();
            		$("#closepopup").on("tap",function(){
            			popup.hide();
            		});
                    setTimeout(function () {
                        $myAjax({
                            url:ctx + '/api/shop/member/login.do',
                            data:{
                                username: $("#telephone").val(),
                                password: $("#pwd").val()
                            },
                            dataType:'json',
                            type:'post',
                            timeout:10000,//超时时间设置为10秒；
                            success:function(data){
                                //服务器返回响应，根据响应结果，分析是否登录成功；
                                if (data.result == 1) {
                                    window.location.href = ctx + "/App_Shop/page1/index.html";
                                }else {
                                	$("#hintinfo").text(data.message);
                                    popup.show();
                            		$("#closepopup").on("tap",function(){
                            			popup.hide();
                            		});
                                }
                            },
                            error:function(xhr,type,errorThrown){
                                //异常处理；
                            	$("#hintinfo").text('出现错误，请重试！');
                                popup.show();
                        		$("#closepopup").on("tap",function(){
                        			popup.hide();
                        		});
                            }
                        });
                    },1000)
                }else {
                	$("#hintinfo").text(data.message);
                    popup.show();
            		$("#closepopup").on("tap",function(){
            			popup.hide();
            		});
                }
            },
            error: function(e){
                var a = "";
            }
        })
    }
})