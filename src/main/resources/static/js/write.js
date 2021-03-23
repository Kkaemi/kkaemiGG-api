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

        if ($('#title').val() === '') {
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
        }).done(function() {
            alert('글이 등록되었습니다.');
            window.location.href = '/';
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });

    }

};

main.init();