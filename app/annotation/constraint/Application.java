package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*; // Validation をインポート
import models.MyForm; // モデルをインポート
import java.util.*;

public class Application extends Controller {

    public static void index() {
        // 初期表示時は新規フォームへリダイレクトなど
        showForm(null);
    }

    // フォーム表示 (新規・編集兼用)
    public static void showForm(Long id) {
        MyForm myForm = null;
        if (id != null) {
            // JPAを使う場合: データベースから取得
            myForm = MyForm.findById(id);
            if (myForm == null) {
                notFound("ID " + id + " not found.");
            }
            Logger.info("Editing form for ID: %d", id);
        } else {
            // 新規作成の場合
            myForm = new MyForm();
             Logger.info("Showing new form");
        }
        // フォームオブジェクトをビューに渡す
        render(myForm);
    }

    // フォーム送信処理
    public static void submitForm(@Valid MyForm myForm) { // @Validでバリデーション実行

        Logger.info("Submitting form data: %s", myForm);

        // バリデーションエラーチェック
        if (validation.hasErrors()) {
            Logger.warn("Validation errors occurred: %s", validation.errorsMap());
            // エラーがある場合、入力値を保持してフォームを再表示
            params.flash(); // 入力値をFlashスコープに保持
            validation.keep(); // エラーメッセージをFlashスコープに保持
            // showFormを直接呼ぶのではなく、showFormテンプレートを描画する
            render("@showForm", myForm); // エラーのあったオブジェクトを渡して再描画
        } else {
            // バリデーション成功
            Logger.info("Validation successful.");
            try {
                if (myForm.id == null) {
                    // 新規登録処理
                    // JPAを使う場合: myForm.save();
                    myForm.save(); // JPA Modelのsaveメソッド
                    flash.success("データが正常に登録されました。 ID: %d", myForm.id);
                     Logger.info("Successfully created new record with ID: %d", myForm.id);
                } else {
                    // 更新処理
                    // JPAを使う場合:
                    // findById で永続化されたエンティティを取得し、
                    // myForm (画面からのデータ) の内容をコピーして save() するのが安全
                    // MyForm dbForm = MyForm.findById(myForm.id);
                    // if (dbForm != null) {
                    //    dbForm.name = myForm.name; // 各フィールドをコピー
                    //    dbForm.email = myForm.email;
                    //    dbForm.description = myForm.description;
                    //    dbForm.startDate = myForm.startDate;
                    //    dbForm.endDate = myForm.endDate;
                    //    // ... 他のフィールドも同様に ...
                    //    dbForm.save();
                    // } else {
                    //    notFound("Update target ID " + myForm.id + " not found.");
                    // }

                    // ModelクラスがJPAエンティティなら、merge() や save() で更新できる
                    // (Detached状態からのマージが必要な場合がある)
                    // Play1 の save() は id があれば update になる
                    myForm.save();
                    flash.success("データが正常に更新されました。 ID: %d", myForm.id);
                     Logger.info("Successfully updated record with ID: %d", myForm.id);
                }
                 // 成功したら詳細表示画面や一覧画面にリダイレクトするのが一般的
                 // ここでは再度フォームを表示する（編集モード）
                 showForm(myForm.id);

            } catch (Exception e) {
                 Logger.error(e, "Error saving form data for ID: %s", myForm.id);
                 flash.error("データの保存中にエラーが発生しました: %s", e.getMessage());
                 // エラー発生時もフォームを再表示
                 params.flash();
                 validation.keep(); // エラー情報を保持（DBエラーなど）
                 render("@showForm", myForm);
            }
        }
    }
}