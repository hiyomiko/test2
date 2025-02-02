import com.github.mygreen.supercsv.annotation.CsvBean;
import com.github.mygreen.supercsv.annotation.CsvColumn;
import com.github.mygreen.supercsv.annotation.constraint.CsvNumberMax;
import com.github.mygreen.supercsv.annotation.constraint.CsvNumberMin;
import com.github.mygreen.supercsv.annotation.constraint.CsvRequire;

@CsvBean(header = true) // ヘッダー行がある場合
public class CsvData {

    @CsvColumn(number = 1, label = "氏名")
    @CsvRequire // 必須項目
    public String name;

    @CsvColumn(number = 2, label = "年齢")
    @CsvNumberMin(value = "0") // 0以上の数値
    @CsvNumberMax(value = "150") // 150以下の数値
    public int age;

    // ... 他のフィールドも同様に定義
}