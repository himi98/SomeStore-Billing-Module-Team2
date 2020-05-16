package main.java.com.capstore.app.signup_login;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserAccountController {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;
	
	@Autowired
	private EmailSenderService emailSenderService;

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

            ConfirmationToken confirmationToken = new ConfirmationToken(cd);

            confirmationTokenRepository.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(cd.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("himanshu.rathod1998@gmail.com");
            mailMessage.setText("To confirm your account, please click here : "
            +"http://localhost:8082/confirm-account?token="+confirmationToken.getConfirmationToken());

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

            ConfirmationToken confirmationToken = new ConfirmationToken(md);

            confirmationTokenRepository.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(md.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("himanshu.rathod1998@gmail.com");
            mailMessage.setText("To confirm your account, please click here : "
            +"http://localhost:8082/confirm-account?token="+confirmationToken.getConfirmationToken());

            emailSenderService.sendEmail(mailMessage);

            return ResponseEntity.ok(HttpStatus.OK);
        }
    }
    
    
    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> confirmUserAccount(ModelAndView modelAndView, @RequestParam("token")String confirmationToken)
    {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
            if(userRepository.findCustomerByEmailIgnoreCase(token.getCd().getEmail())!=null) {
            	CustomerDetails cd=userRepository.findCustomerByEmailIgnoreCase(token.getCd().getEmail());
            	cd.setActive(true);
                userRepository.saveCustomer(cd);
            }
            else if(userRepository.findMerchantByEmailIgnoreCase(token.getMd().getEmail())!=null) {
            	MerchantDetails md=userRepository.findMerchantByEmailIgnoreCase(token.getMd().getEmail());
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
    // getters and setters
}
