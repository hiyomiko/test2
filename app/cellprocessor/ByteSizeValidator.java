package cellprocessor;

import java.nio.charset.StandardCharsets;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

import annotation.constraint.ByteSize;

public class ByteSizeValidator extends CellProcessorAdaptor {

    private final int maxBytes;
    private final String message;

    /**
     * チェーン中間用コンストラクタ
     *
     * @param annotation アノテーション {@link ByteSize} から設定値を取得
     * @param next       次の CellProcessor
     */
    public ByteSizeValidator(ByteSize annotation, CellProcessor next) {
        super(next);
        this.maxBytes = annotation.max();
        this.message = annotation.message();
    }
    
    /**
     * チェーン最終用コンストラクタ
     *
     * @param annotation アノテーション {@link ByteSize} から設定値を取得
     */
    public ByteSizeValidator(ByteSize annotation) {
        super();
        this.maxBytes = annotation.max();
        this.message = annotation.message();
    }
    
    @Override
    public Object execute(Object value, CsvContext context) {
        // 値が null の場合はチェーン先に処理を委譲
        if (value == null) {
            return next.execute(value, context);
        }
        
        // 値を文字列に変換し、UTF-8 でのバイト長を取得
        String str = value.toString();
        int byteLength = str.getBytes(StandardCharsets.UTF_8).length;
        
        // バイト数が上限を超えている場合は例外をスロー
        if (byteLength > maxBytes) {
            throw new SuperCsvCellProcessorException(
                    message + " (許容:" + maxBytes + "バイト, 実際:" + byteLength + "バイト)",
                    context, this);
        }
        
        // 次の CellProcessor に処理を委譲
        return next.execute(value, context);
    }
}