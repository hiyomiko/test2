package service;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import models.smart.Merchant;

public class MerchantService {

    
	private final EntityManager entityManager;
	
    @Inject
    public MerchantService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Merchant> getMerchantAndContractInfo() {
        String jpql = "SELECT m, c FROM smartora.Merchant m JOIN ebsora.CntContractAccount c ON m.merchant_no = c.merchant_id";
        Query query = entityManager.createQuery(jpql,Merchant.class);
        return query.getResultList();
    }
}

