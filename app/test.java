import com.github.mygreen.supercsv.annotation.CsvBean;
import com.github.mygreen.supercsv.annotation.CsvColumn;
import com.github.mygreen.supercsv.io.CsvAnnotationBeanWriter;
import com.github.mygreen.supercsv.prefs.CsvPreference; // CsvPreference をインポート

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CSVに出力するデータのモデルクラス。
 * super-csv-annotation のアノテーションが付与されています。
 */
@CsvBean // CSV Bean であることを示すアノテーション
public class DataModel {

    // このフィールドはCSVに出力しません (国内/海外判定のみに使用)
    private boolean domestic;

    // CSVの1列目に出力。ヘッダーは "ID"
    @CsvColumn(number = 1, label = "ID")
    private int id;

    // CSVの2列目に出力。ヘッダーは "Name"
    @CsvColumn(number = 2, label = "Name")
    private String name;

    // CSVの3列目に出力。ヘッダーは "Value"
    @CsvColumn(number = 3, label = "Value")
    private String value;
    // TODO: 他のCSVに出力したいフィールドにも @CsvColumn を追加してください

    /**
     * デフォルトコンストラクタ。
     * super-csv が内部でオブジェクトを生成する際に必要になることがあります。
     */
    public DataModel() {}

    /**
     * データ設定用コンストラクタ (サンプル用)。
     * @param isDomestic 国内向けデータかどうか
     * @param id ID
     * @param name 名前
     * @param value 値
     */
    public DataModel(boolean isDomestic, int id, String name, String value) {
        this.domestic = isDomestic;
        this.id = id;
        this.name = name;
        this.value = value;
    }

    /**
     * このデータが国内向けかどうかを返します。
     * @return 国内向けの場合は true、海外向けの場合は false
     */
    public boolean isDomestic() {
        return domestic;
    }

    // --- 以下、各フィールドの public getter ---
    // super-csv がフィールドの値を取得するために必要です。

    public int getId() { return id; }
    public String getName() { return name; }
    public String getValue() { return value; }
    // TODO: 他のフィールドの getter も追加してください
}

/**
 * CSV書き込み処理を実行するサービスクラス。
 * super-csv-annotation を利用します。
 *
 * 依存ライブラリ (Maven):
 * <dependency>
 * <groupId>com.github.mygreen</groupId>
 * <artifactId>super-csv-annotation</artifactId>
 * <version>2.4</version> * </dependency>
 * <dependency>
 * <groupId>org.slf4j</groupId>
 * <artifactId>slf4j-api</artifactId>
 * <version>1.7.36</version> * </dependency>
 * <dependency>
 * <groupId>ch.qos.logback</groupId>
 * <artifactId>logback-classic</artifactId>
 * <version>1.2.11</version> * <scope>runtime</scope>
 * </dependency>
 */
public class CsvExportServiceSuperCsv {

    // 1ファイルあたりの最大レコード数
    private static final int RECORDS_PER_FILE = 10;
    // 出力ディレクトリ (環境に合わせて変更してください)
    private static final String OUTPUT_DIR = "./csv_output_supercsv/";
    // 国内向けファイル名のプレフィックス
    private static final String DOMESTIC_FILE_PREFIX = "domestic_data";
    // 海外向けファイル名のプレフィックス
    private static final String OVERSEAS_FILE_PREFIX = "overseas_data";

    /**
     * メイン処理。データの取得、分類、書き込みを実行します。
     * @param args コマンドライン引数 (未使用)
     */
    public static void main(String[] args) {
        // --- 0. DBからデータを取得 ---
        // TODO: この部分は実際のデータベースアクセス処理に置き換えてください
        List<DataModel> allData = fetchDataFromDatabase();

        // --- 1. 国内向けデータと海外向けデータに分類 ---
        List<DataModel> domesticData = allData.stream()
                .filter(DataModel::isDomestic)
                .collect(Collectors.toList());

        List<DataModel> overseasData = allData.stream()
                .filter(data -> !data.isDomestic())
                .collect(Collectors.toList());

        // --- 出力ディレクトリ作成 (存在しない場合) ---
        try {
            Files.createDirectories(Paths.get(OUTPUT_DIR));
            System.out.println("出力ディレクトリを作成または確認しました: " + OUTPUT_DIR);
        } catch (IOException e) {
            System.err.println("エラー: 出力ディレクトリの作成に失敗しました: " + e.getMessage());
            return; // ディレクトリが準備できない場合は処理を中断
        }

        // --- 2. 分類したデータをそれぞれCSVファイルに書き込む ---
        System.out.println("国内向けデータの書き込み処理を開始 (super-csv-annotation)...");
        writeGroupedCsvFilesWithSuperCsv(domesticData, DOMESTIC_FILE_PREFIX, RECORDS_PER_FILE);
        System.out.println("国内向けデータの書き込み処理が完了しました。");

        System.out.println("海外向けデータの書き込み処理を開始 (super-csv-annotation)...");
        writeGroupedCsvFilesWithSuperCsv(overseasData, OVERSEAS_FILE_PREFIX, RECORDS_PER_FILE);
        System.out.println("海外向けデータの書き込み処理が完了しました。");
    }

