package dto;

import com.github.mygreen.supercsv.annotation.CsvBean;
import com.github.mygreen.supercsv.annotation.CsvColumn;
import com.github.mygreen.supercsv.annotation.constraint.CsvPattern;
import com.github.mygreen.supercsv.annotation.constraint.CsvRequire;

import play.db.jpa.Model;

@CsvBean(header = true)
public class CsvImportData extends Model {

    @CsvColumn(number = 1, label = "関連付けID")
    @CsvRequire
    public String relationId;

    @CsvColumn(number = 2, label = "氏名")
    @CsvRequire
    public String name;

    @CsvColumn(number = 3, label = "年齢")
    public Integer age;

    @CsvColumn(number = 4, label = "メールアドレス")
    public String email;

    @CsvColumn(number = 5, label = "電話番号")
    @CsvPattern(regex = "^0\\d{2,3}-\\d{2,4}-\\d{4}$") // 電話番号の正規表現
    public String phoneNumber;

    @CsvColumn(number = 6, label = "住所")
    public String address;

    // 必要に応じて他のフィールドを追加
    // 例:
    // @CsvColumn(number = 7, label = "部署")
    // public String department;

    private String errorMessage; // エラーメッセージを格納するフィールド

    public boolean hasError() {
        return errorMessage != null;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}