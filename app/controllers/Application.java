package controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.FormData;
import models.ebs.ContractAccount;
import models.smart.Merchant;
import models.smart.SalesReport;
import play.Logger;
import play.data.binding.Binder;
import play.db.jpa.JPA;
import play.mvc.Controller;

public class Application extends Controller {

	public static void index() {
		Date today = new Date();
		List<Merchant> merchants = JPA.em()
				.createQuery("SELECT m FROM Merchant m " +
						"JOIN FETCH m.contractAccounts ca " +
						"WHERE ca.id.accountType = 0 " +
						"AND ca.applyStartDate <= :today AND ca.applyEndDate >= :today", Merchant.class)
				.setParameter("today", today)
				.getResultList();

		render(merchants);
	}

	public static void input() {
		render();
	}

	@SuppressWarnings("deprecation")
	public static void submit() {
        Logger.info("Request method: " + request.method);
        Logger.info("Params: " + params.all());

        if (!request.method.equals("POST")) {
            renderText("Invalid request method.");
            return;
        }

        Map<String, String[]> requestParams = params.all();
        if (requestParams != null) {
            FormData formData = new FormData();
            Binder.bind(formData, null, requestParams); // "" を指定

            System.out.println("Name: " + formData.name);
            System.out.println("Email: " + formData.email);
            System.out.println("Address: " + formData.address);

            renderText("データを受信しました: " + formData.name);
        } else {
            renderText("Request parameters are missing.");
            return;
        }
    }

	// Map<String, String[]> から Map<String, String> への変換メソッド
	private static Map<String, String> convertParams(Map<String, String[]> paramsArray) {
		if (paramsArray == null) {
			return null;
		}
		Map<String, String> params = new HashMap<>();
		for (Map.Entry<String, String[]> entry : paramsArray.entrySet()) {
			String key = entry.getKey();
			String[] values = entry.getValue();
			if (values != null && values.length > 0) {
				// 配列の最初の要素を値として使用
				params.put(key, values[0]);
			}
		}
		return params;
	}

	public static List<ContractAccount> findValidContractAccounts(Long merchantId, Date dueDate) {
		return JPA.em()
				.createQuery("SELECT ca FROM ContractAccount ca " +
						"WHERE ca.merchant.id = :merchantId " +
						"AND ca.applyStartDate <= :dueDate " +
						"AND ca.applyEndDate >= :dueDate", ContractAccount.class)
				.setParameter("merchantId", merchantId)
				.setParameter("dueDate", dueDate)
				.getResultList();
	}

	public static void findContractAccountsForReport(Long salesReportId) {
		SalesReport salesReport = SalesReport.findById(salesReportId);
		if (salesReport != null) {
			Long merchantId = salesReport.merchant.id;
			Date dueDate = salesReport.dueDate;
			List<ContractAccount> validAccounts = findValidContractAccounts(merchantId, dueDate);

			// 取得した有効な ContractAccount を処理する
			for (ContractAccount account : validAccounts) {
				play.Logger.info("有効な ContractAccount: %s", account.id.contractNo);
			}
		}
	}

	public static void your_page() {
		render();
	}
}