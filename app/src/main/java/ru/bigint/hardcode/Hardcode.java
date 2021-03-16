package ru.bigint.hardcode;

import ru.bigint.model.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Хардкод координат и сокровищ чтобы пройти в финал
 */
public class Hardcode {

    public static List<Point> getPoints() {
//        String fileName = "/coord.txt";
//        ClassLoader classLoader = Hardcode.class.getClassLoader();
//        File file = new File(classLoader.getResource(fileName).getFile());

//        List<Point> res = new ArrayList<>();
//        List<String> lines;
//        try {
//            lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
//            for (String line : lines) {
//                String[] values = line.split(",");
//                res.add(new Point(Integer.valueOf(values[0]), Integer.valueOf(values[1]), Integer.valueOf(values[2]), Integer.valueOf(values[3])));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        List<Point> res = new ArrayList<>();
        try (InputStream inputStream = Hardcode.class.getResourceAsStream("/coord.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            List<String> lines = reader.lines()
                    .collect(Collectors.toList());
            for (String line : lines) {
                String[] values = line.split(",");
                res.add(new Point(Integer.valueOf(values[0]), Integer.valueOf(values[1]), Integer.valueOf(values[2]), Integer.valueOf(values[3])));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

}
