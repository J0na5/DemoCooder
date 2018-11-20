// Auto close navbar-collapse on click a
$('a.nav-link').on('click', function () {
    $('.navbar-toggler:visible').click();
});

var $ = $.noConflict();
$(function () {
    "use strict";

    if ($(".navbar").width() > 1007)
    {
        $('.navbar .dropdown').hover(function () {
            $(this).addClass('open');
        }, function () {
            $(this).removeClass('open');
        });
    }

    // Preloader
    $(window).preloader({
        delay: 500
    });

    // Language bar
    $(".language-nav a,.close-language").on("click", function () {
        $(".top-language").hide();
    });
    $(".language-nav a").on("click", function () {
        $(".top-language").show();
    });

    // Back to top
    // Check to see if the window is top if not then display button
    $(window).on("scroll", function () {
        if ($(this).scrollTop() > 300) {
            $('.scrollToTop').fadeIn();
        } else {
            $('.scrollToTop').fadeOut();
        }
    });

    // Click event to scroll to top
    $('.scrollToTop').on("click", function () {
        $('html, body').animate({scrollTop: 0}, 800);
        return false;
    });

    // Animated scroll menu
    $(window).on("scroll", function () {
        var scroll = $(window).scrollTop();
        if (scroll > 0) {
            $('.navbar-transparent').addClass('shrink');
        }
        if (scroll <= 0) {
            $('.navbar-transparent').removeClass('shrink');
        }
    });

    // Video popup
    $('.video-popup').magnificPopup({
        type: 'iframe'
    });

    // Maginific popup
    $('.popup-container').each(function () {
        $(this).magnificPopup({
            delegate: 'a',
            type: 'image',
            mainClass: 'mfp-with-zoom',
            gallery: {
                enabled: true
            },
            zoom: {
                enabled: true,
                duration: 300,
                easing: 'ease-in-out',
                opener: function (openerElement) {
                    return openerElement.is('img') ? openerElement : openerElement.find('img');
                }
            }
        });
    });

    // Smooth scroll
    $(function () {
        $('.scroll-to a[href*="#"]:not([href="#"])').on("click",function () {
            if (location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '') && location.hostname == this.hostname) {
                var target = $(this.hash);
                target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');
                if (target.length) {
                    $('html, body').animate({
                        scrollTop: target.offset().top
                    }, 500);
                    return false;
                }
            }
        });
    });
});

// Statistics
$.fn.countTo = function (options) {
    options = options || {};

    return $(this).each(function () {
        // set options for current element
        var settings = $.extend({}, $.fn.countTo.defaults, {
            from:            $(this).data('from'),
            to:              $(this).data('to'),
            speed:           $(this).data('speed'),
            refreshInterval: $(this).data('refresh-interval'),
            decimals:        $(this).data('decimals')
        }, options);

        // how many times to update the value, and how much to increment the value on each update
        var loops = Math.ceil(settings.speed / settings.refreshInterval),
            increment = (settings.to - settings.from) / loops;

        // references & variables that will change with each update
        var self = this,
            $self = $(this),
            loopCount = 0,
            value = settings.from,
            data = $self.data('countTo') || {};

        $self.data('countTo', data);

        // if an existing interval can be found, clear it first
        if (data.interval) {
            clearInterval(data.interval);
        }
        data.interval = setInterval(updateTimer, settings.refreshInterval);

        // initialize the element with the starting value
        render(value);

        function updateTimer() {
            value += increment;
            loopCount++;

            render(value);

            if (typeof(settings.onUpdate) == 'function') {
                settings.onUpdate.call(self, value);
            }

            if (loopCount >= loops) {
                // remove the interval
                $self.removeData('countTo');
                clearInterval(data.interval);
                value = settings.to;

                if (typeof(settings.onComplete) == 'function') {
                    settings.onComplete.call(self, value);
                }
            }
        }

        function render(value) {
            var formattedValue = settings.formatter.call(self, value, settings);
            $self.html(formattedValue);
        }
    });
};

$.fn.countTo.defaults = {
    from: 0,               // the number the element should start at
    to: 0,                 // the number the element should end at
    speed: 2000,           // how long it should take to count between the target numbers
    refreshInterval: 10,  // how often the element should be updated
    decimals: 0,           // the number of decimal places to show
    formatter: formatter,  // handler for formatting the value before rendering
    onUpdate: null,        // callback method for every time the element is updated
    onComplete: null       // callback method for when the element finishes updating
};

function formatter(value, settings) {
    return value.toFixed(settings.decimals);
}

$('.count-number').data('countToOptions', {
    formatter: function (value, options) {
        return value.toFixed(options.decimals).replace(/\B(?=(?:\d{3})+(?!\d))/g, ',');
    }
});

var counted = false;
$(window).scroll(function() {
    var hT = $('.counter').offset().top,
        hH = $('.counter').outerHeight(),
        wH = $(window).height(),
        wS = $(this).scrollTop();
    if(!counted && wS > (hT + hH - wH)) {
        // start all the timers
        $('.timer').each(count);
        counted = true;
    }
});

function count(options) {
    var $this = $(this);
    options = $.extend({}, options || {}, $this.data('countToOptions') || {});
    $this.countTo(options);
}