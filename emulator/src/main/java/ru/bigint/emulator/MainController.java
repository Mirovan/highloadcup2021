package ru.bigint.emulator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.bigint.emulator.model.*;

@RestController
public class MainController {

    @GetMapping("/health-check")
    public HealthCheckResponse getHealthCheck() {
        return new HealthCheckResponse(new Object() );
    }


    @GetMapping("/balance")
    public BalanceResponse getBalance() {
        BalanceResponse balanceResponse = new BalanceResponse();
        balanceResponse.setBalance(123);
        balanceResponse.setWallet(new int[]{1, 2});
        return balanceResponse;
    }


    @GetMapping("/licences")
    public LicencesResponse getLicences() {
        return new LicencesResponse(1, 2, 3);
    }


    @PostMapping("/licences")
    public LicencesResponse postLicences(@RequestBody int[] array) {
        return new LicencesResponse(1, 2, 3);
    }


    @PostMapping("/explore")
    public ExploreResponse postExplore(@RequestBody ExploreRequest exploreRequest) {
        ExploreResponse exploreResponse = new ExploreResponse();
        exploreResponse.setArea(exploreRequest);
        exploreResponse.setAmount(123);
        return exploreResponse;
    }


    @PostMapping("/dig")
    public String[] postDig(@RequestBody DigRequest digRequest) {
        return new String[]{"string"};
    }


    @PostMapping("/cash")
    public int[] postCash(@RequestBody String string) {
        return new int[]{0};
    }

}
