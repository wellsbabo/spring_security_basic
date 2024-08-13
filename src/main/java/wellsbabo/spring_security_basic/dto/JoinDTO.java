package wellsbabo.spring_security_basic.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class JoinDTO {
    private String username;
    private String password;
}
