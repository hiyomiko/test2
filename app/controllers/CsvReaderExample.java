package controllers;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.supercsv.io.CsvListReader; // これが必要
import org.supercsv.prefs.CsvPreference; // これも必要

import play.mvc.Controller;

public class CsvReaderExample extends Controller {
    public void readCsv() {
        String filePath = "sample.csv"; // CSVファイルのパス
        try (CsvListReader listReader = new CsvListReader(new FileReader(filePath), CsvPreference.STANDARD_PREFERENCE)) {
            List<String> row;
            while ((row = listReader.read()) != null) {
                // 行データの処理
                System.out.println("名前: " + row.get(0) + ", 年齢: " + row.get(1) + ", メール: " + row.get(2));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
