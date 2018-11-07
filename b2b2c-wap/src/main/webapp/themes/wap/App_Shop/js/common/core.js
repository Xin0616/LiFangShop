var _path = "http://www.jqtit.com/lishenasms";
var pageNum = 1;
var numPerPage = 10;
var _this;
var u = navigator.userAgent;
var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
//后退到index中mine页面
function IndexMine(){
	$("nav .mui-tab-item").removeClass("mui-active");
	$("nav .mui-tab-item[href='#mine']").addClass("mui-active");
	$(".mui-content .mui-control-content").removeClass("mui-active");
	$("#mine").addClass("mui-active");
}
//安卓手机物理后推键
function onBackKeyDown(e) {
	e.preventDefault();
	//判断是否有元素获得焦点
   if($('input:focus').length==0) {
	   location.href = ctx + $("#url").val();
   }else{
   //移除焦点
	   $('input:focus').blur();
   }   
}
//自定义弹出框
var maskPhoto = "";
var popup={
		show:function(data){
			maskPhoto = mui.createMask();
			maskPhoto.show();
			$(".mui-backdrop").html($("#mypopup").prop("outerHTML"));
			$(".mui-backdrop #mypopup").show();
			$("#fail").css({"display":"block"});
			$("#suc").css({"display":"none"});
			$("#hintinfo").text(data);
			
//			$("#mypopup").show().children("div").show();
//			$("#fail").css({"display":"block"});
//			$("#suc").css({"display":"none"});
//			$("#hintinfo").text(data);
		},
		hide:function(){
//			$("#mypopup").hide().children("div").hide();
//			$("#fail").css({"display":"none"});
//			$("#suc").css({"display":"none"});
			
			$("#fail").css({"display":"none"});
			$("#suc").css({"display":"none"});
			maskPhoto.close();
		}
};
//获取url中的参数
function GetQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if(r != null) return decodeURI(r[2]);
	return null;
}

//自定义ajax方法
function $myAjax(conf) {
	var mask = null;
	mask = layer.load(2, {
		shade: [0.8, "#FFFFFF"],
		content: "<div class='loadMask'>" + (conf.loadMsg == undefined ? "" : conf.loadMsg) + "</div>"
	});
	$.ajax({
		type: conf.type,
		url: conf.url,
		async: conf.async == null ? true : conf.async,
		data: conf.data,
		dataType: conf.dataType,
		success: function(data) {
			if(conf.success != null) {
				conf.success.call(this, data);
			}
			if(mask != null) {
				layer.close(mask);
			}
		},
		error: function(e) {
			if(conf.error != null) {
				conf.error.call(this, e);
			} else {
				alert("连接服务器错误。");
				if(mask != null) {
					layer.close(mask);
				}
			}
			if(mask != null) {
				layer.close(mask);
			}
		}
	});

};

Date.prototype.Format = function(fmt) { //author: meizz 
	var o = {
		"M+": this.getMonth() + 1, //月份 
		"d+": this.getDate(), //日 
		"h+": this.getHours(), //小时 
		"m+": this.getMinutes(), //分 
		"s+": this.getSeconds(), //秒 
		"q+": Math.floor((this.getMonth() + 3) / 3), //季度 
		"S": this.getMilliseconds() //毫秒 
	};
	if(/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for(var k in o)
		if(new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}
//form校验
function checkForm(id) {
	//获取表单中所有input
	var judg = true;
	$("#" + id + " input").each(function() {
		//判断是否是需要提交的数据
		if($(this).attr("name") != undefined && $(this).attr("name") != "") {
			$(this).removeClass("error");
			//input的值
			var val = $(this).val();
			//获取校验信息
			var str = $(this).attr("data-options");
			if(str != undefined) {
				//string转json
				var dataOptions = eval('(' + str + ')');

				//是否不能为空
				if(dataOptions.required != undefined && dataOptions.required) {
					if(val == undefined || val == '') {
						$(this).addClass("error");
						judg = false;
						return false;
					}
				}
				//是否有最大长度限制
				if(dataOptions.maxLength != undefined) {
					if(val.length > dataOptions.maxLength) {
						$(this).addClass("error");
						judg = false;
						return false;
					}
				}
				//是否有最小长度限制
				if(dataOptions.minLength != undefined) {
					if(val.length < dataOptions.minLength) {
						$(this).addClass("error");
						judg = false;
						return false;
					}
				}
				//是否有输入内容格式限制
				if(dataOptions.dataType != undefined) {
					//是否为手机号
					if(dataOptions.dataType == "phone") {
						var reg = /^1[34578]\d{9}$/;
						if(!reg.test(this.value)) {
							$(this).addClass("error");
							judg = false;
							return false;
						}
					}
					//是否为整数
					if(dataOptions.dataType == "integer") {
						var reg = /^-?\d+$/;
						if(!reg.test(this.value)) {
							$(this).addClass("error");
							judg = false;
							return false;
						}
					}
					//是否为小数
					if(dataOptions.dataType == "decimal") {
						var reg = /^-?\d+\.?\d{0,2}$/;
						if(!reg.test(this.value)) {
							$(this).addClass("error");
							judg = false;
							return false;
						}
					}
                    //是否为密码
                    if(dataOptions.dataType == "password") {
                        var reg = /^[a-zA-Z]\w{5,17}$/;
                        if(!reg.test(this.value)) {
                            $(this).addClass("error");
                            judg = false;
                            return false;
                        }
                    }
				}
			}
		}
	});
	return judg;
}
function addError(obj){
    if($(obj).prev().tagName == "label"){
        $(obj).prev().addClass("error");
    }else if($(obj).parent().prev().tagName == "label"){
        $(obj).parent().prev().addClass("error");
    }
}
/*校验正则*/
var regEx = {
    /*验证非空*/
    IsNull: function (input) {
        if (input != '') {
            return true;
        } else {
            return false;
        }
    },
    /*手机号（首位1开头的11位数字）*/
    IsPhoneNumber: function (input) {
        var regex = /^1[34578]\d{9}$/;
        if (input.match(regex)) {
            return true;
        } else {
            return false;
        }
    },
    /*手机短信验证码（纯数字）*/
    IsPhoneCode: function (input) {
        var regex = /^1\d{10}$/;
        if (input.match(regex)) {
            return true;
        } else {
            return false;
        }
    },
    /*登录密码中英结合（以字母开头，长度在6~18之间，只能包含字符、数字和下划线）*/
    IsPassword: function (input) {
        var regex = /^[a-zA-Z]\w{5,17}$/;
        if (input.match(regex)) {
            return true;
        } else {
            return false;
        }
    }
};


