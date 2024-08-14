참고한 재생목록
- https://youtube.com/playlist?list=PLJkjrxxiBSFCKD9TRKDYn7IE96K2u3C3U&si=ywprryUfy-SDyrCg

정리된 문서 리스트
- https://www.devyummi.com/page?id=668bd2d92b88a1ef5f2be2e3

---

- 시큐리티를 의존성에 추가해놓게 되면 스프링 시큐리티가 클라이언트가 보낸 요청을 가로채서 시큐리티 필터를 통해 검증
- 해당 경로의 접근이 누구에게 열려있는지
- 로그인이 완료된 사용자인지
- 해당되는 role을 가지고 있는지

### 인증
- 시스템에서 사용자가 누구인지 확인하는 과정 = 사용자가 주장하는 신원이 실제로 그 사용자와 일치하는지 검증하는 절차
- 사용자가 시스템에 접근할 때, 시스템이 사용자의 신원을 확인하여 해당 사용자가 누구인지 파악하기 위한 목적
- `당신이 누구인가?`

### 인가
- 인증된 사용자가 특정 자원에 접근하거나 작업을 수행할 권한이 있는지 확인하는 과정
- 사용자가 접근할 수 있는 자원이나 기능을 제한하고, 권한이 없는 사용자가 민감한 데이터나 기능에 접근하지 못하게 하기위한 목적
- `당신이 어떤 권한을 가지고 있는가?`

---

## SecurityConfig
시큐리티 설정을 위한 설정 파일
@EnableWebSecurity는 보안 설정을 커스터마이징하기 위해서는 SecurityConfig에 붙여야하는 어노테이션
기본설정을 그대로 사용할 때는 붙이지 않아도되지만 일반적으로는 붙이는게 맞다

#### 시큐리티 버전별 구현
- 버전별로 구현이 상이하다. 이거때문에 고생 좀 했다.
- 아래 링크의 문서에 잘 정리되어 있다
- https://www.devyummi.com/page?id=668bd7fe16014d6810ed85f7

---

## 커스텀 로그인 설정
- SecurityConfig 클래스를 등록하기 전에는 접속시 자동으로 스프링 시큐리티 로그인 페이지로 리다이렉팅해줬지만 시큐리티 설정을 등록하고 나서는 이러한 모든 작업을 커스터마이징 해줘야한다
- 그래서 시큐리티 설정을 등록하고 접속하면 로그인 페이지가 아닌 액세스 거부 에러 페이지가 나온다
- 이 때 커스텀 로그인 페이지로 리다이렉팅 시켜주기 위해서는 시큐리티 설정에서 formLogin 설정을 해줘야한다
```agsl
        http
                .formLogin((auth) -> auth
                        .loginPage("/login")    // 리다이렉팅될 로그인 페이지 경로
                        .loginProcessingUrl("/loginProc")   // 시큐리티가 로그인 처리를 진행해줄 경로. 어차피 html안에 내용있으니까 될줄알았는데 이 설정 없으면 로그인 처리 안됨
                        .permitAll()    // 모두가 들어올 수 있도록 설정
                );
```
- 추가적으로 csrf 토큰을 보내야 로그인이 진행되는데, 시큐리티는 자동으로 csrf 방지 기능을 켜기 때문에, 간단한 테스트를 위해 개발환경에서는 그 설정을 꺼준다

---

## 시큐리티 암호화
- 스프링 시큐리티는 사용자 인증(로그인)시 비밀번호에 대해 단방향 해시 암호화를 진행하여 저장되어 있는 비밀번호와 대조한다.
- 따라서 회원가입시 비밀번호 항목에 대해서 암호화를 진행해야 한다.
- 스프링 시큐리티는 암호화를 위해 BCrypt Password Encoder를 제공하고 권장한다. 따라서 해당 클래스를 return하는 메소드를 만들어 @Bean으로 등록하여 사용하면 된다.

---

## 회원가입
유저 엔티티를 만들때 권한 컬럼이 필수적으로 들어가야한다
```java
@Entity
@Setter
@Getter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String password;

    private String role;    // 필수적으로 들어가야함
}
```
그리고 회원가입 진행시 권한 데이터를 넣을때 사용되는 포맷이 `ROLE_`을 접두사로 붙여서 권한을 붙여주면된다

