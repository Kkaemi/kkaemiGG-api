let registerForm = {

    init : function() {

        const _this = this;

        const email = {
            type: 'email',
            inputTagId: '#floatingEmail',
            divTagId: '#invalidEmail',
            validateCheck: false,
            duplicateCheck: false,
            regExpList: [
                {
                    regExp: /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/g,
                    message: '유효한 이메일 주소를 입력해 주시기 바랍니다.'
                }],
            duplicateCheckMessage: '이미 사용중인 이메일입니다.'
        };

        const nickname = {
            type: 'nickname',
            inputTagId: '#floatingNickname',
            divTagId: '#invalidNickname',
            validateCheck: false,
            duplicateCheck: false,
            regExpList: [
                {
                    regExp: /^.{3,20}$/,
                    message: '최소 3자 이상 최대 20자 이하로 작성해주시기 바랍니다.'
                },
                {
                    regExp: /^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ]*$/,
                    message: '닉네임에 띄어쓰기 혹은 특수문자를 사용하실 수 없습니다.'
                }
            ],
            duplicateCheckMessage: '이미 사용중인 닉네임입니다.'
        };

        const password = {
            type: 'password',
            inputTagId: '#floatingPassword',
            divTagId: '#invalidPassword',
            validateCheck: false,
            regExpList: [
                {
                    regExp: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/,
                    message: '비밀번호는 영문 최소 8자리, 대문자와 숫자가 한자리 이상 들어가야 합니다.'
                }
            ],
        };

        // 모든 검사를 마치면 button 활성화 enter키 입력 활성화
        $(document).on({
            keypress: function(event) {
                $('#submitButton').toggleClass('disabled', !(email.duplicateCheck && nickname.duplicateCheck && password.validateCheck));
                if (event.keyCode === 13) {
                    if (email.duplicateCheck && nickname.duplicateCheck && password.validateCheck) {
                        _this.submit(email.duplicateCheck && nickname.duplicateCheck && password.validateCheck);
                    }
                }
            },
            keyup: function() {
                $('#submitButton').toggleClass('disabled', !(email.duplicateCheck && nickname.duplicateCheck && password.validateCheck));
            },
            blur: function() {
                $('#submitButton').toggleClass('disabled', !(email.duplicateCheck && nickname.duplicateCheck && password.validateCheck));
            }
        });

        // email 유효성 검사
        $('#floatingEmail').on('keyup', function() {
            _this.validate(email);
        });

        // nickname 유효성 검사
        $('#floatingNickname').on('keyup', function() {
        isDuplicatedNickname = false;
            _this.validate(nickname);
        });

        // password 유효성 검사
        $('#floatingPassword').on('keyup', function() {
            _this.validate(password);
        });

        // email 중복 검사
        $('#floatingEmail').on('blur', function() {
            _this.duplicateCheck(email);
            $('#submitButton').toggleClass('disabled', !(email.duplicateCheck && nickname.duplicateCheck && password.validateCheck));
        });

        // nickname 중복 검사
        $('#floatingNickname').on('blur', function() {
            _this.duplicateCheck(nickname);
            $('#submitButton').toggleClass('disabled', !(email.duplicateCheck && nickname.duplicateCheck && password.validateCheck));
        });

        $('#submitButton').on('click', function() {
            _this.submit(email.duplicateCheck && nickname.duplicateCheck && password.validateCheck);
        });

    },

    validate : function(unvalidatedValue) {

        // input tag에 키보드를 입력하면 중복체크 초기화
        unvalidatedValue.duplicateCheck = false;

        $(unvalidatedValue.inputTagId).toggleClass('is-valid', unvalidatedValue.duplicateCheck);

        const icon = "<i class='bi bi-exclamation-circle'></i>";
        const value = $(unvalidatedValue.inputTagId).val();

        // 경고 메세지 지움
        $(unvalidatedValue.divTagId).empty();

        // 빈 값이면 false
        if (value == '') {
            unvalidatedValue.validateCheck = false;
            return;
        }

        // 정규표현식 검사
        for (let i = 0; i < unvalidatedValue.regExpList.length; i++) {
            if (value.search(unvalidatedValue.regExpList[i].regExp) == -1) {
                $(unvalidatedValue.divTagId).html(`${icon} ${unvalidatedValue.regExpList[i].message}`);
                $(unvalidatedValue.divTagId).show();
                unvalidatedValue.validateCheck = false;
                break;
            }
            // 모든 유효성 검사를 통과하면 true
            unvalidatedValue.validateCheck = true;
        }

    },

    duplicateCheck : function(valueToCheck) {

        const icon = "<i class='bi bi-exclamation-circle'></i>";
        const value = $(valueToCheck.inputTagId).val();

        // 유효성 검사가 선행되지 않으면 중복체크 실행 취소
        if (!valueToCheck.validateCheck) {
            return;
        }

        $.ajax({
            type: 'GET',
            url: `/api/v1/users/${valueToCheck.type}/${value}`,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            async: false
        }).done(function(data) {
            if (data.message === 'NOT_EXIST') {
                valueToCheck.duplicateCheck = true;
                $(valueToCheck.inputTagId).toggleClass('is-valid', valueToCheck.duplicateCheck);
            }
            if (data.message === 'EXIST') {
                $(valueToCheck.divTagId).html(`${icon} ${valueToCheck.duplicateCheckMessage}`);
                $(valueToCheck.divTagId).show();
            }
        }).fail(function(error) {
            alert('서버와 통신 에러가 났습니다!!!\n다시 시도해 주세요...');
            console.log(error);
        });

    },

    submit : function(finalCheck) {

        if (!finalCheck) {
            alert('잘못된 접근입니다.\n값을 제대로 입력해주세요');
            return;
        }

        const data = {
            email: $('#floatingEmail').val(),
            nickname: $('#floatingNickname').val(),
            password: $('#floatingPassword').val()
        }

        $.ajax({
            type: 'POST',
            url: '/api/v1/users',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('회원가입이 완료되었습니다.');
            window.location.href = '/';
        }).fail(function() {
            alert('서버와 통신 에러가 났습니다!!!\n다시 시도해 주세요...');
            console.log(error);
        });
    }

};

registerForm.init();