# kkaemiGG
온라인 게임 리그오브레전드 전적 검색 사이트 입니다.

* Spring Initializr를 사용해서 프로젝트를 생성했습니다.

  - spring 2.3.4
  - JAVA 11
  - template engine으로 Thymeleaf를 사용했습니다.

  이 프로젝트를 진행하면서 JSON을 JAVA객체로 바꿔주는 JACKSON과 RestTemplate에 대해서 알게되었습니다.
  역시 개발하기 편하게 Orianna 라이브러리가 존재했었습니다.
  java에서는 기본 Date 클래스보다 joda time을 사용하는게 더 좋다는걸 Orianna를 사용하면서 알게되었습니다.
  
  2020.10.27까지의 홈페이지 작업입니다
  
  **검색창 밑에 자유게시판 목록구현 예정입니다.**
  <img width="1372" alt="스크린샷 2020-10-27 오후 12 51 57" src="https://user-images.githubusercontent.com/64781807/97255502-23179680-1854-11eb-871b-92d72e0339af.png">

+++

  **작업하는 도중에 Orianna 라이브러리의 존재를 알게돼서 코드를 조금 수정했습니다.**

  **현재 롤닉네임 검색 시 나오는 화면입니다**
  <img width="1309" alt="스크린샷 2020-11-16 오후 8 19 59" src="https://user-images.githubusercontent.com/64781807/99247623-8ccf0300-284a-11eb-8940-6dd833cc9a2d.png">

* 현재까지의 구현 정보입니다.
  - 속도가 너무 느려서 최근 10게임 까지의 정보만 가져오게 구현했습니다.
  - 1명의 유저의 정보만 불러올 수 있습니다.
  - 정적데이터는 주로 riot 게임즈에서 지원해주는 data dragon에서 가져오도록 구현했습니다.
  - data dragon에는 룬 이미지를 아직 지원해주지 않는 것 같아서 community dragon이란 곳을 찾아 냈지만 이 곳에서도 모든 이미지를 지원해주지는 않습니다...
  - 아이템 리스트를 인게임화면과 똑같이 맞추는 작업은 어떻게 할 지 모르겠습니다...

<img width="1904" alt="스크린샷 2020-11-18 오후 5 55 32" src="https://user-images.githubusercontent.com/64781807/99507937-5b2f7680-29c7-11eb-879b-13a217a2759f.png">

* 2020.11.18 수정내용
  - 다시 20게임 까지의 정보를 불러오도록 수정했습니다.
  - 게임 타입이 한글로 뜨도록 수정했습니다.
  - 게임 생성시간이 현재시간과 비교해서 뜨도록 수정했습니다.
  - 게임 플레이 시간이 조건적으로 뜨도록 수정했습니다. (ex 시간이 있다면 1시간 11분 11초 없다면 11분 11초)
  - 승패여부에 따른 색깔이 추가되었습니다.



**로그인폼입니다.**

<img width="1284" alt="스크린샷 2020-11-18 오후 5 59 09" src="https://user-images.githubusercontent.com/64781807/99508521-0809f380-29c8-11eb-8fb7-deb33ee12e38.png">

**회원가입폼입니다.**

<img width="1284" alt="스크린샷 2020-11-18 오후 8 52 22" src="https://user-images.githubusercontent.com/64781807/99527344-fdf3ef00-29df-11eb-87b6-b3271e127140.png">

**로그인폼과 회원가입폼 모두 https://colorlib.com/에서 가져왔습니다.**
