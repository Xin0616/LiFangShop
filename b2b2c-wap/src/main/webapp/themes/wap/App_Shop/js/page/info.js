$(function () {
	var url = $("#url").val();
	var maskPhoto = mui.createMask();
	if(isAndroid){
     	//手机物理后退键按钮
    	document.addEventListener("backbutton", onBackKeyDown, false);
	}
	$("#face").click(function() {
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
		mui.alert('Failed because: ' + message);
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
		$("#face").attr("src", aaa.data.img);
		layer.close(mask);
	}

	//上传失败
	function onErrorFile(error) {

		$("#hintinfo").text("An error has occurred: Code = " + error.code);
        popup.show();
 		$("#closepopup").on("tap",function(){
 			popup.hide();
 		});
   }
	
    //保存修改信息
    $("#info").on("tap", function () {
        if(checkForm("info-form") == true){
            var mobile = $("#telephone").val();
            var nickname = $("#nickname").val();
            var face = $("#face").attr("src");
            var sex = $("input[name='sex']:checked").val()
            var apartment_id = $("#apartment_id").val();
            var job = $("#job").val();
            var block_num = $("#address_num").val();
            $myAjax({
                url:ctx + '/api/shop/member/update-member.do',
                data:{
                    mobile: mobile,
                    nickname: nickname,
                    face: face,
                    apartment_id: apartment_id,
                    sex: sex,
                    job:job,
                    block_num: block_num
                },
                dataType:'json',
                type:'post',
                success:function(data){
                    //服务器返回响应，根据响应结果，分析是否登录成功；
                    if (data.result == 1) {
                    	
                    	$("#hintinfo").text("信息保存成功！页面自动跳转");
                        popup.show();
                		$("#closepopup").on("tap",function(){
                			popup.hide();
                		});
                		setTimeout(function(){
                			self.location=document.referrer;
                		},2000);
                		
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
            $("#hintinfo").text("信息验证格式不正确，请填写正确信息！");
            popup.show();
    		$("#closepopup").on("tap",function(){
    			popup.hide();
    		});
        }
    })
})
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
    if (val != '') {
        $("#query li").each(function () {
            if ($(this).find("a").html().indexOf(val) < 0) {
                $(this).hide();
            } else if ($(this).find("a").html() == val) {
                $(this).show();
            } else {
                $("#query li").show();
            }
        });
    }
}