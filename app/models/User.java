package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="USERS") // テーブル名を明示的に指定 (必要に応じて)
public class User extends Model {

    public String name;
    public String email;
    public int age;

    public User(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public User() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
    public String toString() {
        return "User{" +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}