    /**
     * DBからデータを取得するダミーメソッド。
     * 実際にはデータベースへの接続・クエリ実行処理を実装してください。
     * @return DataModelのリスト
     */
    private static List<DataModel> fetchDataFromDatabase() {
        // --- TODO: ここに実際のDBアクセス処理を実装 ---
        // 以下はダミーデータの生成例
        List<DataModel> dataList = new ArrayList<>();
        System.out.println("データベースからデータを取得中... (ダミー)");
        for (int i = 1; i <= 25; i++) {
            // 例: IDが3の倍数なら海外、それ以外は国内
            dataList.add(new DataModel(i % 3 != 0, i, "国内名称" + i, "値" + i * 10));
        }
        for (int i = 26; i <= 55; i++) {
             // 例: IDが5の倍数なら海外、それ以外は国内
             dataList.add(new DataModel(i % 5 != 0, i, "OverseasName" + i, "Value" + i * 10));
         }
        System.out.printf("データベースから %d 件のデータを取得しました (ダミー)。%n", dataList.size());
        return dataList;
    }

    /**
     * データリストを指定された件数ごとに別々のCSVファイルに書き込むメソッド (super-csv-annotation版)。
     * @param dataList 書き込むデータのリスト (アノテーションが付与されたDataModel)
     * @param filenamePrefix ファイル名のプレフィックス (例: "domestic_data")
     * @param recordsPerFile 1ファイルあたりの最大レコード数
     */
    private static void writeGroupedCsvFilesWithSuperCsv(List<DataModel> dataList, String filenamePrefix, int recordsPerFile) {
        if (dataList == null || dataList.isEmpty()) {
            System.out.println("  書き込むデータがありません (" + filenamePrefix + ").");
            return;
        }

        int totalRecords = dataList.size();
        // 作成するファイル数を計算 (例: 25件で10件区切りなら 25/10=2.5 -> 3ファイル)
        int numberOfFiles = (int) Math.ceil((double) totalRecords / recordsPerFile);

        // CSVの書式設定 (super-csv)
        // STANDARD_PREFERENCE (ダブルクォート囲み、カンマ区切り、CRLF改行) をベースに
        // ヘッダー行を出力するように設定します。
        final CsvPreference preference = new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE)
                .useHeader(true) // ヘッダー行を出力する (@CsvColumn の label を使用)
                // 必要に応じて他の設定を追加:
                // .useQuoteMode(...) // 引用符のモード
                // .setDelimiterChar('\t') // 区切り文字をタブにする場合
                // .setEndOfLineSymbols("\n") // 改行コードをLFにする場合
                .build();

        System.out.printf("  %s 用のデータ %d 件を %d 個のファイルに分割して書き込みます...%n", filenamePrefix, totalRecords, numberOfFiles);

        // ファイルごとにループ
        for (int i = 0; i < numberOfFiles; i++) {
            // 現在のファイル番号 (1から開始)
            int filePartNumber = i + 1;
            // このファイルに書き込むデータの開始インデックス (0から)
            int startIndex = i * recordsPerFile;
            // このファイルに書き込むデータの終了インデックス (リストのサイズを超えないように)
            int endIndex = Math.min(startIndex + recordsPerFile, totalRecords);

            // 書き込むデータのサブリストを取得
            List<DataModel> subList = dataList.subList(startIndex, endIndex);

            // 出力ファイル名を生成 (例: ./csv_output_supercsv/domestic_data_part1.csv)
            String filename = String.format("%s%s_part%d.csv", OUTPUT_DIR, filenamePrefix, filePartNumber);

            // try-with-resources で CsvAnnotationBeanWriter と Writer を確実にクローズ
            try (
                // Writer を生成 (文字コードは UTF-8、ファイルが存在すれば上書き)
                BufferedWriter writer = Files.newBufferedWriter(
                        Paths.get(filename),
                        StandardCharsets.UTF_8, // 文字コード指定 (重要)
                        StandardOpenOption.CREATE, // ファイルがなければ作成
                        StandardOpenOption.WRITE,  // 書き込みモード
                        StandardOpenOption.TRUNCATE_EXISTING // 存在すれば上書き
                 );
                 // CsvAnnotationBeanWriter を生成
                 CsvAnnotationBeanWriter<DataModel> beanWriter = new CsvAnnotationBeanWriter<>(
                         DataModel.class, // アノテーションが付与されたBeanクラス
                         writer,          // 生成したWriterオブジェクト
                         preference       // 上で定義したCSV書式設定
                 )
            ) {
                System.out.printf("    ファイル書き込み中: %s (%d件)%n", filename, subList.size());

                // ヘッダーを書き込む (CsvPreference で useHeader(true) にした場合)
                // ヘッダー名は DataModel の @CsvColumn アノテーションの label 属性から取得されます
                beanWriter.writeHeader();

                // データリストを書き込む (Bean のリストを渡すだけ)
                // アノテーションに基づいて自動的にCSV形式に変換されます
                beanWriter.writeAll(subList);

                beanWriter.flush(); // バッファの内容をファイルに確実に書き出す
                System.out.printf("    ファイル書き込み完了: %s%n", filename);

            } catch (IOException e) {
                // ファイルI/O関連のエラー
                System.err.printf("エラー: ファイル書き込み中にI/Oエラーが発生しました (%s): %s%n", filename, e.getMessage());
                // 必要に応じて、より詳細なエラー処理（ログ記録、リトライなど）を追加
            } catch (Exception e) {
                // super-csv がスローする可能性のある他の実行時例外など
                // (例: アノテーション設定の不備、データ型の不整合など)
                System.err.printf("エラー: ファイル書き込み中に予期せぬエラーが発生しました (%s): %s%n", filename, e.getMessage());
                e.printStackTrace(); // 開発中はスタックトレースを出力して原因を特定
            }
        }
    }
}
