$(function(){
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
    };
    //表单校验
    
    $('#password').blur(function(){
    	var pwd = $(this).val();
        var res = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$/g;
        if (pwd == '') {
        	$("#hintinfo").text("密码不能为空");
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
        }
    });
    
	
	var mask = null;
    var maskPhoto = mui.createMask();
	function onSuccessImage(imageURL) {
		mask = layer.load(2, {
			shade: [0.8, "#FFFFFF"],
			content: "<div class='loadMask'>头像上传中</div>"
		});
		uploadFile(imageURL);
	}
    /*点击上传图片*/
    $("#pic").click(function() {
		maskPhoto.show();
		$(".mui-backdrop").html($("#maskDiv").html());
		$(".local").on("tap", function(){
	    	navigator.camera.getPicture(onSuccessImage, onErrorImage, {
		   		destinationType: Camera.DestinationType.FILE_URI,
		        sourceType: Camera.PictureSourceType.PHOTOLIBRARY
			});
		});
		$(".camera").on("tap", function(){
	    	navigator.camera.getPicture(onSuccessImage, onErrorImage, {
		   		destinationType: Camera.DestinationType.FILE_URI
			});
			maskPhoto.close();
		});
    });

	var mask = null;
	function onSuccessImage(imageURL) {
		mask = layer.load(2, {
			shade: [0.8, "#FFFFFF"],
			content: "<div class='loadMask'>头像上传中</div>"
		});
		uploadFile(imageURL);
	}

	function onErrorImage(message) {
		alert('Failed because: ' + message);
	}

	//上传文件
	function uploadFile(fileURL) {
		var uri = encodeURI("http://www.lifang3.com/api/base/upload-image/upload-image.do?subFolder=faceFile");
	    var options = new FileUploadOptions();
	    options.fileKey = "file";
	    options.fileName = fileURL.substr(fileURL.lastIndexOf('/')+1);
	    options.mimeType = "image/jpeg";
	    var headers = {'headerParam':'headerValue'};
	    options.headers = headers;

	    var ft = new FileTransfer();

	    ft.upload(fileURL, uri, onSuccessFile, onErrorFile, options);
	}

	//上传成功
	function onSuccessFile(rusult) {
		var aaa = eval('(' + rusult.response + ')');
		$("#pic").attr("src", aaa.data.img);
		layer.close(mask);
	}

	//上传失败
	function onErrorFile(error) {
		alert(JSON.stringify(rusult));
		alert("An error has occurred: Code = " + error.code);
   }
	
    function getObjectURL(file) {
        var url = null;
        if(window.createObjectURL != undefined) { // basic
            url = window.createObjectURL(file);
        } else if(window.URL != undefined) { // mozilla(firefox)
            url = window.URL.createObjectURL(file);
        } else if(window.webkitURL != undefined) { // webkit or chrome
            url = window.webkitURL.createObjectURL(file);
        }
        return url;
    }

    /*选择地址模态框滚动*/
    mui.init();
    mui('.mui-scroll-wrapper').scroll();
    //输入框内容改变触发
    function Change(obj) {
        $("#list_obj li").show();

        var val = $(obj).val();
        if(val != '') {
            $("#query li").each(function() {
                if($(this).html().indexOf(val) < 0) {
                    $(this).hide();
                }
                if($(this).find("a").html() == val) {
                }
            });
        }
    }
    /*用户注册事件绑定*/
    var mobile = $("#telephone");
    var validcode = $("#code");
    var password = $("#password");
    var nickname = $("#nickname");
    var face = $("#upload");
    var apartment_id = $("#apartment_id");
    $("input").on("focus",function(){
    	$("#error_info").css("display","none");
    });
    /*验证form证表单*/
    $("#reg").on("tap", function () {
        if(checkForm("register-form") == true){
            $myAjax({
                url:ctx + '/api/shop/member/reg-mobile.do',
                data:{
                    mobile: mobile.val(),
                    sms_code: validcode.val(),
                    password: password.val(),
                    nickname: nickname.val(),
                    face: face.val(),
                    apartment_id: apartment_id.val(),
                    license:"agree"
                },
                dataType:'json',
                type:'post',
                success:function(data){
                    //服务器返回响应，根据响应结果，分析是否登录成功；
                    if (data.result == 1) {
                    	$("#hintinfo").text("注册成功！！！");
                        popup.show();
                		$("#closepopup").on("tap",function(){
                			popup.hide();
                		});
                        setTimeout(function () {
                            window.location.href = ctx + "/App_Shop/login/login.html"
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
        }else{
        	var form_obj = {手机号:mobile.val(),验证码:validcode.val(),密码:password.val(),昵称:nickname.val(),所属小区:apartment_id.val()};
        	var form_arr = [];
        	for(var k in form_obj){
        		if(form_obj[k] == "" || form_obj[k] == null ){
        			form_arr.push(k);
        		}else{
        			$("#error_info").css("display","none");
        		}    		
        	}      	
        	$("#error_info .mui-input-row").html(form_arr + " 不能为空！");
        	$("#error_info").css("display","block");
        }

    })
});
function selected1(obj) {
    /*给输入框赋值*/
    $("#customerName").val($(obj).find("a").html());
    $("#apartment_id").val($(obj).find("input").val());
    //关闭列表
    mui('#query').popover('toggle');
}
/*输入框内容改变检索内容*/
function Change(obj) {
    var val = $(obj).val();
    $("#query li").show();
    if(val != '') {
        $("#query li").each(function() {
            if($(this).find("a").html().indexOf(val) < 0) {
                $(this).hide();
            }else if($(this).find("a").html() == val) {
            	$(this).show();
            }else{
                $("#query li").show();
            }
        });
    }
}