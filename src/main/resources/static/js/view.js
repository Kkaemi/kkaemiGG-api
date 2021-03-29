let view = {

    init : function() {

        const _this = this;

        let oldVal;

        $(document).ready(function() {
            _this.loadContent();
        });

        $('#writeComment').on('propertychange change keyup paste input', function() {

            let currentVal = $(this).val();
            if (currentVal === oldVal) { return; }
            oldVal = currentVal;

            if (currentVal.length > 1000) {
                $(this).val(currentVal.substring(0, 1000));
                $(this).css('height', 'auto');
                $(this).height(this.scrollHeight);
                $('#count').html($(this).val().length);
                return;
            }

            $(this).css('height', 'auto');
            $(this).height(this.scrollHeight);
            $('#count').html(currentVal.length);

        });

        $('#commentSubmitButton').on('click', function() {
            _this.submitComment();
        });

        $('#commentRefreshButton').on('click', function() {

        });

    },

    loadContent : function() {

        const id = new URLSearchParams(window.location.search).get('id');

        if (parseInt(id) > 0x7fffffffffffffff || parseInt(id) < 0 || isNaN(id)) {
            alert('올바른 값을 입력해 주세요');
            window.location.replace('/community/list');
            return;
        }

        $.ajax({
            type: 'GET',
            url: `/api/v1/posts/${id}`,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
        }).done(function(data) {

            if (data.postsId === -1) {
                alert('존재하지 않는 게시물입니다');
                window.location.replace('/community/list');
                return;
            }

            document.title = data.title;
            $('#title').text(data.title);
            $('#timeDifference').text(data.timeDifference);
            $('#author').text(data.author);
            $('#hit').text(`조회 ${new Intl.NumberFormat().format(data.hit)}`);
            $('#content').html(data.content);

            if (data.owner) {
                $('#contentHead').append(
                `<div class="mt-3 mb-1">
                  <button class="btn btn-outline-danger btn-sm px-3">삭제</button>
                  <button class="btn btn-outline-info btn-sm px-3 ms-2">수정</button>
                </div>`
                );
            }

        }).fail(function(error) {
            alert(error);
        });
    },

    submitComment : function() {

        if (!$('#writeComment').val().trimLeft().trimRight()) {
            alert('내용을 입력해 주세요!!!');
            $('#writeComment').focus();
            return;
        }

        const postsId = new URLSearchParams(window.location.search).get('id');
        const parentCommentId = null;
        const content = $('#writeComment').val();

        $.ajax({
            type: 'POST',
            url: '/api/v1/comments',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify({
                postsId: postsId,
                parentCommentId: parentCommentId,
                content: content
            })
        }).done(function(data) {

        if (data === 0) {
            alert('로그인이 필요합니다!!!');
            return;
        }

        alert('댓글이 저장되었습니다.');

        }).fail(function(error) {
            alert(error);
            console.log(error);
        });

    },

    loadComment : function() {

        const postsId = new URLSearchParams(window.location.search).get('id');

        $.ajax({
            type: 'GET',
            url: `/api/v1/comments/${postsId}`,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
        }).done(function(data) {
            
        }).fail(function(error) {
            alert(error);
            console.log(error);
        });

    }

};

view.init();