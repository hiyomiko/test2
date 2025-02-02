package validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = ByteSizeValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD}) // フィールドとメソッドに適用
@Retention(RetentionPolicy.RUNTIME)
public @interface ByteSize {

    String message() default "{com.example.validation.ByteSize.message}"; // デフォルトのエラーメッセージ

    Class<?>[] groups() default {}; // グループ指定

    Class<? extends Payload>[] payload() default {}; // ペイロード指定

    int max() default Integer.MAX_VALUE;   // 最大バイト数

    int min() default 0;  // 最小バイト数

    String charset() default "UTF-8"; // エンコード文字コード
}