package models;

import play.db.jpa.Model; // JPAを使う場合
// import play.data.validation.*; // 標準アノテーション
import javax.persistence.Entity; // JPAを使う場合
import java.util.Date;
import play.data.validation.*; // 標準アノテーション

// カスタムアノテーションをインポート
import annotations.CheckByteSize;
import annotations.DateRangeCheck;

// @Entity // JPAを使う場合はコメント解除
@DateRangeCheck(startDateField = "startDate", endDateField = "endDate", message = "validation.dateRange.message")
public class MyForm extends Model { // JPAを使わないなら extends Object でも可

    @Required(message = "validation.required.name")
    @MaxSize(value = 50, message = "validation.maxSize.name")
    public String name;

    @Required(message = "validation.required.email")
    @Email(message = "validation.email.format")
    @MaxSize(value = 100) // 標準の文字数チェック
    public String email;

    @Required(message = "validation.required.description")
    @CheckByteSize(max = 1024, message = "validation.byteSize.description") // カスタムバイトサイズチェック
    public String description;

    @Required(message = "validation.required.startDate")
    @As("yyyy-MM-dd") // 日付フォーマット指定 (Binder用)
    public Date startDate;

    @Required(message = "validation.required.endDate")
    @As("yyyy-MM-dd")
    public Date endDate;

    @Min(value = 0, message = "validation.min.value1")
    public Integer value1;

    @Range(min = 1, max = 100, message = "validation.range.value2")
    public Integer value2;

    // ... 他に50個以上のフィールドを同様に追加 ...
    // 例:
    public String address1;
    @CheckByteSize(max = 255) // メッセージ省略時はデフォルトが使われる
    public String address2;
    public String zipCode;
    // ...

    // コンストラクタ (任意)
    public MyForm() {}

    // toString (デバッグ用)
    @Override
    public String toString() {
        return "MyForm [id=" + id + ", name=" + name + ", email=" + email + ", startDate=" + startDate + ", endDate=" + endDate + "]";
    }
}