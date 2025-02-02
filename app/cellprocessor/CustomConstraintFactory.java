package cellprocessor;


import java.util.Optional;

import org.supercsv.cellprocessor.ift.CellProcessor;

import com.github.mygreen.supercsv.builder.Configuration;
import com.github.mygreen.supercsv.builder.FieldAccessor;
import com.github.mygreen.supercsv.cellprocessor.ConstraintProcessorFactory;
import com.github.mygreen.supercsv.cellprocessor.format.TextFormatter;
import com.github.mygreen.supercsv.exception.SuperCsvInvalidAnnotationException;
import com.github.mygreen.supercsv.localization.MessageBuilder;

public class CustomConstraintFactory implements ConstraintProcessorFactory<CsvCustomConstraint> {

    @Override
    public Optional<CellProcessor> create(CsvCustomConstraint anno, Optional<CellProcessor> next,
            FieldAccessor field, TextFormatter<?> formatter, Configuration config) {
    	
    	final int max = anno.value();

    	if(max <= 0) {
            throw new SuperCsvInvalidAnnotationException(anno, MessageBuilder.create("anno.attr.min")
                    .var("property", field.getNameWithClass())
                    .varWithAnno("anno", anno.annotationType())
                    .var("attrName", "value")
                    .var("attrValue", max)
                    .var("min", 1)
                    .format());
        }

        // CellProcessorのインスタンスを作成します
        final CustomConstraint processor = next.map(n ->  new CustomConstraint(anno.value(), n))
                .orElseGet(() -> new CustomConstraint(anno.value()));
        processor.setValidationMessage(anno.message());

        return Optional.of(processor);

    }


}