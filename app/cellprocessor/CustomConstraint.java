package cellprocessor;

import java.nio.charset.Charset;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

import com.github.mygreen.supercsv.cellprocessor.ValidationCellProcessor;

// 独自の値の検証用のCellProcessor
public class CustomConstraint extends ValidationCellProcessor
        implements StringCellProcessor {

    private int max;

    public CustomConstraint(final int max) {
        super();
        checkPreconditions(max);
        this.max = max;
    }

    public CustomConstraint(final int max, final CellProcessor next) {
        super(next);
        checkPreconditions(max);
        this.max = max;
    }

    // コンストラクタで渡した独自の引数のチェック処理
    private static void checkPreconditions(final int max) {
    	 if( max <= 0 ) {
             throw new IllegalArgumentException(String.format("max length (%d) should not be <= 0", max));
         }
    }

    @Override
    public <T> T execute(final Object value, final CsvContext context) {
        if(value == null) {
            // nullの場合、次の処理に委譲します。
            return next.execute(value, context);
        }

        final String result;
        if(value instanceof String) {
            result = (String)value;

        } else {
            // 検証対象のクラスタイプが不正な場合
            throw new SuperCsvCellProcessorException(String.class, value, context, this);
        }

        byte[] bytes = result.getBytes(Charset.forName("SJIS"));
        int length = bytes.length;
        if( length > max ) {
            throw createValidationException(context)
                .messageFormat("the length (%d) of value '%s' does not lie the max (%d) values (inclusive)",
                        length, result, max)
                .rejectedValue(result)
                .messageVariables("max", getMax())
                .messageVariables("length", length)
                .build();
                
        }
        return next.execute(result, context);

    }
    
    /**
     * 
     * @return 設定された最大文字長を取得する
     */
    public int getMax() {
        return max;
    }


}