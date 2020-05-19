package main.java.com.capstore.app.signup_login;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import main.java.com.capstore.app.models.CustomerDetails;
import main.java.com.capstore.app.models.MerchantDetails;

import lombok.Data;

@Data
@Transactional
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserAccountController {
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
            mailMessage.setFrom("himanshu.rathod1998@gmail.com");
            mailMessage.setText("To confirm your account, please click here : "
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
            mailMessage.setTo(md.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("himanshu.rathod1998@gmail.com");
            mailMessage.setText("To confirm your account, please click here : "
            +"http://localhost:4200/verify?token="+confirmationToken.getConfirmationToken());

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
                userRepository.saveMerchant(md);
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
    	else if (role.equals("Merchant")) {
    		MerchantDetails md=userRepository.findMerchantByEmailIgnoreCase(email);
    		if(md!=null && md.isActive()==true) {
    			if(pass.equals(md.getPassword())) {
    				return ResponseEntity.ok().body(md);
    			}
    		}
    	}
    	return new ResponseEntity<Error>(HttpStatus.CONFLICT);
    }
    
    // getters and setters
    
}
