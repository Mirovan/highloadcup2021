package ru.bigint.emulator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.bigint.emulator.model.*;

@RestController
public class MainController {

    final AreaData areaData;

    public MainController(AreaData areaData) {
        this.areaData = areaData;
    }

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
        return new LicencesResponse(1, 3, 0);
    }


    @PostMapping("/licences")
    public LicencesResponse postLicences(@RequestBody int[] money) {
        return new LicencesResponse(1, 3, 0);
    }


    @PostMapping("/explore")
    public ExploreResponse postExplore(@RequestBody ExploreRequest exploreRequest) {
        ExploreResponse exploreResponse = new ExploreResponse();
        exploreResponse.setArea(exploreRequest);
        int amount = 0;
        for (int i=0; i<areaData.getDepth(); i++) {
            if (areaData.getArea()[exploreRequest.getPosX()][exploreRequest.getPosY()][i] != null) {
                amount++;
            }
        }
        exploreResponse.setAmount(amount);
        return exploreResponse;
    }


    @PostMapping("/dig")
    public String[] postDig(@RequestBody DigRequest digRequest) {
        int x = digRequest.getPosX();
        int y = digRequest.getPosY();
        int depth = digRequest.getDepth();
        return new String[]{areaData.getArea()[x][y][depth]};
    }


    @PostMapping("/cash")
    public int[] postCash(@RequestBody String treasure) {
        if (treasure.equals("s1")) return new int[]{1};
        if (treasure.equals("s2")) return new int[]{2};
        if (treasure.equals("s3")) return new int[]{3};
        return new int[]{0};
    }

}
