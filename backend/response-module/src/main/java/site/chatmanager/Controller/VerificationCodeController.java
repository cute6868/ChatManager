package site.chatmanager.Controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class VerificationCodeController {

    @PostMapping("/")
    public String verificationCode() {
        return "VerificationCode";
    }

}
