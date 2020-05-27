package main.java.com.capstore.app.controller;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
import main.java.com.capstore.app.models.CustomerDetails;
import main.java.com.capstore.app.models.MerchantDetails;
import main.java.com.capstore.app.models.Product;
import main.java.com.capstore.app.repository.ConfirmationTokenRepository;
import main.java.com.capstore.app.repository.ProductServiceImpl;
import main.java.com.capstore.app.repository.UserRepository;
import main.java.com.capstore.app.signup_login.ConfirmationToken;
import main.java.com.capstore.app.signup_login.EmailSenderService;
import main.java.com.capstore.app.signup_login.PasswordProtector;

@Data
@Transactional
@RestController
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class AppController {
	@Autowired
	ProductServiceImpl productServiceImpl;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ConfirmationTokenRepository confirmationTokenRepository;

	@Autowired
	EmailSenderService emailSenderService;

	@RequestMapping(value = "/registerCustomer", method = RequestMethod.POST)
	public ResponseEntity<?> registerCustomer(@Valid @RequestBody CustomerDetails cd) throws Exception {
		CustomerDetails existingCustomer = userRepository.findCustomerByEmailIgnoreCase(cd.getEmail());
		MerchantDetails existingMerchant = userRepository.findMerchantByEmailIgnoreCase(cd.getEmail());
		if (existingCustomer != null && existingMerchant != null) {

			return new ResponseEntity<Error>(HttpStatus.CONFLICT);
		} else {
			cd.setPassword(PasswordProtector.encrypt(cd.getPassword()));
			userRepository.saveCustomer(cd);
			CustomerDetails cd1 = userRepository.findCustomerByEmailIgnoreCase(cd.getEmail());

			ConfirmationToken confirmationToken = new ConfirmationToken(cd1.getUserId());
			// System.out.println(confirmationToken);

			confirmationTokenRepository.save(confirmationToken);

			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(cd.getEmail());
			mailMessage.setSubject("Complete Registration!");
			mailMessage.setFrom("capstore06@gmail.com");
			mailMessage.setText("To activate your account, please click here : " + "http://localhost:4200/verify?token="
					+ confirmationToken.getConfirmationToken());

			emailSenderService.sendEmail(mailMessage);

			return ResponseEntity.ok(HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/registerMerchant", method = RequestMethod.POST)
	public ResponseEntity<?> registerMerchant(@Valid @RequestBody MerchantDetails md) throws Exception {
		CustomerDetails existingCustomer = userRepository.findCustomerByEmailIgnoreCase(md.getEmail());
		MerchantDetails existingMerchant = userRepository.findMerchantByEmailIgnoreCase(md.getEmail());
		if (existingMerchant != null && existingCustomer != null) {
			return new ResponseEntity<Error>(HttpStatus.CONFLICT);
		} else {
			md.setPassword(PasswordProtector.encrypt(md.getPassword()));
			userRepository.saveMerchant(md);
			MerchantDetails md1 = userRepository.findMerchantByEmailIgnoreCase(md.getEmail());

			ConfirmationToken confirmationToken = new ConfirmationToken(md1.getUserId());

			confirmationTokenRepository.save(confirmationToken);

			MimeMessage mailMessage = emailSenderService.createMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);
			String url = "http://localhost:4200/verifyMerchant?token=" + confirmationToken.getConfirmationToken();
			helper.setTo("himanshu.rathod1998@gmail.com");
			helper.setSubject("Merchant Requesting Approval!");
			helper.setFrom("capstore06@gmail.com");
			helper.setText("<html><body><h1>Merchant Registration!</h1><br>" + md + "<br><button type='submit'>"
					+ "<a href=" + url + ">Show Details</a></button>", true);

			emailSenderService.sendEmail(mailMessage);

			return ResponseEntity.ok(HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/confirm-account", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<?> confirmUserAccount(@Valid @RequestParam("token") String confirmationToken) {
		ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

		if (token != null) {
			if (userRepository.findCustomerById(token.getUid()) != null) {
				CustomerDetails cd = userRepository.findCustomerById(token.getUid());
				cd.setActive(true);
				userRepository.saveCustomer(cd);
			}
			return ResponseEntity.ok(HttpStatus.OK);
		} else {
			return new ResponseEntity<Error>(HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/generateToken")
	public ResponseEntity<?> generateToken(@Valid @RequestParam("token") String confirmationToken,
			@Valid @RequestParam("action") String action) throws MessagingException {

		ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

		MerchantDetails md = userRepository.findMerchantById(token.getUid());
		if (action.equals("Accept")) {
			md.setActive(true);
			md.setApproved(true);
		} else {
			md.setActive(false);
			md.setApproved(false);
		}

		userRepository.saveMerchant(md);

		MimeMessage mailMessage = emailSenderService.createMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);
		helper.setTo(md.getEmail());
		helper.setSubject("Account Activated!");
		helper.setFrom("capstore06@gmail.com");
		helper.setText("Admin approved your account.\nTo login and access your account, please click here : "
				+ "http://localhost:4200");

		emailSenderService.sendEmail(mailMessage);

		return ResponseEntity.ok().body(md);
	}

	@RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<?> userLogin(@Valid @RequestBody String[] userCredentials) throws Exception {
		String email = userCredentials[0];
		String pass = userCredentials[1];
		String role = userCredentials[2];
		if (role.equals("Customer")) {
			CustomerDetails cd = userRepository.findCustomerByEmailIgnoreCase(email);
			if (cd != null && cd.isActive() == true) {
				if (pass.equals(PasswordProtector.decrypt(cd.getPassword()))) {
					return ResponseEntity.ok().body(cd);
				}
			}
		} else {
			MerchantDetails md = userRepository.findMerchantByEmailIgnoreCase(email);
			if (md != null && md.isActive() == true) {
				if (pass.equals(PasswordProtector.decrypt(md.getPassword()))) {
					return ResponseEntity.ok().body(md);
				}
			}
		}
		return new ResponseEntity<Error>(HttpStatus.CONFLICT);
	}

	@RequestMapping(value = "/forgotPassword", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<?> forgotPassword(@Valid @RequestBody String email) throws Exception {
		if (userRepository.findCustomerByEmailIgnoreCase(email) != null) {
			CustomerDetails cd = userRepository.findCustomerByEmailIgnoreCase(email);
			if (cd.isActive()) {
				SimpleMailMessage mailMessage = new SimpleMailMessage();
				mailMessage.setTo(cd.getEmail());
				mailMessage.setSubject("Forgot Password");
				mailMessage.setFrom("capstore06@gmail.com");
				mailMessage.setText("Hi, your password is : " + PasswordProtector.decrypt(cd.getPassword()) + "\n"
						+ "Note: Confidential Information. Do Not Share it with Others.");
				emailSenderService.sendEmail(mailMessage);

				return ResponseEntity.ok(HttpStatus.OK);
			}
		} else if (userRepository.findMerchantByEmailIgnoreCase(email) != null) {
			MerchantDetails md = userRepository.findMerchantByEmailIgnoreCase(email);
			if (md.isActive()) {
				SimpleMailMessage mailMessage = new SimpleMailMessage();
				mailMessage.setTo(md.getEmail());
				mailMessage.setSubject("Forgot Password");
				mailMessage.setFrom("capstore06@gmail.com");
				mailMessage.setText("Hi, your password is : " + PasswordProtector.decrypt(md.getPassword()) + "\n"
						+ "Note: Confidential Information. Do Not Share it with Others.");
				emailSenderService.sendEmail(mailMessage);

				return ResponseEntity.ok(HttpStatus.OK);
			}
		}

		return new ResponseEntity<Error>(HttpStatus.CONFLICT);
	}

	@RequestMapping(value = "/changePassword", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<?> changePassword(@Valid @RequestBody String[] details) throws Exception {
		String currPass = details[0];
		String newPass = details[1];
		String email = details[2];
		if (userRepository.findCustomerByEmailIgnoreCase(email) != null) {
			CustomerDetails cd = userRepository.findCustomerByEmailIgnoreCase(email);
			if (cd.isActive() && currPass.equals(PasswordProtector.decrypt(cd.getPassword()))) {
				cd.setPassword(PasswordProtector.encrypt(newPass));
				return ResponseEntity.ok(HttpStatus.OK);
			}
		} else if (userRepository.findMerchantByEmailIgnoreCase(email) != null) {
			MerchantDetails md = userRepository.findMerchantByEmailIgnoreCase(email);
			if (md.isActive() && currPass.equals(PasswordProtector.decrypt(md.getPassword()))) {
				md.setPassword(PasswordProtector.encrypt(newPass));
				return ResponseEntity.ok(HttpStatus.OK);
			}
		}

		return new ResponseEntity<Error>(HttpStatus.CONFLICT);
	}

	@RequestMapping(value = "/getMerchant", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<?> userLogin(@Valid @RequestParam("token") String confToken) {
		ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confToken);
		MerchantDetails md = userRepository.findMerchantById(token.getUid());
		return ResponseEntity.ok().body(md);
	}

	// All Products Data
	@GetMapping(value = "/allProducts")
	public List<Product> getAllProducts() {
		return productServiceImpl.allProductsList();
	}

	// Products data of particular category
	@GetMapping(value = "/productCategory/{category}")
	public List<Product> getCategory(@PathVariable("category") String productCategory) {

		return productServiceImpl.specificCategoryProducts(productCategory);
	}

	// Product data based on discount
	@GetMapping(value = "/discountCategory/{category}/{discountPercent}")
	public List<Product> getDiscountProducts(@PathVariable("category") String productCategory,
			@PathVariable("discountPercent") String discount) {

		return productServiceImpl.specificDiscountProducts(productCategory, discount);
	}

	@GetMapping(value = "/searchProducts/{category}")
	public List<Product> getSearchProducts(@PathVariable("category") String productSearch) {
		return productServiceImpl.searchProducts(productSearch);
	}

	@GetMapping("/{email}")
	public ResponseEntity<MerchantDetails> verifyMerchantDetails(@PathVariable("email") String email) {
		return new ResponseEntity<MerchantDetails>(userRepository.findMerchantByEmailIgnoreCase(email), HttpStatus.OK);
	}

	@GetMapping("/{category}/{order}")
	public List<Product> filterandcategory(@PathVariable String category, @PathVariable String order) {
		return productServiceImpl.filterAndCategory(category, order);
	}

	// getters and setters

}
