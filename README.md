# KKAEMI.GG

__* 현재 계속 진행중인 프로젝트 입니다.__

> __테스트 계정__
>
> ID : test@test.com
>
> PW : Test1234
> (첫번째 T는 대문자 입니다.)

## 개요
- 온라인 게임 리그오브레전드의 게임 기록을 게임 닉네임을 통해서 검색 할 수 있습니다.
  - 한국 서버의 계정만 검색 가능합니다.


- 구글계정으로 간편 로그인 기능을 만들었습니다.


- 자유 게시판을 만들어서 글과 댓글을 쓰는 건 회원만 가능하지만 조회는 모두 가능하도록 만들었습니다.

## 기술
- #### JAVA 11
- #### Spring 5.2.9
  - Spring Boot 2.3.4
  - Spring Security
- #### Gradle 6.6.1
- #### AWS
  - EC2
  - RDS
  - S3
  - CodeDeploy
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

## 상세 내용

### 1. 전적 검색
#### SummonerController

```java  
@RequiredArgsConstructor  
@Controller  
public class SummonerController {  

	private final SummonerService summonerService;  

	@GetMapping("/summoner") public ModelAndView summoner(@RequestParam String userName) {  
		Summoner summoner = summonerService.getSummoner(userName);  
		
		// 등록되지 않은 유저면 user-not-found 뷰로 이동 
		if (!summoner.exists()) { 
			return new ModelAndView("summoner/not-found"); 
		}  
		
		List<LeagueEntry> leagueEntryList = summonerService.getLeagueEntryList(summoner); 
		MatchHistory matchHistory = summonerService.getMatchHistory(summoner);  
		
		return new ModelAndView("summoner/record")
				.addObject("summoner", summoner)
				.addObject("leagueEntryList", leagueEntryList)
				.addObject("matchHistory", matchHistory); 
	}  
}
```  

1. 매개변수로 받은 userName을 바탕으로 Summoner 객체를 불러오고 존재하지 않는 유저면 not-found 페이지로 이동하도록 구현했습니다.

2. 유저가 존재한다면 모델에 summoner, leagueEntryList, matchHistory를 담아서 record 페이지로 이동합니다.
   leagueEntryList - 유저의 랭크 정보를 담고있는 List입니다.
   matchHistory - 유저의 전적 정보가 담겨있는 객체입니다.

#### SummonerService

```java  
@Service  
public class SummonerService {  
	
	public SummonerService(@Value("${RIOT_API_KEY}") String apiKey) { 
		Orianna.setRiotAPIKey(apiKey); 
		Orianna.setDefaultPlatform(Platform.KOREA); 
		Orianna.setDefaultLocale(Platform.KOREA.getDefaultLocale());
	}
	
	public Summoner getSummoner(String userName) { 
		return Summoner.named(userName).get(); 
	}
	  
	public List<LeagueEntry> getLeagueEntryList(Summoner summoner) {  
		List<LeagueEntry> leagueEntryList = new ArrayList<>();  
		
		Queue.RANKED.forEach( queue -> { 
			// 존재하는 리그 포지션만 리스트에 추가 
			if (summoner.getLeaguePosition(queue) != null) { 
				leagueEntryList.add(summoner.getLeaguePosition(queue)); 
			} 
		});  
		
		return leagueEntryList;  
	}  
	
	public MatchHistory getMatchHistory(Summoner summoner) { 
		return MatchHistory.forSummoner(summoner).withEndIndex(20).get(); 
	}
 }  
```  

1. 생성자로 application.properties에 있는 RIOT_API_KEY 값을 불러와서 Orianna 라이브러리에 기본 플랫폼과 함께 세팅해줍니다.


2. getLeagueEntryList 함수는 소환사의 모든 랭크 큐 정보 중에 null이 아닌 값만 List에 담아서 반환해 줍니다.
  - Queue 클래스의 RANKED 필드는 다음과 같습니다.

