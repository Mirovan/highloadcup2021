package ru.bigint.emulator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.bigint.emulator.model.BalanceResponse;
import ru.bigint.emulator.model.ExploreRequest;
import ru.bigint.emulator.model.ExploreResponse;

@RestController
public class MainController {

    @GetMapping("/health-check")
    public void getHealthCheck() {
    }


    @GetMapping("/balance")
    public BalanceResponse getBalance() {
        BalanceResponse balanceResponse = new BalanceResponse();
        balanceResponse.setBalance(123);
        balanceResponse.setWallet(new int[]{1, 2});
        return balanceResponse;
    }


    @GetMapping("/licences")
    public void getLicences() {
    }


    @PostMapping("/licences")
    public void postLicences() {
    }


    @PostMapping("/explore")
    public ExploreResponse postExplore(@RequestBody ExploreRequest exploreRequest) {
        ExploreResponse exploreResponse = new ExploreResponse();
        exploreResponse.setArea(exploreRequest);
        exploreResponse.setAmount(123);
        return exploreResponse;
    }


    @PostMapping("/dig")
    public void postDig() {
    }


    @PostMapping("/cash")
    public void postCash() {
    }

}
