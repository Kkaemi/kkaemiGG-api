# KKAEMIGG
> 온라인 게임 리그오브레전드의 전적 검색 사이트입니다.  
> 2022-09월 이후 서버비용 감당이 힘들어서 사이트가 폐쇄되었습니다...
> ~~https://kkaemigg.com/~~

<br>

## 참여 인원 & 제작 기간
- 개인 프로젝트

- V1 : 2020-10-20 ~ 2021-04-06
- V2 : 2021-11-16 ~ 2021-12-17

<br>

## 기술
`Back-End`
- Java 11
- Spring Boot 2.3
- gradle 6.6
- MySQL 8.0
- JPA
- Querydsl-JPA

<br>

`Front-End`
- Vue.js 2.6
- Vue Router 3.5
- Vuex 3.6
- Vuex Persistedstate 4.1
- Vuetify 2.4

<br>

## ERD 설계
<img width="985" alt="kkaemigg-erd" src="https://user-images.githubusercontent.com/64781807/146561292-b5145e15-e061-4ed8-af0d-d587b4bc9fae.png">

<br>

## 구현 기능
<details>
<summary><b>닉네임으로 전적 검색</b></summary>
<div markdown="1">
	
### 전체 흐름 시퀀스 다이어그램
	
<img width="677" alt="스크린샷 2021-12-18 오전 3 13 53" src="https://user-images.githubusercontent.com/64781807/146589641-042cb8f3-b917-434a-aeb3-ae83f290ffda.png">
	
<br>
<br>
	
[SummonerService 보기](https://github.com/Kkaemi/kkaemiGG-api/blob/master/src/main/java/com/spring/kkaemiGG/service/SummonerService.java#L35)

- **Riot Api Library**
  - Java의 Riot api 라이브러리인 Orianna와 R4J를 모두 사용해서 서비스 로직을 구현했습니다.
  - Orianna 라이브러리가 2021-12-17 기준으로 최신 매치 리스트 riot-api 요청을 반영하고있지 않기 때문에 R4J와 함께 사용하게 되었습니다.
	
<br>

- **properties 객체** &nbsp; [코드 보기](https://github.com/Kkaemi/kkaemiGG-api/blob/master/src/main/java/com/spring/kkaemiGG/config/AppProperties.java#L17)
  - AppProperties라는 yml 파일의 프로젝트 설정 정보를 담고있는 불변 객체를 만들었습니다.

<br>

- **Vue 동적 컴포넌트** &nbsp; [코드 보기](https://github.com/Kkaemi/kkaemiGG-client/blob/master/src/components/summoner/UserCheck.vue#L21)
  - 라우터 파라미터로 받은 userName을 api 서버에 전송해서 유효한 소환사인지 체크합니다.
  - 유저가 존재하지 않으면 예외 컴포넌트를 보여주고, 유저가 존재한다면 프로필 정보, 리그 포지션 정보, 매치 정보 리스트를 비동기 요청합니다.

<br>

- **더보기 버튼** &nbsp; [코드 보기](https://github.com/Kkaemi/kkaemiGG-client/blob/master/src/components/summoner/MatchList.vue#L352)
  - 더보기 버튼을 누를 때마다 beginIndex값을 20씩 증가시켜서 과거 전적을 계속해서 조회할 수 있습니다.

</div>
</details>

<details>
<summary><b>spring oauth2 client를 사용한 google, naver 로그인</b></summary>
<div markdown="1">

### 전체 흐름 시퀀스 다이어그램

<img width="931" alt="스크린샷 2021-12-18 오전 5 41 30" src="https://user-images.githubusercontent.com/64781807/146605451-d43816e1-07dd-4b9a-8736-10c350b9c558.png">
	
<br>
<br>

- **CookieOAuth2AuthorizationRequestRepository** &nbsp; [코드 보기](https://github.com/Kkaemi/kkaemiGG-api/blob/master/src/main/java/com/spring/kkaemiGG/auth/CookieOAuth2AuthorizationRequestRepository.java#L15)
  - REST API 서버이므로 세션을 사용하지 않습니다. 세션을 사용하지 않기 때문에 기본적으로 구현되어있는 HttpSessionOAuth2AuthorizationRequestRepository를 사용하지 않고 쿠키를 사용하도록 커스터마이징 했습니다.
	
</div>
</details>

- **조회수 증가 방지** &nbsp; [코드 보기](https://github.com/Kkaemi/kkaemiGG-api/blob/2aac06ffe3d3e5f534b0810a76870efad629b085/src/main/java/com/spring/kkaemiGG/service/ViewService.java#L19)
  - api 요청 IP를 기준으로 30분에 한번씩 조회수가 증가합니다.

- **댓글 -> 답글 -> 대댓글 형태의 댓글 작성** &nbsp; [코드 보기](https://github.com/Kkaemi/kkaemiGG-api/blob/master/src/main/java/com/spring/kkaemiGG/service/CommentService.java#L51)
  - 답글과 대댓글을 구현하기 위해서 Comment 엔티티를 셀프 참조 형태로 설계했습니다.
  - 대댓글을 달 경우 자동으로 답글 게시자의 아이디를 본문 앞부분에 추가해 줍니다.
  - 객체 깊이가 무한으로 깊어지는 것을 방지하기 위해 대댓글을 저장 할 경우 그룹의 최상위 댓글을 부모 댓글로 취급합니다.

<br>

## 핵심 트러블 슈팅

- 포스트 페이징 처리
>  포스트 페이징을 할 때 댓글의 개수와 조회수를 한 화면에 같이 보여지도록 구현하고 싶었습니다.  
>  Post 엔티티의 경우 댓글과 조회수가 OneToMany 관계의 컬렉션 필드이기 때문에 N+1 문제를 방지하기 위해 페치조인을 사용해야 했습니다.  
>  하지만 페이징과 페치조인을 같이 사용하게 될 경우 모든 Post 엔티티를 DB에서 불러와서 메모리에서 페이징 처리를 하게되는 문제가 있었습니다.  
>  [이동욱님의 게시글](https://jojoldu.tistory.com/457)을 참고해서 `hibernate.default_batch_fetch_size` 적용으로 문제를 해결 할 수 있었습니다.

<br>

- 로그인 상태 관리
> oauth2 로그인 후 access token이 클라이언트의 local storage에 저장되면 로그인 된 상태가 되도록 구현했습니다.  
> 하지만 이럴 경우 access token이 만료됐을 때에도 계속 로그인 된 상태로 사용자에게 표시되는 문제가 있었습니다.  
> 저는 문제 해결을 위해서 vue router의 네비게이션 가드로 화면이 바뀔 때마다 access token을 검증하도록 구현했습니다.  
> 서버에 과부하가 걸릴 것을 염려하여 access token payload의 exp값을 클라이언트에서 검증하고  
> 새로운 토큰 발급은 서버에서 하도록 역할을 나눴습니다.  
> 그리고 한 페이지에 오래 머물러서 토큰이 만료된 경우에는 인증이 필요한 api 요청마다 토큰 검증 로직을 선행하도록 했습니다.

<br>
