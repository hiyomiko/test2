package cellprocessor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.mygreen.supercsv.annotation.constraint.CsvConstraint;
import com.github.mygreen.supercsv.builder.BuildCase;


// 独自の値の検証用のアノテーション
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(CsvCustomConstraint.List.class)
@CsvConstraint(CustomConstraintFactory.class)  // ファクトリクラスを指定
public @interface CsvCustomConstraint {
	
	String message() default "{com.github.mygreen.supercsv.annotation.constraint.ByteSize.message}";
	
    // 共通の属性 - ケース
    BuildCase[] cases() default {};

    // 共通の属性 - グループ
    Class<?>[] groups() default {};

    // 共通の属性 - 並び順
    int order() default 0;

    // 繰り返しのアノテーションの格納用アノテーションの定義
    @Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {

        CsvCustomConstraint[] value();
    }

	int value();
}