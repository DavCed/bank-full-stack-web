package com.bankapp.usersmicro;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@SpringBootTest
class UsersMicroApplicationTest {

	@Test
	void testGetPasswordEncoder() {
		// Arrange
		BCryptPasswordEncoder mockedPasswordEncoder = mock(BCryptPasswordEncoder.class);
		// Act
		BCryptPasswordEncoder encoder = UsersMicroApplication.getPasswordEncoder();
		// Assert
		assertNotNull(encoder);
	}
}