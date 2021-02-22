package ru.bigint.emulator;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;

/**
 * Генерация карты для эмулятора Сервера
 * */
@Component
public class AreaData {

    private final int size = 3500;
    private final int depth = 10;

    public int getDepth() {
        return depth;
    }

    private String[][][] area = new String[size][size][depth];

    public String[][][] getArea() {
        return area;
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                Random rnd = new Random();
                int amount = rnd.nextInt(10);

                for (int k = 0; k < amount; k++) {
                    area[i][j][k] = "s" + k;
                }



                //этажи с монетами
//                area[i][j][0] = "s1";
//                area[i][j][1] = "s2";
//                area[i][j][2] = "s3";
            }
        }

        System.out.println(" --- create some area --- ");
    }
}
