/**
 * Created by Andste on 2016/12/13.
 */
$(function () {
    var module = new Module();

    init()

    function init() {

        module.navigator.init({
            title: '个人资料',
            backgroundColor: '#fff'
        });

        bindEvents();
    }


    function bindEvents() {

        //  修改头像
        changeHead();
        
        //  修改真实姓名
        fillName();

        //  性别选择
        sexSelect();
        
        //  出生日期选择
        dateSelect();

        //  保存修改
        saveInfo();
    }

    //  修改头像
    function changeHead() {
        document.querySelector('#update-head-img').addEventListener('change', function () {
            lrz(this.files[0], {quality: 0.3})
                .then(function (res) {
                    if(res.file.size > 204800){module.message.error('您的照片太大啦！'); return false}
                    $.ajax({
                        url : ctx + '/api/base/upload-image/upload-base64.do',
                        data: {
                            type  : 'avatar',
                            base64: res.base64,
                            base64Len: res.base64Len
                        },
                        type: 'POST',
                        dataType: 'json',
                        success: function (result) {
                            if(result.result == 1){
                                $('.img-head-img').attr('src', result.data.img);
                                $('#head-img-base64').val(result.data.fsimg);
                            }else {
                                module.message.error(result.message);
                            }
                        },
                        error: function () {
                            module.message.error('图片上传错误，请重试！');
                        }
                    })
                })
//                .catch(function (err) {
//                	//  这里eclipse里报错属正常现象，不必惊慌。。。
//                    module.message.error('发生错误，请重试！');
//                })
                .always(function () {

                });
        });
    }


    //  修改真实姓名
    function fillName() {
        $('.fill-name').on('tap', function () {
            var $this = $(this);
            var _name = prompt('输入您的名字：', $this.html())
            _name && _name.length < 20 ? $this.html(_name).siblings('input').val(_name) : null;
            return false
        })
    }


    //  性别选择
    function sexSelect(){
        var sexElement   = $('.sex-select');

        sexElement.on('tap', function () {
            module.layerConfirm({
                content: '选择您的性别',
                btn: ['女', '男'],
                success: function (element) {
                    $(element).find('.layui-m-layerbtn span[no]').css({color:'#40AFFE'});
                    $(element).find('.layui-m-layerbtn span[yes]').css({color:'#fe498c'});
                },
                yes: function () {
                    sexElement.html('女').siblings('input').val(0);
                },
                no: function () {
                    sexElement.html('男').siblings('input').val(1);
                }
            })

            return false
        })
    }

    //  出生日期选择
    function dateSelect() {
        var dateElement = $('.user-date');
        var _initDate = dateElement.val();

        var minDate = dateElement.attr('min') ? new Date(dateElement.attr('min')).getTime() : new Date('1900-01-01').getTime();
        var maxDate = dateElement.attr('max') ? new Date(dateElement.attr('max')).getTime() : new Date().getTime();

        $('.user-date').on('change', function () {
            var $this = $(this),
                _date = $this.val(),
                _time = new Date(_date).getTime();

            (_time >= minDate && _time <= maxDate) ? (function () {
                _initDate = _date;
            })() : (function () {
                $this.val(_initDate);
            })()
        })
    }

    //  保存修改
    function saveInfo() {
        $('#save-info-btn').on('tap', function () {
            module.loading.open();
            var options = {
                url : ctx + '/api/shop/member/save-info.do',
                dataType: "json",
                type: 'POST',
                success: function (res) {
                    module.loading.close();
                    if(res.result == 1){
                        module.message.success('修改成功！', function () {
                            history.back();
                        })
                    }
                },
                error: function () {
                    module.message.error('出现错误，请重试！', function () {
                        module.loading.close();
                    })
                }
            };
            $("#infoForm").ajaxSubmit(options);

            return false
        })
    }
})