```java  
public static final Set<Queue> RANKED = ImmutableSet.of(RANKED_SOLO, RANKED_FLEX, RANKED_THREES, RANKED_TFT);  
```  

3. getMatchHistory 함수는 소환사의 최신 20 게임 전적들만 불러오도록 세팅했습니다.

---

### 2. 로그인 / 회원가입

#### SecurityConfig

```java
@RequiredArgsConstructor  
@EnableWebSecurity  
@Configuration  
public class SecurityConfig extends WebSecurityConfigurerAdapter {  
	
	private final CustomOAuth2UserService customOAuth2UserService;  
	private final UserRepository userRepository;  

	@Bean  
	protected CustomUsernamePasswordAuthenticationFilter getAuthenticationFilter() throws Exception {  
	
		CustomUsernamePasswordAuthenticationFilter authenticationFilter = new CustomUsernamePasswordAuthenticationFilter();  

		authenticationFilter.setFilterProcessesUrl("/api/v1/authentication/login");  
		authenticationFilter.setAuthenticationManager(this.authenticationManagerBean());  
		authenticationFilter.setUsernameParameter("email");  
		authenticationFilter.setPasswordParameter("password");  
		authenticationFilter.setAuthenticationSuccessHandler(new CustomLoginSuccessHandler());  
		authenticationFilter.setAuthenticationFailureHandler(new CustomLoginFailureHandler());  

		return authenticationFilter;  
	}  

	@Bean  
	public PasswordEncoder encoder() {  
		return new BCryptPasswordEncoder();  
	}  

	@Override  
	protected void configure(HttpSecurity http) throws Exception {  
	
		http  
			.csrf().disable()  
			.headers().frameOptions().disable()  
			.and()  
				.authorizeRequests()  
				.antMatchers("/", "/css/**", "/img/**", "/js/**", "/h2-console/**", "/profile").permitAll()  
				.antMatchers("/community/write", "/community/write/").hasRole(Role.USER.name())  
			.and()  
				.formLogin().loginPage("/user/login")  
			.and()  
				.addFilterAt(getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)  
				.logout()  
					.logoutSuccessUrl("/")  
			.and()  
				.oauth2Login()  
					.userInfoEndpoint()  
						.userService(customOAuth2UserService);  

	}  

	@Override  
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {  
		auth  
			.userDetailsService(email -> userRepository.findByEmail(email)
					.orElseThrow(() ->  new UsernameNotFoundException("해당 유저가 존재하지 않습니다."))  
			).passwordEncoder(encoder());  
	}  
}
```

1. Spring Security는 기본적으로 JSON 요청을 지원하지 않습니다. 그래서 JSON 요청도 받아서 처리할 수 있게 UsernamePasswordAuthenticationFilter를 상속받은 CustomUsernamePasswordAuthenticationFilter를 만들었습니다.

2. SuccessHandler와 FailureHandler도 페이지를 리다이렉트 시키지 않고 JSON 형식으로 응답하도록 만들었습니다.

3. 게시판 글쓰기는 로그인 한 유저만 가능하도록 권한 설정을 해줬습니다.

4. UserDetailsService의 구현체를 만들어서 SecurityConfig의 생성자로 주입하면 순환 참조가 되는 문제가 있어서 람다식으로 구현했습니다.

#### CustomLoginSuccessHandler