form 태그 관련 질문
- Q. post라 @RequestBody를 해야한다고 생각하는데 오히려 사용하면 에러가 나고 왜 @Setter를 해야 값이 매핑되는건가요? @AllArgsConstructor는 값을 넣어줄수 없는건가요?
- 일반적인 form태그에서 보내시면 multipart/form-data 형식으로 보내지기 때문에 json이 아닌 상태로 전송됩니다.따라서 @RequestBody로 받으실 수 없습니다.

---

## DB 기반 로그인 검증 로직
로그인을 검증하기 위해서는 `UserDetailService`와 `UserDetails`를 구현해줘야한다

UserDetailService를 통해 UserDetails를 생성해서 SecurityConfig로 보내면, SecurityConfig가 이것을 검증하고, 완료되면 스프링 세션에 저장해주고 사용자가 접근할 수 있도록 허용해준다

= 시큐리티를 통해 인증을 진행하는 방법은 사용자가 Login 페이지를 통해 아이디, 비밀번호를 POST 요청시 스프링 시큐리티가 데이터베이스에 저장된 회원 정보를 조회 후 비밀번호를 검증하고 서버 세션 저장소에 해당 아이디에 대한 세션을 저장한다.

---

## 세션 사용자 아이디 정보
`SecurityContextHolder.getContext().getAuthentication().getName();`

---

## 세션 설정
사용자가 로그인을 진행한 뒤 사용자 정보는 SecurityContextHolder에 의해서 서버 세션에 관리된다

그리고 해당 세션에 대한 세션ID는 사용자에게 쿠키로 반환된다

ㅇ때 세션에 관해 소멸 시간, 아이디당 세션 생성 개수(다중 로그인) 등을 설정할 수 있다

(JWT 같은 경우는 세션에 계속 머무는게 아닌 세션이 Stateless 상태로 관린된다)

#### 세션 고정 보호
해커가 Admin 계정과 같은 유저 계정의 세션 ID를 탈취해서 Admin 계정처럼 위장해서 요청을 보내는 것을 방지하기 위해 사용

세션 고정 공격의 과정은 아래와 같다
1. 해커가 서버에 접속해서 세션 쿠키를 하나 생성한다
2. 해커가 그 쿠키를 User에게 심는다
3. User가 그 쿠키를 들고 서버에 접속하면 해당 쿠키는 그 User의 권한을 가진 쿠키가 된다
4. 해커는 그 쿠키를 이용해서 User의 권한을 사용한다

방어를 위한 설정
- sessionManagement().sessionFixation().none() : 로그인 시 세션 정보 변경 안함 (방어 못함)
- sessionManagement().sessionFixation().newSession() : 로그인 시 세션 새로 생성
- sessionManagement().sessionFixation().changeSessionId() : 로그인 시 동일한 세션에 대한 id 변경

---

## CSRF enable 설정
CSRF(Cross-Site Request Forgery)는 요청을 위조하여 사용자가 원하지 않아도 서버측으로 특정 요청을 강제로 보내는 방식이다. (회원 정보 변경, 게시글 CRUD를 사용자 모르게 요청)

기존의 테스트 환경에서와 달리 csrf 시큐리티 설정을 활성화시키면.

스프링 시큐리티는 CsrfFilter를 통해 POST, PUT, DELETE 요청에 대해서 csrf 토큰 검증을 진행한다

#### 배포환경에서 CSRF 토큰을 집어넣고자 할때
- POST 요청에서 설정 방법
```agsl
<form action="/loginReceiver" method="post" name="loginForm">
    <input type="text" name="username" placeholder="아이디"/>
    <input type="password" name="password" placeholder="비밀번호"/>
    <input type="hidden" name="_csrf" value="{{_csrf.token}}"/>
    <input type="submit" value="로그인"/>
</form>
```
- ajax 요청시 (HTML <head> 구획에 아래 요소 추가)
```agsl
<meta name="_csrf" content="{{_csrf.token}}"/>
<meta name="_csrf_header" content="{{_csrf.headerName}}"/>
```
ajax 요청시 위의 content 값을 가져온 후 함께 요청

XMLHttpRequest 요청시 setRequestHeader를 통해 _csrf, _csrf_header Key에 대한 토큰 값 넣어 요청

