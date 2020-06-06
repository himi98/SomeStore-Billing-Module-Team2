package com.capstore.app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.capstore.app.controller.AppController;
import com.capstore.app.repository.ConfirmationTokenRepository;
import com.capstore.app.repository.ProductServiceImpl;
import com.capstore.app.repository.UserRepository;
import com.capstore.app.signup_login.EmailSenderService;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class AppControllerTest {

	@InjectMocks
	AppController appController;

	@Mock
	ProductServiceImpl productServiceImpl;

	@Mock
	UserRepository userRepository;

	@Mock
	ConfirmationTokenRepository confirmationTokenRepository;

	@Mock
	EmailSenderService emailSenderService;

	@Test
	public void testChangePassword() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	}
}