```java
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {  
	
	@Override  
	public void onAuthenticationSuccess(HttpServletRequest request,  
										HttpServletResponse response,  
										Authentication authentication)  
			throws IOException {  

		String redirectUrl = "/";  

		// 뷰 컨트롤러에서 만든 prevPage 세션 정보를 가져온다  
		String prevPage = (String) request.getSession().getAttribute("prevPage");  

		User user = (User) authentication.getPrincipal();  
		request.getSession().setAttribute("user", new SessionUser(user));  

		/*  
		* 유저가 권한이 필요한 요청을 하면 시큐리티가 요청을 가로채게 되는데, 
		* 유저가 원래 요청한 URL 정보를 RequestCache 안에 SavedRequest에 저장한다. 
		* 
		* RequestCache와 SavedRequest는 인터페이스이다. 
		* 이를 구현한 기본 구현체로는 HttpSessionRequestCache와 DefaultSavedRequest가 있다. 
		* */  
		RequestCache requestCache = new HttpSessionRequestCache();  
		SavedRequest savedRequest = requestCache.getRequest(request, response);  

		// Spring Security가 요청을 가로 챈 경우  
		if (savedRequest != null) {  
			redirectUrl = savedRequest.getRedirectUrl();  

			// 세션에 저장된 객체를 다 사용한 뒤에는 지워줘서 메모리 누수 방지  
			requestCache.removeRequest(request, response);  

			respondWithJson(response, redirectUrl);  
			return;  
		}  

		// 유저가 로그인 버튼을 눌러서 로그인 할 경우  
		if (StringUtils.hasText(prevPage)) {  
			redirectUrl = prevPage;  

			// 세션에 저장된 객체를 다 사용한 뒤에는 지워줘서 메모리 누수 방지  
			request.getSession().removeAttribute("prevPage");  

			respondWithJson(response, redirectUrl);  
			return;  
		}  

		respondWithJson(response, redirectUrl);  

	}  

	private void respondWithJson(HttpServletResponse response, String redirectUrl) throws IOException {  

		UsersLoginResponseDto responseDto = UsersLoginResponseDto.builder()  
				.status(HttpStatus.OK.value())  
				.message("LOGIN_SUCCEEDED")  
				.redirectUrl(redirectUrl)  
				.build();  

		response.setContentType(ContentType.APPLICATION_JSON.getMimeType());  
		response.setCharacterEncoding(CharEncoding.UTF_8);  
		response.setStatus(HttpStatus.OK.value());  
		
		PrintWriter out = response.getWriter();  
		out.print(new Gson().toJson(responseDto));  
		out.flush();  

	}  

}
```

1. 로그인이 성공하면 우선순위에 따라서 처음 요청했던 페이지로 이동하도록 만들었습니다.

1순위 - 사용자가 권한이 필요한 페이지를 요청했을 때 Spring Security가 요청을 가로 챈 경우
2순위 - 사용자가 로그인 버튼을 눌러서 로그인 페이지로 이동했을 경우
디폴트 - 사용자가 url을 직접 입력하거나 즐겨찾기 등록을 해서 이전 페이지 정보가 없을 경우 홈페이지(/)로 이동

```java
@Controller  
@RequestMapping("/user")  
public class UserController {  
  
	@GetMapping("/login")  
	public String loginForm(HttpServletRequest request) {  
		Optional.ofNullable(request.getHeader("Referer"))  
				.filter(url -> !url.contains("/login"))  
				.filter(url -> !url.contains("/join"))  
				.ifPresent(url -> request.getSession().setAttribute("prevPage", url));  

		return "user/login-form";  
	}  

	...
  
}
```

2. prevPage 세션은 Referer 헤더의 값을 기준으로 만들어집니다.
   Referer 헤더의 값이 null이 아니고 url에 /login 이나 /join 문자열이 없는 경우에만 세션이 만들어집니다.

---

### 3. 자유 게시판

#### Table ERD
<img width="639" alt="스크린샷 2021-04-05 오전 10 49 11 2" src="https://user-images.githubusercontent.com/64781807/113529123-98567700-95fd-11eb-9dea-cd65f8d404e3.png">

#### PostsService

