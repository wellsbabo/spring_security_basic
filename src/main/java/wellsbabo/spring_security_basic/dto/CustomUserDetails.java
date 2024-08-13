package wellsbabo.spring_security_basic.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import wellsbabo.spring_security_basic.entity.UserEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final UserEntity userEntity;

    // 사용자의 권한 리턴 (role 컬럼 값)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userEntity.getRole();
            }
        });

        return collection;
    }

    // 사용자의 비밀번호 리턴
    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    // 사용자의 username 리턴
    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }


    /*
    아래 4개는 현재 프로젝트에서는 DB 회원가입할 때 넣어주지 않았기 때문에 우선 강제로 true를 반환하게 설정
    설정하고자하면 DB에 필드값을 추가해서 그 값을 가져오면 됨
     */

    // 사용자의 계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
//        return UserDetails.super.isAccountNonExpired();
        return true;
    }

    // 사용자의 계정 잠금 여부
    @Override
    public boolean isAccountNonLocked() {
//        return UserDetails.super.isAccountNonLocked();
        return true;
    }

    // 사용자의 계정 자격증명(비밀번호) 잠금 여부
    @Override
    public boolean isCredentialsNonExpired() {
//        return UserDetails.super.isCredentialsNonExpired();
        return true;
    }

    // 사용자의 계정 사용 가능 유무
    @Override
    public boolean isEnabled() {
//        return UserDetails.super.isEnabled();
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomUserDetails that = (CustomUserDetails) o;
        return Objects.equals(userEntity, that.userEntity); // 고유 식별자(ID)를 기준으로 비교
    }

    @Override
    public int hashCode() {
        return Objects.hash(userEntity);    // 고유 식별자(ID)를 기준으로 해시코드 생성
    }
}
