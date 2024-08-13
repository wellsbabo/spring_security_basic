package wellsbabo.spring_security_basic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import wellsbabo.spring_security_basic.dto.JoinDTO;
import wellsbabo.spring_security_basic.service.JoinService;

@Controller
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @GetMapping("/join")
    public String joinP(){
        return "join";
    }

    @PostMapping("/joinProc")
    public String joinProcess(JoinDTO joinDTO){

        System.out.println("joinDTO = " + joinDTO);

        joinService.joinProcess(joinDTO);

        return "redirect:/login";
    }
}
