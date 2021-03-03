let registerForm = {
    init : function() {

        let _this = this;

        let isValidatedEmail = false;
        let isValidatedNickname = false;
        let isValidatedPassword = false;

        let isDuplicatedEmail = false;
        let isDuplicatedNickname = false;

        // Enter키 입력 시 자동 submit 방지
        $('form').on('keydown', function(event) {
          if (event.keyCode === 13) {
            event.preventDefault();
          };
        });

        // email 유효성 검사
        $('#floatingInput').on('keyup', function() {
            isValidatedEmail = _this.validateEmail();
            $('#submitButton').toggleClass('disabled', !(isDuplicatedEmail && isDuplicatedNickname && isValidatedPassword));
        });

        // email 중복 검사
        $('#floatingInput').on('blur', function() {
            isDuplicatedEmail = _this.checkEmailIsDuplicated(isValidatedEmail);
            $('#submitButton').toggleClass('disabled', !(isDuplicatedEmail && isDuplicatedNickname && isValidatedPassword));
        });

        // nickname 유효성 검사
        $('#floatingNickname').on('keyup', function() {
            isValidatedNickname = _this.validateNickname();
            $('#submitButton').toggleClass('disabled', !(isDuplicatedEmail && isDuplicatedNickname && isValidatedPassword));
        });

        // nickname 중복 검사
        $('#floatingNickname').on('blur', function() {
            isDuplicatedNickname = _this.checkNicknameIsDuplicated(isValidatedNickname);
            $('#submitButton').toggleClass('disabled', !(isDuplicatedEmail && isDuplicatedNickname && isValidatedPassword));
        });

        // password 유효성 검사
        $('#floatingPassword').on('keyup', function() {
            isValidatedPassword = _this.validatePassword();
            $('#submitButton').toggleClass('disabled', !(isDuplicatedEmail && isDuplicatedNickname && isValidatedPassword));
        });



    },

    validateEmail : function() {

        let email = $('#floatingInput').val();
        let isValidatedEmail = false;

        $('#invalidEmail').empty();

        if (email == '') {
            $('#invalidEmail').empty();
            return isValidatedEmail;
        }

        if (!/^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/g.test(email)) {
            $('#invalidEmail').html("<i class='bi bi-exclamation-circle'></i> 유효한 이메일 주소를 입력해 주시기 바랍니다.");
            $('#invalidEmail').show();
            return isValidatedEmail;
        }

        isValidatedEmail = true;

        return isValidatedEmail;

    },

    checkEmailIsDuplicated : function(isValidatedEmail) {

        let isDuplicatedEmail = false;

        if (!isValidatedEmail) {
            return isDuplicatedEmail;
        }

        isDuplicatedEmail = true;

        return isDuplicatedEmail;

    },

    validateNickname : function() {

        let nickname = $('#floatingNickname').val();
        let isValidatedNickname = false;

        $('#invalidNickname').empty();

        if (nickname == '') {
            $('#invalidNickname').empty();
            return isValidatedNickname;
        }

        if (!(nickname.length >= 3 && nickname.length <= 20)) {
            $('#invalidNickname').html("<i class='bi bi-exclamation-circle'></i> 최소 3자 이상 최대 20자 이하로 작성해주시기 바랍니다.");
            $('#invalidNickname').show();
            return isValidatedNickname;
        }

        if (!/^[a-zA-Z0-9가-힣]*$/.test(nickname)) {
            $('#invalidNickname').html("<i class='bi bi-exclamation-circle'></i> 닉네임에 띄어쓰기 혹은 특수문자를 사용하실 수 없습니다.");
            $('#invalidNickname').show();
            return isValidatedNickname;
        }

        isValidatedNickname = true;

        return isValidatedNickname;

    },

    checkNicknameIsDuplicated : function(isValidatedNickname) {

        let isDuplicatedNickname = false;

        if (!isValidatedNickname) {
            return isDuplicatedNickname;
        }

        isDuplicatedNickname = true;

        return isDuplicatedNickname;

    },

    validatePassword : function() {

        let password = $('#floatingPassword').val();
        let isValidatedPassword = false;

        $('#invalidPassword').empty();

        if (password == '') {
            $('#invalidPassword').empty();
            return isValidatedPassword;
        }

        if (!/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/.test(password)) {
            $('#invalidPassword').html("<i class='bi bi-exclamation-circle'></i> 비밀번호는 영문 최소 8자리, 대문자와 숫자가 한자리 이상 들어가야 합니다.");
            $('#invalidPassword').show();
            return isValidatedPassword;
        }

        isValidatedPassword = true;

        return isValidatedPassword;

    }

};

registerForm.init();