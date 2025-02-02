package annotation.constraint;


import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import cellprocessor.ByteSizeValidator;

/**
 * バイトサイズチェック用のアノテーションです。
 * 
 * ※本サンプルでは、validator属性にバリデータクラス（ByteSizeValidator.class）を指定しています。
 * 実際の連携方法は、ライブラリのバージョンや実装によって異なるため、
 * 必要に応じてカスタムロジックでアノテーションから validator 属性を読み取り、
 * 対応するバリデータのインスタンスを生成・実行してください。
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface ByteSize {

    /**
     * 許容する最大のバイト数（UTF-8 でのバイト長）を指定します。
     */
    int max();

    /**
     * 検証エラー時に返すメッセージを指定します。
     */
    String message() default "バイトサイズが上限を超えています";

    /**
     * このアノテーションに対応するバリデータのクラスを指定します。
     * （必ずしもライブラリ側で自動的に連携されるわけではなく、
     *  アプリケーション側でこの属性を参照してインスタンス化する必要があります）
     */
    Class<?> validator() default ByteSizeValidator.class;
}