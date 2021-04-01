let edit = {

    init : function() {

        const _this = this;
        const postsId = new URLSearchParams(window.location.search).get('id');
        let editor;

        ClassicEditor
                .create(document.querySelector( '#editor' ), {
                    language: 'ko'
                })
                .then( newEditor => {
                    editor = newEditor;
                    _this.loadPosts(postsId, editor);
                } )
                .catch( error => {
                    console.error( error );
                } );

        $('#saveButton, #mobileSaveButton').on('click', function() {
            _this.savePosts(postsId, editor);
        });

    },

    loadPosts : function(postsId, editor) {

        $.ajax({
            type: 'GET',
            url: `/community/edit/${postsId}`,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function(data) {
            $('#title').val(data.title);
            editor.setData(data.content);
        }).fail(function(error) {
            alert(error.statusText);
            console.log(error);
        });

    },

    savePosts : function(postsId, editor) {

        const data = {
            title: $('#title').val(),
            content: editor.getData()
        };

        $.ajax({
            type: 'PUT',
            url: `/api/v1/posts/${postsId}`,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(data) {
            if (data === 0) {
                alert('로그인해 주세요!!!');
                window.location.href = '/user/login';
                return;
            }
            window.location.href = `/community/view?id=${postsId}`
        }).fail(function(error) {
            alert(error.statusText);
            console.log(error);
        });

    }

};

edit.init();