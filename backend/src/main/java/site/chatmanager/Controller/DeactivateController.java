package site.chatmanager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.chatmanager.pojo.Result;
import site.chatmanager.service.DeactivateService;

@RestController
@RequestMapping("/api/deactivate")
public class DeactivateController {

    @Autowired
    private DeactivateService deactivateService;

    // 注销账号
    @DeleteMapping("/{uid}")
    public ResponseEntity<Result> deactivateAccount(@PathVariable("uid") String uid) {
        return deactivateService.deactivateAccount(uid);
    }
}