#### 로그아웃
csrf 설정시 POST 요청으로 로그아웃을 진행해야 하지만 아래의 SecurityConfig 설정을 통해 GET 방식으로 진행할 수 있다
```agsl
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

    http
            .logout((auth) -> auth.logoutUrl("/logout")
                    .logoutSuccessUrl("/"));

    return http.build();
}
```

앱에서 사용하는 API 서버의 경우 보통 세션을 STATELESS로 관리하기 떄문에 스프링 시큐리티 csrf를 disable 해도 된다 (JWT와 같은 방식을 사용하면 애초에 세션이 생성되지 않기 때문)

---

## InMemory 방식 유저 정보 저장
토이 프로젝트를 진행하는 경우 또는 시큐리티 로그인 환경이 필요하지만 소수의 회원 정보만 가지며 데이터베이스라는 자원을 투자하기 힘든 경우는 회원가입 없는 InMemory 방식으로 유저를 저장하면 된다.

이 경우 InMemoryUserDetailsManager 클래스를 통해 유저를 등록하면 된다.

하지만 사용할 일은 거의 없을듯하다.

## Http Basic 인증방식
Http Basic 인증 방식은 아이디와 비밀번호를 Base64 방식으로 인코딩한 뒤 HTTP 인증 헤더에 부착하여 서버측으로 요청을 보내는 방식이다.

Http Basic 방식은 주로 내부망의 서버간 통신을 진행하는 경우 사용합니다. 이 경우는 로그인 페이지도 없고 있다고 하더라도 서버가 서버에게 로그인 페이지에서 로그인을 진행할 수 없기 때문에 사용합니다.

## Role Hierarchy (계층 권한)
#### 계층권한이란?
권한 A, 권한 B, 권한 C가 존재하고 권한의 계층은 “A < B < C”라고 설정을 진행하고 싶은 경우 RoleHierarchy 설정을 진행할 수 있다.

예를 들어 Admin 권한은 그 밑의 User 권한까지 모두 포함하도록 만들기위해 사용한다

아래와 같이 SecurityConfig를 설정해서 계층을 설정하면 된다
```agsl
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

    http
            .csrf((auth) -> auth.disable());

    http
            .authorizeHttpRequests((auth) -> auth
                    .requestMatchers("/login").permitAll()
                    .requestMatchers("/").hasAnyRole("A", "B", "C")
                    .requestMatchers("/manager").hasAnyRole("B", "C")
                    .requestMatchers("/admin").hasAnyRole("C")
                    .anyRequest().authenticated()
            );

    http
            .formLogin((auth) -> auth.loginPage("/login")
                    .loginProcessingUrl("/loginProc")
                    .permitAll()
            );

    return http.build();
}
```
하지만 이렇게 설정하면 권한의 개수가 많아졌을때 관리가 힘들기 때문에, 메소드 등록을 통해 권한을 관계를 설정해서 사용하면 좋다

시큐리티 6.3.x 버전 이후 메소드 형식 적용법

- 계층 권한 메소드 등록
```java
@Bean
public RoleHierarchy roleHierarchy() {

    return RoleHierarchyImpl.fromHierarchy("""
            ROLE_C > ROLE_B
            ROLE_B > ROLE_A
            """);
}
```

아래와 같이 사용할 수도 있다
```java
@Bean
public RoleHierarchy roleHierarchy() {

    return RoleHierarchyImpl.withDefaultRolePrefix()
            .role("C").implies("B")
            .role("B").implies("A")
            .build();
}
```

만약 접두사를 `ROLE_`이 아닌 다른 값으로 하고 싶다면
```java
@Bean
public RoleHierarchy roleHierarchy() {

    return RoleHierarchyImpl.withRolePrefix("접두사_")
            .role("C").implies("B")
            .role("B").implies("A")
            .build();
}
```

- SecurityConfig 설정
```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

    http
            .csrf((auth) -> auth.disable());

    http
            .authorizeHttpRequests((auth) -> auth
                    .requestMatchers("/login").permitAll()
                    .requestMatchers("/").hasAnyRole("A")
                    .requestMatchers("/manager").hasAnyRole("B")
                    .requestMatchers("/admin").hasAnyRole("C")
                    .anyRequest().authenticated()
            );

    http
            .formLogin((auth) -> auth.loginPage("/login")
                    .loginProcessingUrl("/loginProc")
                    .permitAll()
            );

    return http.build();
}
```