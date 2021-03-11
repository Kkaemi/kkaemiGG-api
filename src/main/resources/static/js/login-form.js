let loginForm = {

    init: function() {

        const _this = this;

        $(document).on({
            keyup: function() {
                if ($('#floatingEmail').val() === '' || $('#floatingPassword').val() === '') {
                    $('#submitButton').toggleClass('disabled', true);
                } else {
                    $('#submitButton').toggleClass('disabled', false);
                }
            },
            keypress: function(event) {
                if (event.keyCode === 13) {
                    if (!$('#submitButton').hasClass("disabled")) {
                        _this.submit();
                    }
                }
            }
        });

        $('#submitButton').on('click', function() {
            _this.submit();
        });

    },

    submit: function() {

        const data = {
            email: $('#floatingEmail').val(),
            password: $('#floatingPassword').val()
        }

        $.ajax({
            type: 'POST',
            url: '/auth/login',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(data) {
            alert('로그인 성공');
            console.log(data);
        }).fail(function(error) {
            alert(`${error.status} 에러 발생`);
            console.log(error);
        });

    }

};

loginForm.init();