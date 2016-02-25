var utils = {};
(function(){
    utils = {
        isEmpty:function(val) {
            return typeof val !== 'string' || val === '';
        },
        getHashValue:function(){
            var hash = window.location.hash;
            if(!this.isEmpty(hash)) {
                return hash.substr(1);
            }
            return '';
        }
    };
    $.fn.extend({
        animateCss: function (animationName, onComplete) {
            var animationEnd = 'webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend';
            $(this).addClass('animated ' + animationName).one(animationEnd, function() {
                if(typeof onComplete === 'function') {
                    onComplete.call(this);
                }
                $(this).removeClass('animated ' + animationName);
            });
        }
    });
})();