$( document ).ready(function() {

    // Toggle Search
    $('.show-search').click(function(){
        $('.search-form').css('margin-top', '0');
    });
    
    $('.close-search').click(function(){
        $('.search-form').css('margin-top', '-60px');
    });
    
    
    // Fullscreen
    function toggleFullScreen() {
        if ((document.fullScreenElement && document.fullScreenElement !== null) ||  
            (!document.mozFullScreen && !document.webkitIsFullScreen)) {
            if (document.documentElement.requestFullScreen) {  
                document.documentElement.requestFullScreen();  
            } else if (document.documentElement.mozRequestFullScreen) {  
                document.documentElement.mozRequestFullScreen(); 
            } else if (document.documentElement.webkitRequestFullScreen) {
                document.documentElement.webkitRequestFullScreen(Element.ALLOW_KEYBOARD_INPUT);  
            }  
        } else {  
            if (document.cancelFullScreen) {  
                document.cancelFullScreen();  
            } else if (document.mozCancelFullScreen) {  
                document.mozCancelFullScreen();  
            } else if (document.webkitCancelFullScreen) {  
                document.webkitCancelFullScreen();  
            }  
        }  
    }
    
    $('.toggle-fullscreen').click(function(){
        toggleFullScreen();
    });
    
    
    // Waves
    Waves.displayEffect();
    
    // tooltips
    $( '[data-toggle~="tooltip"]' ).tooltip({
        container: 'body'
    });
    
    // Switchery
    var elems = Array.prototype.slice.call(document.querySelectorAll('.js-switch'));
    
    elems.forEach(function(html) {
        var switchery = new Switchery(html, { color: '#23B7E5' });
    });
    
    // Element Blocking
    function blockUI(item) {    
        $(item).block({
            message: '<img src="/template/assets/images/reload.gif" width="20px" alt="">',
            css: {
                border: 'none',
                padding: '0px',
                width: '20px',
                height: '20px',
                backgroundColor: 'transparent'
            },
            overlayCSS: {
                backgroundColor: '#fff',
                opacity: 0.9,
                cursor: 'wait'
            }
        });
    }
    
    function unblockUI(item) {
        $(item).unblock();
    }  
    
    // Panel Control
    $('.panel-collapse').click(function(){
        $(this).closest(".panel").children('.panel-body').slideToggle('fast');
    });
    
    $('.panel-reload').click(function() { 
        var el = $(this).closest(".panel").children('.panel-body');
        blockUI(el);
        window.setTimeout(function () {
            unblockUI(el);
        }, 1000);
    
    }); 
    
    $('.panel-remove').click(function(){
        $(this).closest(".panel").hide();
    });
    
    // Push Menu
    $('.push-sidebar').click(function(){
        var hidden = $('.sidebar');
        
        if (hidden.hasClass('visible')){
            hidden.removeClass('visible');
            $('.page-inner').removeClass('sidebar-visible');
        } else {
            hidden.addClass('visible');
            $('.page-inner').addClass('sidebar-visible');
        }
    });
    
    // sortable
    $(".sortable").sortable({
        connectWith: '.sortable',
        items: '.panel',
        helper: 'original',
        revert: true,
        placeholder: 'panel-placeholder',
        forcePlaceholderSize: true,
        opacity: 0.95,
        cursor: 'move'
    });
    
    // Uniform
    var checkBox = $("input[type=checkbox]:not(.switchery), input[type=radio]:not(.no-uniform)");
    if (checkBox.size() > 0) {
        checkBox.each(function() {
            $(this).uniform();
        });
    };
    
    // .toggleAttr() Function
    $.fn.toggleAttr = function(a, b) {
        var c = (b === undefined);
        return this.each(function() {
            if((c && !$(this).is("["+a+"]")) || (!c && b)) $(this).attr(a,a);
            else $(this).removeAttr(a);
        });
    };
    
    // Sidebar Menu
    var parent, ink, d, x, y;
    $('.sidebar .accordion-menu li .sub-menu').slideUp(0);
    $('.sidebar .accordion-menu li.open .sub-menu').slideDown(0);
    $('.small-sidebar .sidebar .accordion-menu li.open .sub-menu').hide(0);
    $('.sidebar .accordion-menu > li.droplink > a').click(function(){
        
        if($('body').hasClass('.small-sidebar')) {
            return;
        };
        
        if($('body').hasClass('.page-horizontal-bar')) {
            return;
        };
        
        if($('body').hasClass('.hover-menu')) {
            return;
        };
        
        var menu = $('.sidebar .menu'),
            sidebar = $('.page-sidebar-inner'),
            page = $('.page-content'),
            sub = $(this).next(),
            el = $(this);
        
        menu.find('li').removeClass('open');
        $('.sub-menu').slideUp(200, function() {
            sidebarAndContentHeight();
        });
        sidebarAndContentHeight();
        
        if (!sub.is(':visible')) {
            $(this).parent('li').addClass('open');
            $(this).next('.sub-menu').slideDown(200, function() {
                sidebarAndContentHeight();
            });
        } else {
            sub.slideUp(200, function() {
                sidebarAndContentHeight();
            });
        }
        return false;
    });
    
    $('.sidebar .accordion-menu .sub-menu li.droplink > a').click(function(){
        
        var menu = $(this).parent().parent(),
            sidebar = $('.page-sidebar-inner'),
            page = $('.page-content'),
            sub = $(this).next(),
            el = $(this);
        
        menu.find('li').removeClass('open');
        sidebarAndContentHeight();
        
        if (!sub.is(':visible')) {
            $(this).parent('li').addClass('open');
            $(this).next('.sub-menu').slideDown(200, function() {
                sidebarAndContentHeight();
            });
        } else {
            sub.slideUp(200, function() {
                sidebarAndContentHeight();
            });
        }
        return false;
    });
    
    // Makes .page-inner height same as .page-sidebar height
    var sidebarAndContentHeight = function () {
        var content = $('.page-inner'),
            sidebar = $('.page-sidebar'),
            body = $('body'),
            height,
            footerHeight = $('.page-footer').outerHeight(),
            pageContentHeight = $('.page-content').height();
        
        content.attr('style', 'min-height:' + sidebar.height() + 'px !important');
        
        if (body.hasClass('page-sidebar-fixed')) {
            height = sidebar.height() + footerHeight;
        } else {
            height = sidebar.height() + footerHeight;
            if (height  < $(window).height()) {
                height = $(window).height();
            }
        }
        
        if (height >= content.height()) {
            content.attr('style', 'min-height:' + height + 'px !important');
        }
    };
    
    sidebarAndContentHeight();
    
    window.onresize = sidebarAndContentHeight;
    
    
    // Slimscroll
    $('.slimscroll').slimscroll({
        allowPageScroll: true
    });
    
    // Layout Settings
    var fixedHeaderCheck = document.querySelector('.fixed-header-check'),
        fixedSidebarCheck = document.querySelector('.fixed-sidebar-check'),
        horizontalBarCheck = document.querySelector('.horizontal-bar-check'),
        toggleSidebarCheck = document.querySelector('.toggle-sidebar-check'),
        boxedLayoutCheck = document.querySelector('.boxed-layout-check'),
        compactMenuCheck = document.querySelector('.compact-menu-check'),
        hoverMenuCheck = document.querySelector('.hover-menu-check'),
        defaultOptions = function() {
            
            if(($('body').hasClass('small-sidebar'))&&(toggleSidebarCheck.checked == 1)){
                toggleSidebarCheck.click();
            }
        
            if(!($('body').hasClass('page-header-fixed'))&&(fixedHeaderCheck.checked == 0)){
                fixedHeaderCheck.click();
            }
        
            if(($('body').hasClass('page-sidebar-fixed'))&&(fixedSidebarCheck.checked == 1)){
                fixedSidebarCheck.click();
            }
        
            if(($('body').hasClass('page-horizontal-bar'))&&(horizontalBarCheck.checked == 1)){
                horizontalBarCheck.click();
            }
        
            if(($('body').hasClass('compact-menu'))&&(compactMenuCheck.checked == 1)){
                compactMenuCheck.click();
            }
        
            if(($('body').hasClass('hover-menu'))&&(hoverMenuCheck.checked == 1)){
                hoverMenuCheck.click();
            }
        
            if(($('.page-content').hasClass('container'))&&(boxedLayoutCheck.checked == 1)){
                boxedLayoutCheck.click();
            }
        
            $(".theme-color").attr("href", '/adm/assets/css/themes/white.css');
           
            sidebarAndContentHeight();
        },
        str = $('.navbar .logo-box a span').text(),
        smTxt = (str.slice(0,1)),
        collapseSidebar = function() {
            $('body').toggleClass("small-sidebar");
            $('.navbar .logo-box a span').html($('.navbar .logo-box a span').text() == smTxt ? str : smTxt);

            if ($('body').hasClass('small-sidebar')) {
                postRequest('t_sidebar', true);
            } else {
                postRequest('t_sidebar', false);
            }

            sidebarAndContentHeight();
        },
        fixedHeader = function() {
            if(($('body').hasClass('page-horizontal-bar'))&&($('body').hasClass('page-sidebar-fixed'))&&($('body').hasClass('page-header-fixed'))){
                fixedSidebarCheck.click();
                alert("Static header isn't compatible with fixed horizontal nav mode. Modern will set static mode on horizontal nav.");
            };
            $('body').toggleClass('page-header-fixed');

            if ($('body').hasClass('page-header-fixed')) {
                postRequest('f_header', true);
            } else {
                postRequest('f_header', false);
            }

            sidebarAndContentHeight();
        },
        fixedSidebar = function() {

            if(($('body').hasClass('page-horizontal-bar'))&&(!$('body').hasClass('page-sidebar-fixed'))&&(!$('body').hasClass('page-header-fixed'))){
                fixedHeaderCheck.click();
                alert("Fixed horizontal nav isn't compatible with static header mode. Modern will set fixed mode on header.");
            };
            if(($('body').hasClass('hover-menu'))&&(!$('body').hasClass('page-sidebar-fixed'))){
                hoverMenuCheck.click();
                alert("Fixed sidebar isn't compatible with hover menu mode. Modern will set accordion mode on menu.");
            };

            $('body').toggleClass('page-sidebar-fixed');

            if ($('body').hasClass('page-sidebar-fixed')) {
                postRequest('f_sidebar', true);
                $('.page-sidebar-inner').slimScroll({
                    destroy:true
                });
            } else {
                postRequest('f_sidebar', false);
            }
            $('.page-sidebar-inner').slimScroll();
            sidebarAndContentHeight();
        },
        horizontalBar = function() {
            $('.sidebar').toggleClass('horizontal-bar');
            $('.sidebar').toggleClass('page-sidebar');
            $('body').toggleClass('page-horizontal-bar');
            if(($('body').hasClass('page-sidebar-fixed'))&&(!$('body').hasClass('page-header-fixed'))){
                fixedHeaderCheck.click();
                alert("Static header isn't compatible with fixed horizontal nav mode. Modern will set static mode on horizontal nav.");
            };

            if ($('.sidebar').hasClass('horizontal-bar')) {
                postRequest('h_bar', true);
            } else {
                postRequest('h_bar', false);
            }

            sidebarAndContentHeight();
        },
        boxedLayout = function() {
            $('.page-content').toggleClass('container');

            if ($('.page-content').hasClass('container')) {
                postRequest('b_layout', true);
            } else {
                postRequest('b_layout', false);
            }

            sidebarAndContentHeight();
        },
        compactMenu = function() {
            $('body').toggleClass('compact-menu');

            if ($('body').hasClass('compact-menu')) {
                postRequest('c_menu', true);
            } else {
                postRequest('c_menu', false);
            }

            sidebarAndContentHeight();
        },
        hoverMenu = function() {
            if((!$('body').hasClass('hover-menu'))&&($('body').hasClass('page-sidebar-fixed'))){
                fixedSidebarCheck.click();
                alert("Fixed sidebar isn't compatible with hover menu mode. Modern will set static mode on sidebar.");
            };
            $('body').toggleClass('hover-menu');

            if ($('body').hasClass('hover-menu')) {
                postRequest('h_menu', true);
            } else {
                postRequest('h_menu', false);
            }

            sidebarAndContentHeight();
        };
    
    
    // Logo text on Collapsed Sidebar
    $('.small-sidebar .navbar .logo-box a span').html($('.navbar .logo-box a span').text() == smTxt ? str : smTxt);
    
    
    if( !$('.theme-settings').length ) {
        $('.sidebar-toggle').click(function() {
            collapseSidebar();
        });
    };
    
    if( $('.theme-settings').length ) {
    fixedHeaderCheck.onchange = function() {
        fixedHeader();
    };
    
    fixedSidebarCheck.onchange = function() {
        fixedSidebar();
    };
    
    function postRequest(argument, value) {
            $.post('/ajax/changeStyle', {
                type: argument,
                value: value
            }, function(data, status) {
                if (!navigator.onLine) {
                    alert("Check your internet connection...");
                    return;
                }
                if (status == 'success') {
                    console.log(data);
                    if (data.status == 'ok') {
                        console.log("Changed");
                    } else {
                        alert("Error, couldn't change...");
                    }
                } else {
                    alert("Error, I can't connect with the server...");
                }
            });
    }
    
    horizontalBarCheck.onchange = function() {
        horizontalBar();
    };
    
    toggleSidebarCheck.onchange = function() {
        collapseSidebar();
    };
        
    compactMenuCheck.onchange = function() {
        compactMenu();
    };
        
    hoverMenuCheck.onchange = function() {
        hoverMenu();
    };
        
    boxedLayoutCheck.onchange = function() {
        boxedLayout();
    };
    
    
    // Sidebar Toggle
    $('.sidebar-toggle').click(function() {
        toggleSidebarCheck.click();
    });
    
    // Reset options
    $('.reset-options').click(function() {
        defaultOptions();
        postRequest('reset', true);
    });
    
    // Color changer
    $(".colorbox").click(function(){
        var color =  $(this).attr('data-css');
        $(".theme-color").attr('href', '/adm/assets/css/themes/' + color + '.css');
        postRequest('custom_style', color);
        return false;
    });
    
    // Fixed Sidebar Bug
    if(!($('body').hasClass('page-sidebar-fixed'))&&(fixedSidebarCheck.checked == 1)){
        $('body').addClass('page-sidebar-fixed');
    }
    
    if(($('body').hasClass('page-sidebar-fixed'))&&(fixedSidebarCheck.checked == 0)){
        $('.fixed-sidebar-check').prop('checked', true);
    }
    
    // Fixed Header Bug
    if(!($('body').hasClass('page-header-fixed'))&&(fixedHeaderCheck.checked == 1)){
        $('body').addClass('page-header-fixed');
    }
    
    if(($('body').hasClass('page-header-fixed'))&&(fixedHeaderCheck.checked == 0)){
        $('.fixed-header-check').prop('checked', true);
    }
    
    // horizontal bar Bug
    if(!($('body').hasClass('page-horizontal-bar'))&&(horizontalBarCheck.checked == 1)){
        $('body').addClass('page-horizontal-bar');
        $('.sidebar').addClass('horizontal-bar');
        $('.sidebar').removeClass('page-sidebar');
    }
    
    if(($('body').hasClass('page-horizontal-bar'))&&(horizontalBarCheck.checked == 0)){
        $('.horizontal-bar-check').prop('checked', true);
    }
    
    // Toggle Sidebar Bug
    if(!($('body').hasClass('small-sidebar'))&&(toggleSidebarCheck.checked == 1)){
        $('body').addClass('small-sidebar');
    }
    
    if(($('body').hasClass('small-sidebar'))&&(toggleSidebarCheck.checked == 0)){
        $('.horizontal-bar-check').prop('checked', true);
    }
    
    // Boxed Layout Bug
    if(!($('.page-content').hasClass('container'))&&(boxedLayoutCheck.checked == 1)){
        $('.toggle-sidebar-check').addClass('container');
    }
    
    if(($('.page-content').hasClass('container'))&&(boxedLayoutCheck.checked == 0)){
        $('.boxed-layout-check').prop('checked', true);
    }
        
    // Boxed Layout Bug
    if(!($('.page-content').hasClass('container'))&&(boxedLayoutCheck.checked == 1)){
        $('.toggle-sidebar-check').addClass('container');
    }
    
    if(($('.page-content').hasClass('container'))&&(boxedLayoutCheck.checked == 0)){
        $('.boxed-layout-check').prop('checked', true);
    }
        
    // Boxed Layout Bug
    if(!($('.page-content').hasClass('container'))&&(boxedLayoutCheck.checked == 1)){
        $('.toggle-sidebar-check').addClass('container');
    }
    
    if(($('.page-content').hasClass('container'))&&(boxedLayoutCheck.checked == 0)){
        $('.boxed-layout-check').prop('checked', true);
    }
    }
    
    
    // Chat Sidebar
    var menuRight = document.getElementById( 'cbp-spmenu-s1' ),
        showRight = document.getElementById( 'showRight' ),
        closeRight = document.getElementById( 'closeRight' ),
        menuRight2 = document.getElementById( 'cbp-spmenu-s2' ),
        closeRight2 = document.getElementById( 'closeRight2' ),
        body = document.body;
    
    if (showRight != null) showRight.onclick = function() {
        classie.toggle( menuRight, 'cbp-spmenu-open' );
    };
    
    if (closeRight != null) closeRight.onclick = function() {
        classie.toggle( menuRight, 'cbp-spmenu-open' );
    };
    
    if (closeRight2 != null) closeRight2.onclick = function() {
        classie.toggle( menuRight2, 'cbp-spmenu-open' );
    };
    
    $('.showRight2').click(function() {
        classie.toggle( menuRight2, 'cbp-spmenu-open' );
    });
    
    $(".chat-write form input").keypress(function (e) {
        if ((e.which == 13)&&(!$(this).val().length == 0)) {
            $('<div class="chat-item chat-item-right"><div class="chat-message">' + $(this).val() + '</div></div>').insertAfter(".chat .chat-item:last-child");
            $(this).val('');
        } else if(e.which == 13) {
            return;
        }
        $('.chat').slimscroll({
            allowPageScroll: true
        });
    });

    $('#lockScreen').on('click', function(){
        $.post('/ajax/lock', {
            action: 'lock'
        }, function(data, status) {
            if (!navigator.onLine) {
                document.getElementById("textmodal").innerHTML = "Check your internet connection...";
                document.getElementById("modal_").click();
                return;
            }
            if (status == 'success') {
                if (data.status == 'ok') {
                    location.reload();
                }
            } else {
                document.getElementById("textmodal").innerHTML = "Error, I can't connect with the server...";
                document.getElementById("modal_").click();
            }
        });
    });
    function getCookie(name) {
        var matches = document.cookie.match(new RegExp("(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"));
        return matches ? decodeURIComponent(matches[1]) : undefined;
    }
    function setCookie(name, value, options) {
        options = options || {};

        var expires = options.expires;

        if (typeof expires == "number" && expires) {
            var d = new Date();
            d.setTime(d.getTime() + expires * 1000);
            expires = options.expires = d;
        }
        if (expires && expires.toUTCString) {
            options.expires = expires.toUTCString();
        }

        value = encodeURIComponent(value);

        var updatedCookie = name + "=" + value;

        for (var propName in options) {
            updatedCookie += "; " + propName;
            var propValue = options[propName];
            if (propValue !== true) {
                updatedCookie += "=" + propValue;
            }
        }
        document.cookie = updatedCookie;
    }

    // to prevent zoom in browser & mobiles

    
    $(document).keydown(function(event) {
    if (event.ctrlKey==true && (event.which == '61' || event.which == '107' || event.which == '173' || event.which == '109'  || event.which == '187'  || event.which == '189'  ) ) {
        // alert('disabling zooming'); 
        event.preventDefault();
        // 107 Num Key  +
        //109 Num Key  -
        //173 Min Key  hyphen/underscor Hey
        // 61 Plus key  +/=
         }
    });
    
    $(window).bind('mousewheel DOMMouseScroll', function (event) {
        if (event.ctrlKey == true) {
            //alert('disabling zooming'); 
            event.preventDefault();
        }
    });
});