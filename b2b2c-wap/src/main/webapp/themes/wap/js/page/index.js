/**
 * Created by Andste on 2016/12/5.
 */
$(function () {
    var module = new Module();
    var indexScroll;
    var searchNav = $('#index-search-nav');

    init();

    function init() {
        module.scrollToTopControl.init({
            rollListen: false,
            clickToTop: function () {
                indexScroll.isAnimating = false;
                setTimeout(function () {
                    indexScroll.scrollTo(0, 0, 300, IScroll.utils.ease.quadratic)
                }, 20)
            }
        });

        bindEvents();
    }

    function bindEvents() {

        //  初始化iscroll
        initIScroll();
        indexScroll.on('scroll', updateHeader);
        indexScroll.on('scrollEnd', updateHeader);

        //  初始化Swiper【广告横幅�?        initSwiper();

        //  商城公告事件绑定
        storeEvents();

        //  新品上市事件绑定
        newListingEvents();

        //  特别推荐
        sellingGoodsEvents();

        //  搜索栏事件绑�?        searchEvents();
    }

    function initIScroll() {
        indexScroll = new IScroll('#index', {
            probeType   : 3,
            mouseWheel  : false,
            disableTouch: false,
            tap         : true
        });
        document.addEventListener('touchmove', function (e) { e.preventDefault() }, false);
    }

    //  更新header透明
    function updateHeader () {
        var _top = -(this.y>>0);
        _top > 150 ? module.scrollToTopControl.show() : module.scrollToTopControl.hide();
        _top < 0 ? searchNav.addClass('hide') : searchNav.removeClass('hide');
        if(_top < 0){_top = 0}
        if(_top > 80){_top = 80}
        $('#index-search-nav').css({backgroundColor: 'rgba(201, 21, 35, '+ _top/100 +')'})
    }

    //  初始化广告横
    function initSwiper() {
        var advSwiper = new Swiper('.swiper-container', {
            loop: true,
            autoplay: 3000,
            autoplayDisableOnInteraction : false,
            pagination : '.swiper-pagination',
            preventLinksPropagation : false,
            onTap: function (swiper) {
                console.log(swiper)
                var clickedSlide = swiper.clickedSlide.attributes['data-href']['nodeValue']
                if(clickedSlide !== undefined){location.href = clickedSlide}
            }
        })
    }

    //  商城公告事件绑定
    function storeEvents() {
        var bulletinBoard = $('.index-bulletin-board');
        var items         = bulletinBoard.find('.item'),
            itemsLen      = items.length;
        $('.more-bulletin-board').on('tap', function () {
            module.message.error('抱歉，没有更多了。');
            return false
        })
        if (itemsLen < 1) {
            return
        }
        var bulletinSwiper = new Swiper('.index-bulletin-board', {
            loop                        : true,
            autoplay                    : 3000,
            autoplayDisableOnInteraction: false,
            direction                   : 'vertical',
            preventLinksPropagation     : false,
            onTap                       : function (swiper) {
                var clickedSlide  = swiper.clickedSlide;
                var slideHrefAttr = clickedSlide.attributes['data-href'];
                var _href         = slideHrefAttr !== undefined ? clickedSlide['nodeValue'] : null;
                //  if(_href){location.href = _href}
                var dataTitle     = clickedSlide.attributes['data-title']['nodeValue'];
                module.message.error(dataTitle);
                return false
            }
        })
    }

    //  新品上市事件绑定
    function newListingEvents() {
        var newListing = $('.list-new-listing'),
            items    = newListing.find('.item'),
            itemsLen = items.length;
        $('.content-new-listing').on('tap', '.item', function () {
            var $this = $(this),
                _href = $this.attr('data-href');
            if(_href){location.href = _href};
            return false
        })
        if(itemsLen < 4){return}
        newListing.css({width: itemsLen * 0.3 * 100 + '%'})
        //  初始化iscroll
        var newListingScroll = new IScroll("#content-new-listing", {
            scrollX: true,
            scrollY: false,
            probeType   : 1,
            mouseWheel  : false,
            disableTouch: false,
            tap         : true
        });
    }

    //  热销商品事件绑定
    function sellingGoodsEvents() {
        var sellingGoods = $('.content-selling-goods'),
            items    = sellingGoods.find('.item'),
            itemsLen = items.length;
        if(itemsLen < 1){return}
        var specialSwiper = new Swiper('.content-selling-goods', {
            loop: true,
            autoplay: 3000,
            autoplayDisableOnInteraction : false,
            effect : 'coverflow',
            slidesPerView: 3,
            centeredSlides: true,
            coverflow: {
                rotate      : 30,
                stretch     : 5,
                depth       : 100,
                modifier    : 2,
                slideShadows: true
            },
            onTap: function (swiper) {
                var clickedSlide = swiper.clickedSlide.attributes['data-href'];
                var _href = clickedSlide !== undefined ? clickedSlide['nodeValue'] : null;
                if(_href){location.href = _href}
            }
        })
    }

    //  搜索栏事件绑
    function searchEvents() {
        $('#search-nav').on('tap', function () {
            module.searchControl.init();
            return false
        })
    }

    //  整个文档加载完成后再次刷新iscroll
    window.onload = function () {
        indexScroll.refresh()
    }
    
    function seachAndAdd(){
		var content = $("#search-input").val();
		if(content != undefined && content != ""){
			$.ajax({
	            url : ctx + '/api/shop/searchRecord/add.do',
	            data: {
	            	content: content
	            },
	            async: false,
	            type: 'POST',
	            success: function (res) {
	            },
	            error: function () {
	            }
	        })
			$("#search-form").submit();
		}
	}
})
function seachAndAdd(){
		var content = $("#search-input").val();
		if(content != undefined && content != ""){
			$.ajax({
	            url : ctx + '/api/shop/searchRecord/add.do',
	            data: {
	            	content: content
	            },
	            async: false,
	            type: 'POST',
	            success: function (res) {
	            },
	            error: function () {
	            }
	        })
			$("#search-form").submit();
		}
	}
