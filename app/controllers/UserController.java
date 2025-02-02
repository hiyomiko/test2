package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.supercsv.exception.SuperCsvException;
import org.supercsv.prefs.CsvPreference;

import com.github.mygreen.supercsv.io.CsvAnnotationBeanReader;
import com.github.mygreen.supercsv.localization.EncodingControl;
import com.github.mygreen.supercsv.localization.MessageResolver;
import com.github.mygreen.supercsv.localization.ResourceBundleMessageResolver;
import com.github.mygreen.supercsv.validation.CsvExceptionConverter;

import dto.CsvImportData;
import models.CsvUser;
import models.User;
import models.ebs.FinalData;
import models.ebs.UserData;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Http;

public class UserController extends Controller {

    private static final Pattern ERROR_MESSAGE_PATTERN = Pattern.compile("line: \\d+, column: \\d+, (.+)");

	public static void upload() {
		render("User/upload.html");
	}

	public static void index() {
		render();
	}

	@Transactional
	public static void processUpload() {
		Http.Request request = Http.Request.current();
		File file = request.params.get("csvFile", File.class);
		
		 // メッセージソースの設定
		 CsvExceptionConverter exceptionConverter = new CsvExceptionConverter();
		 MessageResolver testMessageResolver = new ResourceBundleMessageResolver(ResourceBundle.getBundle("SuperCsvMessages", new EncodingControl("UTF-8")));
		 exceptionConverter.setMessageResolver(testMessageResolver);
	       

		if (file != null) {
			List<String> errorMessages = new ArrayList<>();
            Set<String> uniqueErrorKeys = new HashSet<>(); // 正規化されたエラーメッセージの重複チェック用
			List<CsvUser> validUsers = new ArrayList<>();
			List<User> registUser = new ArrayList<>();

			CsvAnnotationBeanReader<CsvUser> csvReader = null;
			try {
				csvReader = new CsvAnnotationBeanReader<>(
						CsvUser.class,
						new FileReader(file),
						CsvPreference.STANDARD_PREFERENCE);
				csvReader.setExceptionConverter(exceptionConverter);
				

				// ヘッダー行の読み込み
				String[] headers = csvReader.getHeader(true);

				List<CsvUser> list = new ArrayList<>();

				// レコードの読み込み - 1行づつ
				CsvUser record = null;
				
				while (true) {
                    try {
                        record = csvReader.read();
                        if(record == null) {
                            break; // ファイルの終わりに達したらループ終了
                        }
                        list.add(record);
                    } catch (SuperCsvException e) {
                        // 変換されたエラーメッセージの取得
                        List<String> messages = csvReader.getErrorMessages();
                         for (String message : messages) {
                            String normalizedMessage = normalizeErrorMessage(message);
                            if (uniqueErrorKeys.add(normalizedMessage)) {
                                errorMessages.add(message); // オリジナルのエラーメッセージを追加
                            }
                        }
                        // エラーが発生しても次の行を読み込むためにループを継続
                    }
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

			render("User/result.html", errorMessages, validUsers);
		} else {
			flash.error("File is missing!");
			upload();
		}
	}

    /**
     * エラーメッセージを正規化するメソッド
     * @param message エラーメッセージ
     * @return 正規化されたエラーメッセージ
     */
    private static String normalizeErrorMessage(String message) {
        Matcher matcher = ERROR_MESSAGE_PATTERN.matcher(message);
        if (matcher.find()) {
            return matcher.group(1).trim(); // エラーメッセージ部分のみを返す
        }
        return message.trim(); // マッチしない場合は元のメッセージを返す
    }

	public static void importCsv(File csvFile) {
		List<String> errors = new ArrayList<>();
		List<CsvImportData> csvImportDataList = new ArrayList<>();

		CsvAnnotationBeanReader<CsvImportData> csvReader = null;
		try {

			csvReader = new CsvAnnotationBeanReader<>(
					CsvImportData.class,
					new FileReader(csvFile),
					CsvPreference.STANDARD_PREFERENCE);

			String[] header = csvReader.getHeader(true); // ヘッダー行を読み飛ばす

			// レコードの読み込み - 1行づつ
			CsvImportData record = null;
			while ((record = csvReader.read()) != null) {
				CsvImportData csvData = new CsvImportData();
				csvData.relationId = record.relationId;
				csvData.name = record.name;
				csvData.age = record.age;
				csvData.email = record.email;
				csvData.phoneNumber = record.phoneNumber;
				csvData.address = record.address;

				csvImportDataList.add(record);
			}

		} catch (Exception e) {
			errors.add("CSV ファイルの読み込みに失敗しました: " + e.getMessage());
		}

		if (errors.isEmpty()) {
			Map<String, UserData> userDataMap = new HashMap<>();
			List<UserData> userDataList = UserData.findAll();
			for (UserData userData : userDataList) {
				userDataMap.put(userData.relationId, userData);
			}

			for (CsvImportData csvData : csvImportDataList) {
				UserData userData = userDataMap.get(csvData.relationId);
				if (userData != null) {
					FinalData finalData = new FinalData();
					finalData.relationId = csvData.relationId; // 関連付けIDを設定
					finalData.name = csvData.name;
					finalData.age = csvData.age;
					finalData.email = csvData.email;
					finalData.phoneNumber = csvData.phoneNumber;
					finalData.address = csvData.address;
					finalData.userId = userData.userId;
					finalData.department = userData.department;
					finalData.position = userData.position;

					finalData.save();
				} else {
					errors.add("関連付けID " + csvData.relationId + " が見つかりません。");
				}
			}

			if (errors.isEmpty()) {
				flash.success("CSV ファイルのインポートが完了しました。");
				index();
			} else {
				render(errors);
			}
		} else {
			render(errors);
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