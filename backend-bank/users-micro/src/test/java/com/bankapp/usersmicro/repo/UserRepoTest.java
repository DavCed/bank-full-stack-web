package com.bankapp.usersmicro.repo;

import com.bankapp.usersmicro.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest // Set up an in-memory database and load JPA configuration for testing
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepoTest {

    @Autowired
    UserRepo userRepo;

    @Test
    void testFindByEmail() {
        // Create a sample user and save it to the database
        User user = new User(1,"Exist","Tent","existent@email.com","password",null,'E');
        user.setEmail("existent@email.com");
        userRepo.saveAndFlush(user);

        // Call the repository method to find the user by email
        Optional<User> foundUser = userRepo.findByEmail("existent@email.com");

        assertTrue(foundUser.isPresent());
        assertEquals("existent@email.com", foundUser.get().getEmail());
    }

    @Test
    void testFindByEmailNotFound() {
        // Call the repository method to find the user by an email that does not exist in the database
        Optional<User> foundUser = userRepo.findByEmail("nonexistent@email.com");

        assertFalse(foundUser.isPresent());
    }
}