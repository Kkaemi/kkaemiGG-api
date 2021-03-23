let loginForm = {

    init: function() {

        const _this = this;

        let emailOldVal = "";
        let passwordOldVal = "";

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

        $('#floatingEmail').on('propertychange change keydown paste input', function() {

            const currentVal = $(this).val();

            if (currentVal == emailOldVal) {
                return;
            }

            emailOldVal = currentVal;

            if ($(this).hasClass('is-invalid')) {
                $('#floatingEmail').removeClass('is-invalid');
                $('#floatingPassword').removeClass('is-invalid');
                $('#invalidMessage').empty();

            }
        });

        $('#floatingPassword').on('propertychange change keydown paste input', function() {

            const currentVal = $(this).val();

            if (currentVal == passwordOldVal) {
                return;
            }

            passwordOldVal = currentVal;

            if ($(this).hasClass('is-invalid')) {
                $('#floatingEmail').removeClass('is-invalid');
                $('#floatingPassword').removeClass('is-invalid');
                $('#invalidMessage').empty();
            }
        });

        $('#submitButton').on('click', function() {
            _this.submit();
        });

    },

    submit: function() {

        $('#submitButton').html("<span class='spinner-border spinner-border-sm' role='status'></span>");

        const data = {
            email: $('#floatingEmail').val(),
            password: $('#floatingPassword').val()
        }

        $.ajax({
            type: 'POST',
            url: '/api/v1/authentication/login',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(data) {
            if (data.status === 200) {
                window.location.replace(data.redirectUrl);
            } else {
                $('#floatingEmail').addClass('is-invalid');
                $('#floatingPassword').addClass('is-invalid');
                $('#invalidMessage').html(`<i class='bi bi-exclamation-circle'></i> ID가 존재하지 않거나 비밀번호가 일치하지 않습니다. 다시 시도해주세요.`);
                $('#invalidMessage').show();
                $('#submitButton').html('로그인');
            }
        }).fail(function(error) {
            alert(`${error.status} 에러 발생`);
            console.log(error);
        });

    }

};

loginForm.init();