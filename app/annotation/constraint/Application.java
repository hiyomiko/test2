package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;
import models.MyNewFieldsForm; // 新項目用モデル
import models.YourFullEntity; // ★ 既存項目を含む完全なJPAエンティティ (仮名)
import java.util.*;

public class Application extends Controller {

    // フォーム表示メソッド (変更が必要な場合)
    // ビューには既存項目用の変数と、新しい項目用の MyNewFieldsForm オブジェクトを渡す
    public static void showForm(Long entityId) {
        MyNewFieldsForm newFields = new MyNewFieldsForm();
        String name = null;
        String email = null;
        // ... 他の既存項目 ...

        if (entityId != null) {
            YourFullEntity entity = YourFullEntity.findById(entityId);
            if (entity == null) notFound();
            // 既存項目
            name = entity.name;
            email = entity.email;
            // ...
            // 新項目をDTOにコピー
            newFields.description = entity.description;
            newFields.startDate = entity.startDate;
            newFields.endDate = entity.endDate;
            // ...
        }
        render(entityId, name, email, /*...,*/ newFields);
    }


    /**
     * フォーム送信処理
     * @param entityId 更新対象のID (nullなら新規)
     * @param name 既存項目
     * @param email 既存項目
     * @param // ... 他の既存引数 ...
     * @param newFields 新しい項目群 (アノテーションはあるが@Validなし)
     */
    public static void submitForm(
        Long entityId, // パスやhiddenから取得
        String name,
        String email,
        // ... 他の既存引数 ...
        MyNewFieldsForm newFields // 新項目をバインド (@Validなし)
    ) {
        Logger.info("Submit received. entityId=%d, name=%s, email=%s, newFields=%s", entityId, name, email, newFields);

        // --- 1. 事前処理 (例: 権限チェック) ---
        if (!checkUserPermission(entityId /*, session, etc */)) {
            forbidden("操作権限がありません。");
        }
        Logger.info("Permission check passed.");

        // --- 2. バリデーション (手動実行) ---
        // 2a. 既存引数のバリデーション
        Validation.required("name", name).message("validation.required.name");
        Validation.maxSize("name", name, 50).message("validation.maxSize.name", 50);
        Validation.required("email", email).message("validation.required.email");
        Validation.email("email", email).message("validation.email.format");
        // ... 他の既存引数のチェック ...

        // 2b. 新しい項目のアノテーションベースバリデーション
        if (newFields != null) {
            Validation.valid(newFields); // newFieldsのアノテーションをチェック
        } else {
            // 通常 newFields は null にならないはず (バインディングエラーは別の問題)
            // フォームの name 属性が `newFields.description` 等になっているか確認
            validation.addError("newFields", "新規項目データの取得に失敗しました。");
             Logger.error("MyNewFieldsForm object is null. Check form field names prefix (e.g., newFields.description).");
        }

        // --- 3. バリデーション結果の確認 ---
        if (validation.hasErrors()) {
            Logger.warn("Validation errors occurred: %s", validation.errorsMap());
            params.flash(); // 既存引数も newFields.* もflashスコープへ
            validation.keep(); // エラー情報をflashスコープへ

            // エラー時にフォームを再表示するために必要なデータを準備
            // paramsから取得するか、引数の値をそのまま使う
            renderArgs.put("entityId", entityId);
            renderArgs.put("name", params.get("name")); // flashから取得
            renderArgs.put("email", params.get("email"));// flashから取得
            // ... 他の既存項目も同様 ...
            // newFieldsオブジェクトもflashから復元されるはずだが、明示的に渡す
            renderArgs.put("newFields", newFields);
            renderTemplate("Application/showForm.html"); // テンプレートを再描画
        } else {
            // --- 4. バリデーション成功時の処理 ---
            Logger.info("Validation successful.");
            try {
                YourFullEntity entityToSave;
                if (entityId == null) {
                    // 新規登録
                    entityToSave = new YourFullEntity();
                    Logger.info("Creating new entity.");
                } else {
                    // 更新
                    entityToSave = YourFullEntity.findById(entityId);
                    if (entityToSave == null) {
                        notFound("Update target ID " + entityId + " not found.");
                    }
                    Logger.info("Updating entity with ID: %d", entityId);
                }

                // データをエンティティにマージ
                // 既存項目
                entityToSave.name = name;
                entityToSave.email = email;
                // ...

                // 新項目 (newFieldsからコピー)
                entityToSave.description = newFields.description;
                entityToSave.startDate = newFields.startDate;
                entityToSave.endDate = newFields.endDate;
                entityToSave.value1 = newFields.value1;
                entityToSave.value2 = newFields.value2;
                // ...

                // 保存
                entityToSave.save();
                Logger.info("Entity saved/updated successfully. ID: %d", entityToSave.id);
                flash.success(entityId == null ? "登録しました。" : "更新しました。");

                // 成功後、編集フォームを再表示
                showForm(entityToSave.id);

            } catch (Exception e) {
                Logger.error(e, "Error saving data for entityId: %s", entityId);
                flash.error("保存中にエラーが発生しました: %s", e.getMessage());
                params.flash();
                validation.keep(); // DBエラーなどでもメッセージ表示のため
                 // エラー時もフォーム再表示
                renderArgs.put("entityId", entityId);
                renderArgs.put("name", params.get("name"));
                renderArgs.put("email", params.get("email"));
                // ...
                renderArgs.put("newFields", newFields);
                renderTemplate("Application/showForm.html");
            }
        }
    }

    // ダミーの権限チェック
    private static boolean checkUserPermission(Long entityId) {
        // ここに実際の権限チェックロジック
        return true;
    }

    // ダミーの完全なエンティティ (実際には app/models に)
    // @javax.persistence.Entity
    public static class YourFullEntity extends play.db.jpa.Model {
        @Required public String name;
        @Email public String email;
        // ... 他の既存フィールド ...
        public String description;
        public Date startDate;
        public Date endDate;
        public Integer value1;
        public Integer value2;
        // ... 他の新規フィールド ...
    }
}
