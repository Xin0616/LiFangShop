$(function(){
    //点击注册按钮跳转到注册界面
    document.getElementById('register').addEventListener('tap', function() {
        //打开注册页面
        mui.openWindow({
            url: 'register.html'
        });
    });
    //点击忘记密码按钮跳转到界面
    document.getElementById('forget-pwd').addEventListener('tap', function() {
        this.style.color = "#E73602";
        //打开忘记密码页面
        mui.openWindow({
            url: 'forget-pwd.html'
        });
    });
    var accountBox = document.getElementById("account");
    var passwordBox = document.getElementById("password");
    var submitForm = document.getElementById("submitForm");
    
	localStorage.act = "";
	if(localStorage.username != undefined){
		$("#account").val(localStorage.username);
		$("#password").val(localStorage.password);
	}
    
    /*按钮登录*/
    submitForm.addEventListener("tap",function () {
		if(!accountBox.value.match(/^1[34578]\d{9}$/)){
			$("#hintinfo").text("请输入正确的手机号码!!!");
			popup.show();
			$("#closepopup").on("tap",function(){
				popup.hide();
			});
		}
        if(regEx.IsNull(accountBox.value)&&regEx.IsNull(passwordBox.value)){
            $myAjax({
                url:ctx + '/api/shop/member/login.do',
                data:{
                    username: accountBox.value,
                    password: passwordBox.value
                },
                dataType:'json',
                type:'post',
                timeout:10000,//超时时间设置为10秒；x
                success:function(data){
                    //服务器返回响应，根据响应结果，分析是否登录成功；
                	if (data.result == 1) {
                		localStorage.username = accountBox.value;
                		localStorage.password = passwordBox.value;
                		if(data.message == "0"){
                            window.location.href = ctx + "/App_Shop/page2/index.html";
                		}else{
                			localStorage.setItem("check","yes");
                			console.log(111);
                            window.location.href = ctx + "/App_Shop/page1/index.html";
                        }
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
        }else{
        	$("#hintinfo").text("用户名或者密码未填写");
        	popup.show();
    		$("#closepopup").on("tap",function(){
    			popup.hide();
    		});
        }

    })
})