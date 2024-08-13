package wellsbabo.spring_security_basic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity  // 스프링 시큐리티 설정을 위한 어노테이션
public class SecurityConfig {

    /*
    스프링 시큐리티 암호화를 위한 메서드
    이 메서드를 통해 이후 시큐리티 관련 각종 암호화를 진행
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){

        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/login","/loginProc","/join","/joinProc").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/my/**").hasAnyRole("ADMIN","USER")
                        .anyRequest().authenticated()
                );

        http
                .formLogin((auth) -> auth
                        .loginPage("/login")    // 리다이렉팅될 로그인 페이지 경로
                        .loginProcessingUrl("/loginProc")   // 시큐리티가 로그인 처리를 진행해줄 경로. 어차피 html안에 내용있으니까 될줄알았는데 이 설정 없으면 로그인 처리 안됨
                        .permitAll()    // 모두가 들어올 수 있도록 설정
                );

        http
                .csrf((auth) -> auth.disable());

        return http.build();
    }
}
