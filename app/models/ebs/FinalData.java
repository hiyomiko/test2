package models.ebs;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class FinalData extends Model {

    public String relationId; // 関連付け用ID

    public String name; // 氏名 (csv_import_data から取得)
    public Integer age; // 年齢 (csv_import_data から取得)
    public String email; // メールアドレス (csv_import_data から取得)
    public String phoneNumber; // 電話番号 (csv_import_data から取得)
    public String address; // 住所 (csv_import_data から取得)

    public String userId; // 外部システムから取得するID (user_data から取得)
    public String department; // 部署名 (user_data から取得)
    public String position; // 役職 (user_data から取得)

    // 必要に応じて他のフィールドを追加
    // 例:
    // public Date hireDate;

    // @ManyToOne // 外部キー制約がないため、アノテーションは不要
    // public CsvImportData csvImportData;

    // @ManyToOne // 外部キー制約がないため、アノテーションは不要
    // public UserData userData;
}