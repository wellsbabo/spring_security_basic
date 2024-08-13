package wellsbabo.spring_security_basic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import wellsbabo.spring_security_basic.dto.JoinDTO;
import wellsbabo.spring_security_basic.entity.UserEntity;
import wellsbabo.spring_security_basic.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;  // 회원가입시 비밀번호를 암호화하여 저장해야하기 때문에 사용

    public void joinProcess(JoinDTO joinDTO){
        
        // db에 이미 동일한 username을 가진 회원이 존재하는지 검증


        UserEntity data = new UserEntity();

        data.setUsername(joinDTO.getUsername());
        data.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword()));
        data.setRole("ROLE_USER");

        
        userRepository.save(data);
    }
}
