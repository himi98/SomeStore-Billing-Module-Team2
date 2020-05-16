package main.java.com.capstore.app.signup_login;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import main.java.com.capstore.app.models.User;


@Repository("confirmationTokenRepository")
public class ConfirmationTokenRepository {
	
	@Autowired
	@PersistenceContext
	EntityManager entityManager;
	
	public ConfirmationToken findByConfirmationToken(String confirmationToken)
	{
		Query q2 = entityManager
				.createNativeQuery("SELECT ct from ConfirmationToken ct where ct.confirmationToken="+confirmationToken);
		ConfirmationToken confToken = (ConfirmationToken)q2.getSingleResult();
		return confToken;
	}
	
	public void save(ConfirmationToken confirmationToken) {
		entityManager.persist(confirmationToken);
	}
	
}
