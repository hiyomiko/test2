package validator;

import java.io.UnsupportedEncodingException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ByteSizeValidator implements ConstraintValidator<ByteSize, String> {

  private int max;
  private int min;
  private String charset;

  @Override
  public void initialize(ByteSize constraintAnnotation) {
    this.max = constraintAnnotation.max();
    this.min = constraintAnnotation.min();
    // 文字コードをSJISに設定
    this.charset = constraintAnnotation.charset();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
     if (value == null) {
       return true;  // nullの場合はチェックしない
    }
    
    try{
        byte[] bytes = value.getBytes(charset);
        int length = bytes.length;
        
       return length >= min && length <= max;
        
    }catch(UnsupportedEncodingException e){
        return false; // サポートされていない文字コード
    }
  }
}