```java
@RequiredArgsConstructor  
@Service  
public class PostsService {  
  
	private final PostsRepository postsRepository;  
	private final UserRepository userRepository;  
  
    ...
    
	@Transactional  
	public PostsResponseDto findByIdWithSession(Long id, SessionUser sessionUser) {  

		PostsResponseDto responseDto = postsRepository.findById(id)  
				.map(entity -> {  
					entity.hit(); // 조회수 증가  
					return new PostsResponseDto(entity);  
				})  
				.orElseGet(() -> new PostsResponseDto(-1L));  

		if (sessionUser != null && sessionUser.getId().equals(responseDto.getUserId())) {  
			responseDto.setOwner(true);  
		}  

		return responseDto;  

	}
  
	@Transactional(readOnly = true)  
	public Page<PostsListResponseDto> findByRequest(PostsPageRequestDto requestDto) {  
		PageRequest pageRequest = PageRequest.of(requestDto.getPage(), 20, Sort.Direction.DESC, "createdDate");  
		
		// 검색 조건 추가된 경우
		if (requestDto.isSearched()) {  
			if (requestDto.getTarget().equals("title")) {  
				return postsRepository.findByTitleContaining(requestDto.getKeyword(), pageRequest)  .map(PostsListResponseDto::new);  
			}  

			if (requestDto.getTarget().equals("author")) {  
				return postsRepository.findByAuthorContaining(requestDto.getKeyword(), pageRequest)  .map(PostsListResponseDto::new);  
			}  
		}
		
		// 인기순 정렬 구현 예정

		return postsRepository.findAll(pageRequest).map(PostsListResponseDto::new);  
	}  
}
```
1. 포스트 뷰 페이지로 이동하면 조회수를 하나 증가시켜 주고 세션의 id 값과 posts 엔티티에 저장되어있는 user의 id 값이 동일할 경우 글의 주인이므로 응답 Dto의 owner 값을 true로 바꿔줍니다.

2. 페이징 요청 시 검색 조건이 추가 됐는지 확인 하고 조건에 맞는 쿼리가 실행 되도록 구현했습니다.
   이 부분에서 게시글 마다 댓글의 개수도 같이 보이게 구현하다 보니 N + 1 문제가 발생했습니다.
   Querydsl의 동적쿼리와 페치 조인을 이용해서 코드를 수정하기 위해 공부중입니다.

```java
@Getter  
@NoArgsConstructor  
@Entity  
public class Posts extends BaseTimeEntity {

	...
	
	@OneToMany(mappedBy = "posts", orphanRemoval = true)  
	private List<Comment> comments = new ArrayList<>();
	
	...
	
}
```

3. orphanRemoval = true 옵션을 줘서 게시글이 삭제되면 관련 댓글들도 모두 같이 삭제되도록 구현했습니다.

#### Comment

```java
@Getter  
@NoArgsConstructor  
@Entity  
public class Comment extends BaseTimeEntity {
	
	...

	@ManyToOne(fetch = FetchType.LAZY)  
	@JoinColumn(name = "GROUP_ID")  
	private Comment parentComment;  
	  
	@OneToMany(mappedBy = "parentComment")  
	private List<Comment> childComments = new ArrayList<>();

	...
	
}
```

1. 댓글과 대댓글을 구현하기 위해서 Comment 엔티티를 셀프 참조 형태로 만들었습니다.

2. 자식 댓글(대댓글)이 있는 부모 댓글이라면 삭제되지 않고 상태만 변경해 주기 위해 deletion 칼럼을 만들었습니다.

#### CommentController

```java
@RequiredArgsConstructor  
@RestController  
public class CommentApiController {

	...
	
	@GetMapping("/api/v1/reply")  
	public Boolean reply(@LoginUser SessionUser sessionUser) {  
		return sessionUser == null;  
	}
	
	...
	
}
```

1. 답글 달기 버튼을 누를 경우 세션이 없다면 로그인 페이지로 이동하도록 구현했습니다.

#### CommentService

