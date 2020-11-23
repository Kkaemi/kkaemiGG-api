package com.spring.kkaemiGG.bean;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class MemberDTO {

    @Pattern(regexp = "^[가-힣]{2,4}|[a-zA-Z]{2,10}\\s[a-zA-Z]{2,10}$",
            message = "올바른 이름을 입력해 주세요.")
    private String name;

    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$",
            message = "유효한 이메일 주소를 입력해 주시기 바랍니다.")
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",
            message = "비밀번호는 영문 최소 8자리, 대문자와 숫자가 한자리 이상 들어가야 합니다.")
    private String password;
}
