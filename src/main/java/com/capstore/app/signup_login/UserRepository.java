package main.java.com.capstore.app.signup_login;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import main.java.com.capstore.app.models.CustomerDetails;
import main.java.com.capstore.app.models.MerchantDetails;
import main.java.com.capstore.app.models.User;

@Repository("userRepository")
public class UserRepository {

	@Autowired
	@PersistenceContext
	EntityManager entityManager;
	
	public CustomerDetails findCustomerByEmailIgnoreCase(String email)
	{
		Query q2 = entityManager
				.createNativeQuery("SELECT cd from CustomerDetails cd where cd.email="+email);
		CustomerDetails cd = (CustomerDetails)q2.getSingleResult();
		return cd;
	}
	
	public MerchantDetails findMerchantByEmailIgnoreCase(String email)
	{
		Query q2 = entityManager
				.createNativeQuery("SELECT md from MerchantDetails md wheremd.email="+email);
		MerchantDetails md = (MerchantDetails)q2.getSingleResult();
		return md;
	}
	
	public void saveCustomer(CustomerDetails cd) {
		entityManager.persist(cd);
	}
	
	public void saveMerchant(MerchantDetails md) {
		entityManager.persist(md);
	}

}
