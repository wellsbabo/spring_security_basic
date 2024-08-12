참고한 재생목록
- https://youtube.com/playlist?list=PLJkjrxxiBSFCKD9TRKDYn7IE96K2u3C3U&si=ywprryUfy-SDyrCg

정리된 문서 리스트
- https://www.devyummi.com/page?id=668bd2d92b88a1ef5f2be2e3

## 개요

### 인가
- 시큐리티를 의존성에 추가해놓게 되면 스프링 시큐리티가 클라이언트가 보낸 요청을 가로채서 시큐리티 필터를 통해 검증
- 해당 경로의 접근이 누구에게 열려있는지
- 로그인이 완료된 사용자인지
- 해당되는 role을 가지고 있는지

### SecurityConfig
시큐리티 설정을 위한 설정 파일
@EnableWebSecurity는 보안 설정을 커스터마이징하기 위해서는 SecurityConfig에 붙여야하는 어노테이션
기본설정을 그대로 사용할 때는 붙이지 않아도되지만 일반적으로는 붙이는게 맞다