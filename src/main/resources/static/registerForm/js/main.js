$(function() {

    $.validator.addMethod("password", function(value, element) {
      return this.optional(element) || /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/.test(value);
    }, '비밀번호는 영문 최소 8자리, 대문자와 숫자가 한자리 이상 들어가야 합니다.');

    $.validator.addMethod("email", function(value, element) {
          return this.optional(element) || /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/g.test(value);
        }, '유효한 이메일 주소를 입력해 주시기 바랍니다.');

    var validator = $("#signup-form").validate({
        rules: {
            name: {
                required: true,
                rangelength: [2, 30]
            },
            email: {
                required: true,
                email: true,
                remote: {
                    url: "/validateDuplicateEmail",
                    type: "post",
                    data: {
                        email : function() {
                            return $("#email").val();
                        }
                    }
                }
            },
            password: {
                required: true,
                password: true
            }
        },

        messages: {
            name : {
                required: "필수 입력 항목입니다.",
                rangelength: "최소 2자 이상 최대 30자 이하로 작성해주시기 바랍니다."
            },
            email: {
                required: "필수 입력 항목입니다.",
                remote: "이미 존재하는 이메일입니다."
            },
            password: {
                required: "필수 입력 항목입니다."
            }
        },

        submitHandler: function() {
            alert("회원가입이 정상적으로 처리되었습니다.");
            form.submit();
        }
    });

});

(function($) {

    $(".toggle-password").click(function() {

        $(this).toggleClass("zmdi-eye zmdi-eye-off");
        var input = $($(this).attr("toggle"));
        if (input.attr("type") == "password") {
          input.attr("type", "text");
        } else {
          input.attr("type", "password");
        }
      });

})(jQuery);