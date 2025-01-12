package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.supercsv.exception.SuperCsvException;
import org.supercsv.prefs.CsvPreference;

import com.github.mygreen.supercsv.io.CsvAnnotationBeanReader;

import models.CsvUser;
import models.User;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Http;


public class UserController extends Controller {

	public static void upload() { // ここが重要
		render("User/upload.html");
    }


	@Transactional
	  public static void processUpload() {
	    Http.Request request = Http.Request.current();
	    File file = request.params.get("csvFile", File.class);

	    if (file != null) {
	      List<String> errorMessages = new ArrayList<>();
	      List<CsvUser> validUsers = new ArrayList<>();
	      List<User> registUser = new ArrayList<>();
	      CsvAnnotationBeanReader<CsvUser> beanReader = null;
	      ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
          Validator validator = factory.getValidator(); // Validatorのインスタンスを取得
          
          
			CsvAnnotationBeanReader<CsvUser> csvReader = null;
			try {
				csvReader = new CsvAnnotationBeanReader<>(
						CsvUser.class,
						new FileReader(file),
						CsvPreference.STANDARD_PREFERENCE);
				
				
				// ヘッダー行の読み込み
	            String[] headers = csvReader.getHeader(true);

	            List<CsvUser> list = new ArrayList<>();

	            // レコードの読み込み - 1行づつ
	            CsvUser record = null;
	            while((record = csvReader.read()) != null) {
	                    list.add(record);
	            }
				
			} catch (SuperCsvException e) {

				// 変換されたエラーメッセージの取得
				List<String> messages = csvReader.getErrorMessages();
				for(String item: messages) {
					errorMessages.add(item);
				}

			} catch (FileNotFoundException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} finally {
				if (csvReader != null) {
					try {
						csvReader.close();
					} catch (IOException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
				}
			}
          
//          
//			int rowNum = 1;
//			try (CsvAnnotationBeanReader<CsvUser> csvReader = new CsvAnnotationBeanReader<>(
//					CsvUser.class, new FileReader(file), CsvPreference.STANDARD_PREFERENCE);) {
//
//				// ヘッダー行の読み込み
//				String[] headers = csvReader.getHeader(true);
//
//				List<CsvUser> list = new ArrayList<>();
//
//				// レコードの読み込み - 1行づつ
//				CsvUser record = null;
//				while ((record = csvReader.read()) != null) {
//					rowNum++;
//					list.add(record);
//				}
//
//			} catch (SuperCsvNoMatchColumnSizeException e) {
//				errorMessages.add(String.format("フォーマットが不正です:(エラー詳細) %s", e.getMessage()));
//			} catch (SuperCsvException e) {
//				// Super CSVの設定などのエラー
//				
//				errorMessages.add(String.format("Row %d: %s", rowNum, e.getMessage()));
//				e.get
//
//			} catch (IOException e) {
//				// ファイルI/Oに関する例外
//				errorMessages.add(String.format("Row %d: %s", e.getMessage()));
//			}
	      
//	      try {
//	        beanReader = new CsvAnnotationBeanReader<>(CsvUser.class, new FileReader(file), CsvPreference.STANDARD_PREFERENCE);
//	        beanReader.getHeader(true); // Skip header
//
//	        CsvUser user;
//	        int rowNum = 1;
//	        while ((user = beanReader.read()) != null) {
//	          rowNum++;
//	          
//	          // Bean Validationを実行
//              Set<ConstraintViolation<CsvUser>> violations = validator.validate(user);
//
//              if (!violations.isEmpty()) {
//                  for (ConstraintViolation<CsvUser> violation : violations) {
//                      errorMessages.add(String.format("Row %d: %s (%s)", rowNum, violation.getMessage(), violation.getPropertyPath()));
//                  }
//                  continue; // バリデーションエラーがあれば次の行へ
//              }
//	          
//	          try {
//	        	  
//	        	  User userRegist = new User();
//
//                  // リフレクションを使って同名フィールドをコピー
//                  copyMatchingFields(user, userRegist);
//	            // Validation happens automatically during read
//                  registUser.add(userRegist);
//	            JPA.em().persist(userRegist); // Persist valid user
//	          } catch(SuperCsvNoMatchColumnSizeException e) {
//	        	  errorMessages.add(String.format("Row %d: %s", rowNum, e.getMessage()));  
//	          } catch (SuperCsvConstraintViolationException e) {
//	            errorMessages.add(String.format("Row %d: %s", rowNum, e.getMessage()));
//	          } catch (SuperCsvException e) {
//	            errorMessages.add(String.format("Row %d: %s", rowNum, e.getMessage()));
//	          }
//	        }
//	      } finally {
//	        if (beanReader != null) {
//	          beanReader.close();
//	        }
//	      }
	      render("User/result.html", errorMessages, validUsers);
	    } else {
	      flash.error("File is missing!");
	      upload();
	    }
	  }
	
	private static void copyMatchingFields(Object source, Object destination) throws IllegalAccessException {
        Class<?> sourceClass = source.getClass();
        Class<?> destinationClass = destination.getClass();

        for (Field sourceField : sourceClass.getDeclaredFields()) {
            for (Field destinationField : destinationClass.getDeclaredFields()) {
                if (sourceField.getName().equals(destinationField.getName()) &&
                        sourceField.getType().equals(destinationField.getType())) {
                    sourceField.setAccessible(true);
                    destinationField.setAccessible(true);
                    destinationField.set(destination, sourceField.get(source));
                    break; // 一致するフィールドが見つかったら内側のループを抜ける
                }
            }
        }
    }
}