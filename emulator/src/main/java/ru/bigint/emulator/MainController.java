package ru.bigint.emulator;

import com.fasterxml.jackson.databind.node.TextNode;
import org.springframework.web.bind.annotation.*;
import ru.bigint.emulator.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
public class MainController {

    final AreaData areaData;
//    private static AtomicInteger queryNum = new AtomicInteger(0);
    volatile int queryNum;
    volatile int licenseId;

    public MainController(AreaData areaData) {
        this.areaData = areaData;
    }

    @GetMapping("/ping/{id}")
    public String ping(@PathVariable int id) throws InterruptedException {
//        queryNum.getAndIncrement();
        synchronized (this) {
            queryNum++;
            Random rnd = new Random();
            int k = rnd.nextInt(2);
            if (k == 1) {
                System.out.println("sleep (" + k + "); res=" + queryNum + "; id=" + id);
                Thread.sleep(10);
            } else {
                System.out.println("work (" + k + "); res=" + queryNum + "; id=" + id);
            }

            return "queryNum=" + queryNum + "; id=" + id;
        }
    }


    @GetMapping("/health-check")
    public HealthCheckResponse getHealthCheck() {
        return new HealthCheckResponse( "" );
    }


    @GetMapping("/balance")
    public BalanceResponse getBalance() {
        BalanceResponse balanceResponse = new BalanceResponse();
        balanceResponse.setBalance(123);
        balanceResponse.setWallet(new int[]{1, 2});
        return balanceResponse;
    }


    @GetMapping("/licenses")
    public LicencesResponse[] getLicences() {
        List<LicencesResponse> list = new ArrayList<>();
        list.add(new LicencesResponse(1, 3, 0));
        list.add(new LicencesResponse(1, 3, 0));
        return list.toArray(new LicencesResponse[0]);
    }


    @PostMapping("/licenses")
    public LicencesResponse postLicences(@RequestBody int[] money) {
        Random rnd = new Random();
        try {
            Thread.sleep(rnd.nextInt(200));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (this) {
            licenseId++;
            return new LicencesResponse(licenseId, 3, 0);
        }
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
        Random rnd = new Random();
        try {
            Thread.sleep(rnd.nextInt(200));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new String[]{areaData.getArea()[x][y][depth-1]};
    }


    @PostMapping("/cash")
    public int[] postCash(@RequestBody TextNode treasure) {
        if (treasure.asText().equals("s1")) return new int[]{1};
        if (treasure.asText().equals("s2")) return new int[]{2};
        if (treasure.asText().equals("s3")) return new int[]{3};
        return new int[]{0};
    }

}
