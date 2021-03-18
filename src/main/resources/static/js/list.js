let list = {

    init : function() {

        const _this = this;

        $('#searchModal').on('shown.bs.modal', function() {
            $('#modalSearchKeyword').focus();
        });

        $(window).scroll(function() {

            const height = $(document).scrollTop(); //실시간으로 스크롤의 높이를 측정

            if (height > 0) {
              $('#mobileSubHeader').addClass('border-bottom shadow-sm');
            } else {
              $('#mobileSubHeader').removeClass('border-bottom shadow-sm');
            }

        });

    }

};

list.init();