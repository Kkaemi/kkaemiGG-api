let main = {

    init : function() {

        const _this = this;
        let editor;

        ClassicEditor
                .create(document.querySelector( '#editor' ), {
                    language: 'ko'
                })
                .then( newEditor => {
                    editor = newEditor;
                } )
                .catch( error => {
                    console.error( error );
                } );

        $('#mobileSaveButton, #saveButton').on('click', function() {
            _this.save(editor);
        });

    },

    save : function(editor) {

        if (!$('#title').val().trimLeft().trimRight()) {
            alert('제목을 입력해 주세요');
            return;
        }

        if (editor.getData() === '') {
            alert('내용을 입력해 주세요');
            return;
        }

        var data = {
            title: $('#title').val(),
            content: editor.getData()
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/posts',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(data) {
            if (data === 0) {
                alert('에러가 발생했습니다!!!');
                return;
            }
            alert('글이 등록되었습니다.');
            window.location.replace(`/community/view?id=${data}`);
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });

    }

};

main.init();