let list = {

    init : function() {

        const _this = this;

        $(document).ready(function() {
            _this.loadList(_this);
        });

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

    },

    loadList : function(_this) {

        const page = _this.getPageFromQueryParam();
        const sort = _this.getSortFromQueryParam();
        const target = _this.getTargetFromQueryParam();
        const keyword = _this.getKeywordFromQueryParam();

        if (sort === 'top') {
            $('#recent').removeClass('text-success');
            $('#recent').addClass('text-muted');

            $('#top').removeClass('text-muted');
            $('#top').addClass('text-success');
        }

        $.ajax({
            type: 'GET',
            url: '/api/v1/posts',
            dataType: 'json',
            data: {
                page: page,
                sort: sort,
                target: target,
                keyword: keyword
            }
        })
        .done(function(data) {
            data.content.forEach(
                (content) => { $('#content-list').append(_this.makeListItem(content)); }
            );
            $('#content-list').append(
                `<div class="pt-3">
                    <ul id="pagination" class="pagination justify-content-center">
                    </ul>
                </div>`
            );
            _this.makePagination(data, target, keyword);
        })
        .fail(function(error) {
            alert(error);
            console.log(error);
        });

    },

    makeListItem : function(content) {

        return `<div id="content-list-item" class="d-flex w-100 p-2 border border-top-0">
                    <div class="d-flex flex-column align-items-center align-self-center col-1">
                        <i class="bi bi-caret-up-fill text-muted"></i>
                        <span class="text-muted">0</span>
                    </div>
                    <div class="d-flex flex-column align-self-center col-9 ps-2">
                        <div class="d-flex">
                            <a href="/community/view?id=${content.id}" class="text-decoration-none text-dark w-100">
                                <span>${content.title}</span>
                                &nbsp;
                                <span class="text-success">[${content.comments}]</span>
                            </a>
                        </div>
                        <div class="d-flex align-items-center text-muted">
                            <span style="font-size: 14px;">${content.timeDifference}</span>

                            <div class="mx-2" style="--bs-breadcrumb-divider: '|';">
                                <ol class="breadcrumb m-auto fw-lighter fs-4">
                                    <li class="breadcrumb-item"></li>
                                    <li class="breadcrumb-item"></li>
                                </ol>
                            </div>

                            <a href="javascript:void(0);" class="text-decoration-none text-muted">
                                <span style="font-size: 14px;">${content.author}</span>
                            </a>
                        </div>
                    </div>
                    <div class="col-2 text-end">
                    </div>
                </div>`;

    },

    makePagination : function(data, target, keyword) {

        const currentPage = data.number + 1;
        const pageBlock = 10;
        const startNum = parseInt((currentPage - 1) / pageBlock) * pageBlock + 1;
        const endNum = (startNum + pageBlock - 1) > data.totalPages ? data.totalPages : startNum + pageBlock - 1;

        if (target != null && keyword != null) {

            if (currentPage > pageBlock) {
                $('#pagination').append(
                    `<li class="page-item">
                        <a class="page-link" href="/community/list?target=${target}&keyword=${keyword}&page=${startNum - 1}" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>`
                );
            }

            for (var i = startNum; i <= endNum; i++) {
                if (i === currentPage) {
                    $('#pagination').append(
                        `<li class="page-item active">
                        <a class="page-link" href="/community/list?target=${target}&keyword=${keyword}&page=${i}">${i}</a>
                        </li>`
                    );
                    continue
                }
                $('#pagination').append(
                    `<li class="page-item">
                    <a class="page-link" href="/community/list?target=${target}&keyword=${keyword}&page=${i}">${i}</a>
                    </li>`
                );
            }

            if (endNum < data.totalPages) {
                $('#pagination').append(
                    `<li class="page-item">
                        <a class="page-link" href="/community/list?page=${endNum + 1}" aria-label="Next">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>`
                );
            }

        } else {

            if (currentPage > pageBlock) {
                $('#pagination').append(
                    `<li class="page-item">
                        <a class="page-link" href="/community/list?page=${startNum - 1}" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>`
                );
            }

            for (var i = startNum; i <= endNum; i++) {
                if (i === currentPage) {
                    $('#pagination').append(
                        `<li class="page-item active">
                        <a class="page-link" href="/community/list?page=${i}">${i}</a>
                        </li>`
                    );
                    continue
                }
                $('#pagination').append(
                    `<li class="page-item">
                    <a class="page-link" href="/community/list?page=${i}">${i}</a>
                    </li>`
                );
            }

            if (endNum < data.totalPages) {
                $('#pagination').append(
                    `<li class="page-item">
                        <a class="page-link" href="/community/list?page=${endNum + 1}" aria-label="Next">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>`
                );
            }

        }

    },

    getPageFromQueryParam : function() {

        const urlParams = new URLSearchParams(window.location.search);

        let intPage = parseInt(urlParams.get('page'));

        if (isNaN(intPage)) {
            return 0;
        }

        return intPage < 0 || intPage === 0 ? 0 : intPage -= 1;
    },

    getSortFromQueryParam : function() {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get('sort');
    },

    getTargetFromQueryParam : function() {

        const urlParams = new URLSearchParams(window.location.search);

        let target = urlParams.get('target');

        switch (target) {
            case 'title':
                target = 'title';
                break;
            case 'author':
                target = 'author';
                break;
            default:
                target = null;
        }

        return target;

    },

    getKeywordFromQueryParam : function() {

        const urlParams = new URLSearchParams(window.location.search);

        let keyword = urlParams.get('keyword');

        if (!keyword) {
            return keyword;
        }

        return keyword.trimLeft().trimRight();

    }

};

list.init();