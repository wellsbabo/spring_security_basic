package wellsbabo.spring_security_basic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import wellsbabo.spring_security_basic.dto.CustomUserDetails;
import wellsbabo.spring_security_basic.entity.UserEntity;
import wellsbabo.spring_security_basic.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * username으로 유저를 검증하는 로직 작성
     * @param username 사용자가 로그인을 하면 스프링 SecurityConfig가 검증을 위해서 넣어줌
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userData = userRepository.findByUsername(username);

        // 검증결과 해당하는 유저가 존재하면 userDetails를 리턴시켜준다
        if(userData != null){
            return new CustomUserDetails(userData);
        }

        // 해당계정이 없으면 null 넘겨준다
        return null;
    }
}
