package com.appsdeveloperblog.app.ws.shared;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UtilsTest {

    @Autowired
    Utils utils;

    @BeforeEach
    void setUp() throws Exception {

    }

    @Test
    final void testGenerateUserId(){
        String userId1 = utils.generateUserId(30);
        String userId2 = utils.generateUserId(30);
        assertNotNull(userId1);
        assertNotNull(userId2);
        assertTrue(userId1.length() == 30);
        assertTrue(!userId1.equalsIgnoreCase(userId2));
    }

    @Test
    final void testHasTokenNotExpired(){
        String token = utils.generateEmailVerificationToken("4yr65hhyid84");
        assertNotNull(token);

        boolean hasTokenExpired = Utils.hasTokenExpired(token);
        assertFalse(hasTokenExpired);
    }

    @Test
    final void testHasTokenExpired(){
        // Add expired token here
        String expiredToken = "12";
        boolean hasTokenExpired = Utils.hasTokenExpired(expiredToken);
        //Delete after adding new token
        //assertTrue(hasTokenExpired);
    }
}
