let view = {

    init : function() {

        const _this = this;

        let oldVal;

        $(document).ready(function() {
            _this.loadContent();
            _this.loadComments();
        });

        $(document).on('propertychange change keyup paste input', '#writeComment, #writeReply', function(event) {

            let count = `#count`;
            if (event.target.id === 'writeReply') {
                count = `#replyCount`;
            }

            let currentVal = $(this).val();
            if (currentVal === oldVal) { return; }
            oldVal = currentVal;

            if (currentVal.length > 1000) {
                $(this).val(currentVal.substring(0, 1000));
                $(this).css('height', 'auto');
                $(this).height(this.scrollHeight);
                $(count).html($(this).val().length);
                return;
            }

            $(this).css('height', 'auto');
            $(this).height(this.scrollHeight);
            $(count).html(currentVal.length);

        });

        $(document).on('click', '#commentSubmitButton, #replySubmitButton', function(event) {

            $(this).html(`<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>`);

            const textarea = event.target.id === 'commentSubmitButton' ? '#writeComment' : '#writeReply';

            if (!$(textarea).val().trimLeft().trimRight()) {
                alert('내용을 입력해 주세요!!!');
                $(this).html(`작성`);
                $(textarea).focus();
                return;
            }

            let commentId = $(`#${event.target.id}`).parent().parent().parent().parent().parent().prev().attr('id');
            let targetNickname = $(`#${event.target.id}`).parent().parent().parent().parent().parent().prev().find('span.fw-bold').text();
            let content = $(textarea).val();

            if (isNaN(commentId)) {
                commentId = null;
            }

            _this.submitComment(commentId, targetNickname, content, _this);

        });

        $('#commentRefreshButton').on('click', function() {
            $('#comment').children().remove();
            _this.loadComments();
        });

        $(document).on('click', '.reply-btn', function() {
            const id = $(this).parent().parent().parent().attr('id');
            _this.clickWriteReplyButton(id);
        });

        $(document).on('click', '.comment-remove-btn', function() {
            if (window.confirm('댓글을 삭제하시겠습니까?')) {
                const id = $(this).parent().parent().parent().attr('id');
                _this.removeComment(_this, id);
            }
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
            alert(error.statusText);
            console.log(error);
        });
    },

    submitComment : function(commentId, targetNickname, content, init) {

        const postsId = new URLSearchParams(window.location.search).get('id');

        $.ajax({
            type: 'POST',
            url: '/api/v1/comments',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify({
                postsId: postsId,
                parentCommentId: commentId,
                targetNickname: targetNickname,
                content: content
            })
        }).done(function(data) {

        if (data === 0) {
            alert('로그인이 필요합니다!!!');
            window.location.href = '/user/login';
            return;
        }

        $('#comment').children().remove();
        init.loadComments();
        $('#commentSubmitButton, #replySubmitButton').html('작성');
        $('#writeComment').val('');

        }).fail(function(error) {
            alert(error.statusText);
            console.log(error);
        });

    },

    loadComments : function() {

        const postsId = new URLSearchParams(window.location.search).get('id');

        $.ajax({
            type: 'GET',
            url: `/api/v1/comments/${postsId}`,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
        }).done(function(data) {
            if (data.length === 0) {
                $('#commentCount').text('0');
                $('#contentHeadCommentCount').text('댓글 0');
                $('#comment').append(`<div class="text-muted text-center py-5">
                                    <div class="fs-1">
                                      <i class="bi bi-chat-left-dots-fill"></i>
                                    </div>
                                    <div>등록된 댓글이 없습니다.</div>
                                  </div>`);
                return;
            }
            $('#commentCount').text(data.length);
            $('#contentHeadCommentCount').text(`댓글 ${data.length}`);
            data.forEach((comment) => {
                $('#comment').append(function() {

                    let style = '';
                    let arrowIcon = '';
                    let owner = '';

                    if (comment.step) {
                        style = `style="background-color: #f8f9fa;"`;
                        arrowIcon = `<div class="ms-5 me-2 py-1"><i class="bi bi-arrow-return-right"></i></div>`;
                    }

                    if (comment.owner) {
                        owner = `<button class="comment-remove-btn btn text-decoration-none text-danger p-0 me-3">삭제</button>`;
                    }

                    return `<div id="${comment.commentId}" class="parent d-flex border-bottom p-3" ${style}>
                                    ${arrowIcon}
                                    <div class="d-flex flex-column w-100">
                                      <div class="d-flex align-items-center">
                                        <span class="fw-bold">${comment.author}</span>
                                        <div class="d-md-block me-md-2" style="--bs-breadcrumb-divider: '|';">
                                          <ol class="breadcrumb m-auto fw-lighter fs-4">
                                            <li class="breadcrumb-item"></li>
                                            <li class="breadcrumb-item"></li>
                                          </ol>
                                        </div>
                                        <span class="text-muted">${comment.timeDifference}</span>
                                      </div>
                                      <div class="mt-3">
                                        ${comment.content}
                                      </div>
                                      <div class="text-muted mt-3">
                                        ${owner}
                                        <button class="reply-btn btn text-decoration-none text-muted p-0 border-0">
                                          <i class="bi bi-chat-right-text-fill"></i>
                                          <span>답글 쓰기</span>
                                        </button>
                                      </div>
                                    </div>
                                  </div>`;
                });
            });
        }).fail(function(error) {
            alert(error.statusText);
            console.log(error);
        });

    },

    clickWriteReplyButton : function(id) {

        const reply = `<div id="reply" class="p-4 border-bottom" style="background-color: #f8f9fa;">
                           <div class="d-flex">
                             <div class="ms-5 me-2 py-1">
                               <i class="bi bi-arrow-return-right"></i>
                             </div>
                             <div class="border bg-white w-100">
                               <div class="px-3 py-4">
                                 <textarea name="content" id="writeReply" class="w-100 border border-0" style="height: 56px; outline: none; resize: none; overflow: hidden;" placeholder="댓글을 입력해 주세요."></textarea>
                               </div>
                               <div class="d-flex border-top">
                                 <div class="me-auto"></div>
                                 <div class="text-muted p-2">
                                   <span style="font-size: 14px;">(<span id="replyCount">0</span>/1000)</span>
                                 </div>
                                 <div>
                                   <button id="replySubmitButton" class="btn btn-success w-100 rounded-0 px-4 py-1 h-100">작성</button>
                                 </div>
                               </div>
                             </div>
                           </div>
                         </div>`;

        $.ajax({
            type: 'GET',
            url: `/api/v1/reply`,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
        }).done(function(data) {

            if (data) {
                window.location.href = '/user/login';
                return;
            }

            if ($(`#${id}`).next().attr('id') === 'reply') {
                $('#reply').remove();
                return;
            }

            if ($('#comment').find('div#reply').length) {
                $('#reply').remove();
                $(`#${id}`).after(reply);
            } else {
                $(`#${id}`).after(reply);
            }

        }).fail(function(error) {
            console.log(error);
        });
    },

    removeComment : function(init, id) {

        $.ajax({
            type: 'DELETE',
            url: `/api/v1/comments/${id}`,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function(data) {
            $('#comment').children().remove();
            init.loadComments();
        }).fail(function(error) {
            alert(error.statusText);
            console.log(error);
        });

    }

};

view.init();