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

        $('#content-list').ready(function() {
            _this.loadList();
        });

    },

    loadList : function() {

        $.ajax({});

    },

    makeListItem : function(response) {

        return `<div id="content-list-item" class="d-flex w-100 p-2 border border-top-0">
                    <div class="d-flex flex-column align-items-center align-self-center col-1">
                        <i class="bi bi-caret-up-fill text-muted"></i>
                        <span class="text-muted">1234</span>
                    </div>
                    <div class="d-flex flex-column align-self-center col-9 ps-2">
                        <div class="d-flex">
                            <a href="" class="text-decoration-none text-dark w-100">
                                <span>회사에서 빡칠 때 꿀팁</span>
                                &nbsp;
                                <span class="text-success">[78]</span>
                            </a>
                        </div>
                        <div class="d-flex align-items-center text-muted">
                            <span style="font-size: 14px;">17 시간 전</span>

                            <div class="mx-2" style="--bs-breadcrumb-divider: '|';">
                                <ol class="breadcrumb m-auto fw-lighter fs-4">
                                    <li class="breadcrumb-item"></li>
                                    <li class="breadcrumb-item"></li>
                                </ol>
                            </div>

                            <a href="" class="text-decoration-none text-muted">
                                <span style="font-size: 14px;">태어날때도란링</span>
                            </a>
                        </div>
                    </div>
                    <div class="col-2 text-end">
                        <a href="">
                            <img th:src="@{/img/image_not_found.png}" alt="thumbnail" class="thumbnail" style="width: 62px; height: 62px;">
                        </a>
                    </div>
                </div>`

    }

};

list.init();