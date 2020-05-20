package main.java.com.capstore.app.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import main.java.com.capstore.app.models.CustomerDetails;
import main.java.com.capstore.app.models.MerchantDetails;
import main.java.com.capstore.app.models.Product;
import main.java.com.capstore.app.repository.ConfirmationTokenRepository;
import main.java.com.capstore.app.repository.ProductServiceImpl;
import main.java.com.capstore.app.repository.UserRepository;
import main.java.com.capstore.app.signup_login.ConfirmationToken;
import main.java.com.capstore.app.signup_login.EmailSenderService;
import lombok.Data;

@Data
@Transactional
@RestController
@CrossOrigin(origins = "http://localhost:4200",allowedHeaders = "*")
public class AppController {
	@Autowired
	ProductServiceImpl productServiceImpl;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ConfirmationTokenRepository confirmationTokenRepository;
	
	@Autowired
	EmailSenderService emailSenderService;

    @RequestMapping(value="/registerCustomer", method = RequestMethod.POST)
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody CustomerDetails cd)
    {
        CustomerDetails existingCustomer = userRepository.findCustomerByEmailIgnoreCase(cd.getEmail());
        if(existingCustomer != null)
        {
            return new ResponseEntity<Error>(HttpStatus.CONFLICT);
        }
        else
        {
            userRepository.saveCustomer(cd);
            CustomerDetails cd1=userRepository.findCustomerByEmailIgnoreCase(cd.getEmail());
            
            ConfirmationToken confirmationToken = new ConfirmationToken(cd1.getUserId());
            System.out.println(confirmationToken);

            confirmationTokenRepository.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(cd.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("capstore06@gmail.com");
            mailMessage.setText("To activate your account, please click here : "
            +"http://localhost:4200/verify?token="+confirmationToken.getConfirmationToken());

            emailSenderService.sendEmail(mailMessage);
            
            return ResponseEntity.ok(HttpStatus.OK);
        }
    }

    @RequestMapping(value="/registerMerchant", method = RequestMethod.POST)
    public ResponseEntity<?> registerMerchant(@Valid @RequestBody MerchantDetails md)
    {

        MerchantDetails existingMerchant = userRepository.findMerchantByEmailIgnoreCase(md.getEmail());
        if(existingMerchant != null)
        {
            return new ResponseEntity<Error>(HttpStatus.CONFLICT);
        }
        else
        {
            userRepository.saveMerchant(md);
            MerchantDetails md1=userRepository.findMerchantByEmailIgnoreCase(md.getEmail());

            ConfirmationToken confirmationToken = new ConfirmationToken(md1.getUserId());

            confirmationTokenRepository.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo("dsonaje6@gmail.com");
            mailMessage.setSubject("Merchant Requesting Approval!");
            mailMessage.setFrom("capstore06@gmail.com");
            mailMessage.setText("To provide Approval, please click here : "
            +"http://localhost:8080/Billing-App/confirm-account?token="+confirmationToken.getConfirmationToken());

            emailSenderService.sendEmail(mailMessage);

            return ResponseEntity.ok(HttpStatus.OK);
        }
    }
    
    
    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> confirmUserAccount(@Valid  @RequestParam("token") String confirmationToken)
    {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
            if(userRepository.findCustomerById(token.getUid())!=null) {
            	CustomerDetails cd=userRepository.findCustomerById(token.getUid());
            	cd.setActive(true);
                userRepository.saveCustomer(cd);
            }
            else if(userRepository.findMerchantById(token.getUid())!=null) {
            	MerchantDetails md=userRepository.findMerchantById(token.getUid());
            	md.setActive(true);
            	md.setApproved(true);
                userRepository.saveMerchant(md);
                
                SimpleMailMessage mailMessage1 = new SimpleMailMessage();
                mailMessage1.setTo(md.getEmail());
                mailMessage1.setSubject("Account Activated!");
                mailMessage1.setFrom("capstore06@gmail.com");
                mailMessage1.setText("Admin approved your account.\nTo login and access your account, please click here : "
                +"http://localhost:4200");

                emailSenderService.sendEmail(mailMessage1);
            }
            
            return ResponseEntity.ok(HttpStatus.OK);
        }
        else
        {
        	return new ResponseEntity<Error>(HttpStatus.CONFLICT);
        }
    }
    
    @RequestMapping(value="/login", method= {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> userLogin(@Valid @RequestBody String[] userCredentials) {
    	String email=userCredentials[0];
    	String pass=userCredentials[1];
    	String role=userCredentials[2];
    	System.out.println(email+pass+role);
    	if (role.equals("Customer")) {
    		CustomerDetails cd=userRepository.findCustomerByEmailIgnoreCase(email);
    		if(cd!=null && cd.isActive()==true) {
    			if(pass.equals(cd.getPassword())) {
    				return ResponseEntity.ok().body(cd);
    			}
    		}
    	}
    	else {
    		MerchantDetails md=userRepository.findMerchantByEmailIgnoreCase(email);
    		if(md!=null && md.isActive()==true) {
    			if(pass.equals(md.getPassword())) {
    				return ResponseEntity.ok().body(md);
    			}
    		}
    	}
    	return new ResponseEntity<Error>(HttpStatus.CONFLICT);
    }
    
  //All Products Data
  	@GetMapping(value="/allProducts")
  	public List<Product> getAllProducts(){
  		return productServiceImpl.allProductsList();
  	}
  	
  	
  	//Products data of particular category
  	@GetMapping(value="/productCategory/{category}")
  	public List<Product> getCategory(@PathVariable("category") String productCategory){
  		
  		return productServiceImpl.specificCategoryProducts(productCategory);
  	}
  	
  	
  	//Product data based on discount
  	@GetMapping(value="/discountCategory/{category}/{discountPercent}")
  	public List<Product> getDiscountProducts(@PathVariable("category") String productCategory,@PathVariable("discountPercent") String discount){
  		
  		return productServiceImpl.specificDiscountProducts(productCategory, discount);
  	}
  	
  	
  	@GetMapping(value="/searchProducts/{category}")
  	public List<Product> getSearchProducts(@PathVariable("category") String productSearch){
  		return productServiceImpl.searchProducts(productSearch);
  	}
    
    // getters and setters
    
}
