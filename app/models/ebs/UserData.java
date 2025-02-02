package models.ebs;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class UserData extends Model {

    public String relationId; // 関連付け用ID
    public String userId; // 外部システムから取得するID
    public String department;
    public String position;

    // 必要に応じて他のフィールドを追加
    // 例:
    // public Date hireDate;
}