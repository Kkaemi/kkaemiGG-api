# KKAEMI.GG

__* 현재 계속 진행중인 프로젝트 입니다.__

> __테스트 계정__
> 
> ID : test@test.com
> 
> PW : Test1234
> (첫번째 T는 대문자 입니다.)

## 개요
- 리그오브레전드 전적 검색 사이트 OP.GG의 클론 사이트입니다.

---

## 기술
- #### JAVA 11
- #### Spring 5.2.9
  - Spring Boot 2.3.4
  - Spring Security
- #### AWS
  - EC2
  - RDS
- #### DataBase
  - MariaDB
- #### ORM
  - JPA
- #### Web
  - HTML
  - Thymeleaf
  - Bootstrap 5.0.0
  - jQuery
  - Ajax
- #### Riot API Library
  - Orianna
  
---

## 지금까지 구현 한 기능
- 메인, 검색결과 페이지
  - [Youtube Api를 사용해서 동영상 불러오기](https://github.com/Kkaemi/kkaemiGG/blob/master/src/main/java/com/spring/kkaemiGG/service/youtube/YoutubeService.java)
  - [Thymeleaf를 사용해서 검색 결과 화면 보여주기](https://github.com/Kkaemi/kkaemiGG/blob/master/src/main/resources/templates/summoner/record.html)
  

- 로그인, 회원가입
  - [jQuery key 이벤트를 활용한 유효성 검사](https://github.com/Kkaemi/kkaemiGG/blob/master/src/main/resources/static/js/register-form.js)


- [Spring Security](https://github.com/Kkaemi/kkaemiGG/tree/master/src/main/java/com/spring/kkaemiGG/config/auth)
  - [JSON 요청도 처리 할 수 있게 UsernamePasswordAuthenticationFilter 수정](https://github.com/Kkaemi/kkaemiGG/blob/master/src/main/java/com/spring/kkaemiGG/config/auth/CustomUsernamePasswordAuthenticationFilter.java)
  - [로그인 완료 후 요청했던 페이지로 이동](https://github.com/Kkaemi/kkaemiGG/blob/master/src/main/java/com/spring/kkaemiGG/config/auth/CustomLoginSuccessHandler.java)
  - [UserController에서 Referer 헤더를 참조해서 prevPage 세션 생성](https://github.com/Kkaemi/kkaemiGG/blob/master/src/main/java/com/spring/kkaemiGG/web/controller/user/UserController.java)
  - [로그인 실패 시 로그인 실패 페이지로 이동하는게 아니라 JSON 데이터를 보냄](https://github.com/Kkaemi/kkaemiGG/blob/master/src/main/java/com/spring/kkaemiGG/config/auth/CustomLoginFailureHandler.java)
  - [Google 로그인 구현](https://github.com/Kkaemi/kkaemiGG/blob/master/src/main/java/com/spring/kkaemiGG/config/auth/CustomOAuth2UserService.java)

  
- 커뮤니티
  - 부트스트랩으로 반응형 페이지 구현
  - 로그인 한 회원만 글쓰기 가능
  - 글쓰기 기능 구현
---

## 앞으로 해야 할 일
- 전적검색 결과 화면
  - REST API로 만들어서 AJAX로 결과 화면 보여주기
  - 같이 게임 했던 유저의 닉네임이 길 경우 ...으로 보여주기
  - 아이템이나 룬 이미지에 상세 설명 보여주는 마우스 이벤트 만들
  

- 로그인, 회원가입
  - 회원가입 시 이메일 인증 단계 추가하기
  - 로그인 화면에 비밀번호 찾기 페이지 추가하기


- 자유 게시판
  - 게시판 목록 최신순, 인기순으로 불러오기
  - 게시판 뷰 페이지 만들기
  - 좋아요, 싫어요 기능 만들기
  - 댓글 기능 만들기