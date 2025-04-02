package site.chatmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.chatmanager.pojo.Result;
import site.chatmanager.pojo.data.UpdateData;
import site.chatmanager.service.DeactivateService;

@RestController
@RequestMapping("/api/deactivate")
public class DeactivateController {

    @Autowired
    private DeactivateService deactivateService;

    // 注销账号
    @DeleteMapping("/{uid}")
    public ResponseEntity<Result> deactivateAccount(@PathVariable("uid") Long uid, @RequestBody UpdateData data) {
        return deactivateService.deactivateAccount(uid, data);
    }
}
