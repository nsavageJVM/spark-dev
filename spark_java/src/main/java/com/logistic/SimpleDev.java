package com.logistic;

import com.logistic.artifacts.HashValueBag;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ubu on 19.10.15.
 */
public class SimpleDev {

    public static void main(String[] args) {

      try( Stream<String> lines = Files.lines(Paths.get(SimpleDev.class.getResource("/ComercialBanks10k.csv").toURI()))) {

          List<String> listLines = lines.collect(Collectors.toList());

          listLines.forEach(s -> {
              HashValueBag hashBag = new HashValueBag(s, s.hashCode());
              System.out.println(hashBag);

          });


      } catch (URISyntaxException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      }


    }
}
