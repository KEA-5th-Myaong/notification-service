package myaong.popolog.notificationservice.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "member-service")
public interface MemberServiceFeignClient {

    @GetMapping("/members/nickname")
    String getNicknameById(@RequestParam("memberId") Long memberId);
}