```java
@RequiredArgsConstructor  
@Service  
public class CommentService {  

	private final CommentRepository commentRepository;  
	private final PostsRepository postsRepository;  
	private final UserRepository userRepository;  
	private final CommentQueryRepository queryRepository;  

	public Long save(CommentSaveRequestDto requestDto) {  
	
		Posts posts = postsRepository.findById(requestDto.getPostsId())  
						.orElseThrow(() -> new IllegalArgumentException("해당 ID의 게시글이 없습니다."));  

		User user = userRepository.findById(requestDto.getUserId())  
						.orElseThrow(() -> new IllegalArgumentException("해당 ID의 유저가 없습니다."));  

		// 만약 부모 댓글 ID가 있는 경우  
		if (requestDto.getParentCommentId() != null) {  
			Comment parentComment = commentRepository.findById(requestDto.getParentCommentId())  
					.orElseThrow(() -> new IllegalArgumentException("해당 ID의 댓글이 없습니다."));  
			Comment grandParentComment = parentComment.getParentComment();  
			Comment childComment = requestDto.toEntity();  

			childComment.setPosts(posts);  
			childComment.setUser(user);  
			childComment.setGroupOrder(grandParentComment.getChildComments().size() + 1);  
			childComment.setParentComment(grandParentComment);  

			if (!requestDto.getParentCommentId().equals(grandParentComment.getId())) {  
				childComment.addTargetNickname(requestDto.getTargetNickname());  
			}  

			commentRepository.save(grandParentComment);  
			postsRepository.save(posts);  

			return commentRepository.save(childComment).getId();  
		}  

		Comment comment = requestDto.toEntity();  
		comment.setPosts(posts);  
		comment.setUser(user);  
		comment.setParentComment(comment);  
		postsRepository.save(posts);  

		return commentRepository.save(comment).getId();  
	}  

	public List<CommentResponseDto> find(Long postsId, SessionUser sessionUser) {  
		List<CommentResponseDto> responseDtoList = queryRepository.getCommentList(postsId);  
		if (sessionUser != null) {  
			responseDtoList.forEach(dto -> dto.setOwner(sessionUser.getId()));  
		}  
		return responseDtoList;  
	}  

	public void delete(Long commentId) {  

		Comment comment = commentRepository.findById(commentId)  
				.orElseThrow(() -> new IllegalArgumentException("해당 ID의 댓글이 없습니다."));  
		Comment parentComment = comment.getParentComment();  

		// 부모 댓글일 경우 상태만 변경  
		if (comment.getChildComments().size() > 1) {  
			comment.setDeletion(true);  
			return;  
		}  

		// 부모 댓글의 상태가 삭제된 상태고 자식 댓글이 하나밖에 없으면 자식댓글과 부모댓글 같이 삭제  
		if (parentComment.getDeletion() && parentComment.getChildComments().size() == 2) {  
			commentRepository.delete(comment);  
		    commentRepository.delete(parentComment);  
		    return;  
		}

		// 삭제 되는 댓글 보다 뒤에 있는 댓글들의 groupOrder를 하나씩 빼줌
		comment.getParentComment().getChildComments().stream()  
		.filter((childComment) -> childComment.getGroupOrder() > comment.getGroupOrder())  
		.forEach((childComment) -> childComment.setGroupOrder(childComment.getGroupOrder() - 1));  

		commentRepository.delete(comment);  
	}
	
}
```

1. 댓글을 DB에 저장할 때 자식 댓글로 추가가되는 댓글이라면 최상위 댓글의 자식으로 추가되도록 구현했습니다. 셀프 참조 형태이기 때문에 최상위 댓글을 저장할 때도 자기 자신을 자식으로 추가되도록 구현했습니다.

2. 댓글을 불러올 때 세션이 있다면 세션의 id와 comment의 user id가 일치한다면 댓글의 주인이므로 owner 값에 true를 세팅해주도록 구현했습니다.

3. 댓글을 삭제할 경우 자식이 있는 최상위 댓글이라면 상태만 변경해주고
   부모 댓글의 상태가 삭제된 상태이고 자식 댓글이 하나밖에 없다면 자식 댓글과 부모 댓글을 같이 삭제하도록 구현했습니다.
   자식 댓글만 삭제 하는 경우라면 삭제되는 자식 댓글보다 최신 댓글들의 순서를 하나씩 앞당겨 주도록 구현했습니다.

---