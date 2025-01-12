package controllers;

import java.util.Date;
import java.util.List;

import models.ebs.ContractAccount;
import models.ebs.ContractAccountId;
import models.smart.DueDateCalendar;
import models.smart.DueDateCode;
import models.smart.Merchant;
import models.smart.SalesReport;
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

	 public static void createSampleData() {
	        // Merchant のサンプルデータ
	        DueDateCode dueCode1 = new DueDateCode("A", "月末締め");
	        DueDateCode dueCode2 = new DueDateCode("B", "翌月15日締め");
	        dueCode1.save();
	        dueCode2.save();

	        Merchant merchant1 = new Merchant("M001", "店舗A", dueCode1);
	        Merchant merchant2 = new Merchant("M002", "店舗B", dueCode2);
	        merchant1.save();
	        merchant2.save();

	        // ContractAccount のサンプルデータ
	        Date today = new Date();
	        Date future = new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000); // 1年後
	        Date past = new Date(System.currentTimeMillis() - 365L * 24 * 60 * 60 * 1000); // 1年前

	        ContractAccount account1 = new ContractAccount(new ContractAccountId("C001", 0), merchant1, 12345, today, future);
	        ContractAccount account2 = new ContractAccount(new ContractAccountId("C002", 1), merchant1, 67890, today, future);
	        ContractAccount account3 = new ContractAccount(new ContractAccountId("C003", 0), merchant2, 98765, past, today); // 過去に有効
	        account1.save();
	        account2.save();
	        account3.save();

	        // SalesReport のサンプルデータ
	        SalesReport report1 = new SalesReport(merchant1, today, 1000.0);
	        SalesReport report2 = new SalesReport(merchant1, past, 500.0);
	        SalesReport report3 = new SalesReport(merchant2, today, 1500.0);
	        report1.save();
	        report2.save();
	        report3.save();

	        // DueDateCalendar のサンプルデータ
	        DueDateCalendar calendar1 = new DueDateCalendar(dueCode1, "A", today, future);
	        DueDateCalendar calendar2 = new DueDateCalendar(dueCode2, "B", past, today);
	        calendar1.save();
	        calendar2.save();

	        renderText("サンプルデータを作成しました。");
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
}