package com.appsdeveloperblog.app.ws.service.impl;

import com.appsdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.repositories.UserRepository;
import com.appsdeveloperblog.app.ws.shared.AmazonSES;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;
import com.appsdeveloperblog.app.ws.shared.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    Utils utils;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    AmazonSES amazonSES;

    String userId = "hhty657efy";
    String encryptedPassword = "74hgh8474jf";

    UserEntity userEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Vladimir");
        userEntity.setLastName("Stratiev");
        userEntity.setUser_Id(userId);
        userEntity.setEncryptedPassword(encryptedPassword);
        userEntity.setEmail("test@yahoo.com");
        userEntity.setEmailVerificationToken("fudshuf3903jfjds");
        // userEntity.setAddresses((List<AddressEntity>) getAddressEntity().get(1));
    }

    @Test
    final void testGetUserByEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDTO userDTO = userService.getUserByEmail("test@yahoo.com");

        assertNotNull(userDTO);
        assertEquals("test@yahoo.com", userDTO.getEmail());
        assertEquals("Vladimir", userDTO.getFirstName());
        assertEquals("Stratiev", userDTO.getLastName());

    }

    @Test
    final void testGetUserByEmail_UsernameNotFoundException() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        //Handle exception
        assertThrows(UsernameNotFoundException.class,
                () -> userService.getUserByEmail("test@yahoo.com"));
    }

    @Test
    final void testCreateUser_CreateUserServiceException() {
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Vladimir");
        userDTO.setLastName("Stratiev");
        userDTO.setPassword("1234567");
        userDTO.setEmail("test@yahoo.com");
        userDTO.setAddresses(getAddressesDTO());

        assertThrows(UserServiceException.class,
                () -> userService.createUser(userDTO));
    }

        @Test
        @Disabled
    final void testCreateUser() {

        // userRepository.findByEmail(userDTO.getEmail()) != null
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        // utils.generateAddressId(30)
        when(utils.generateAddressId(anyInt())).thenReturn("hdsajdj459jnds");

        // utils.generateUserId(15)
        when(utils.generateUserId(anyInt())).thenReturn(userId);

        // bCryptPasswordEncoder.encode(userDTO.getPassword())
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);

        // userRepository.save(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        Mockito.doNothing().when(amazonSES).verifyEmail(any(UserDTO.class));

        UserDTO userDTO = new UserDTO();
        userDTO.setAddresses(getAddressesDTO());
        userDTO.setFirstName("Vladimir");
        userDTO.setLastName("Stratiev");
        userDTO.setPassword("1234567");
        userDTO.setEmail("test@yahoo.com");

        UserDTO storedUserDetails = userService.createUser(userDTO);

        assertNotNull(storedUserDetails);
        assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
        assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());
        assertNotNull(storedUserDetails.getUser_Id());
        assertEquals(storedUserDetails.getAddresses().size(), userEntity.getAddresses().size());
        verify(utils,times(2)).generateAddressId(30);
        verify(bCryptPasswordEncoder, times(1)).encode("1234567");
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    private List<AddressDTO> getAddressesDTO() {
        AddressDTO shippingAddressDTO = new AddressDTO();
        shippingAddressDTO.setType("shipping");
        shippingAddressDTO.setCity("Sofia");
        shippingAddressDTO.setCountry("Bulgaria");
        shippingAddressDTO.setPostalCode("1001");
        shippingAddressDTO.setStreetName("Mladost1");

        AddressDTO billingAddressDTO = new AddressDTO();
        billingAddressDTO.setType("shipping");
        billingAddressDTO.setCity("Sofia");
        billingAddressDTO.setCountry("Bulgaria");
        billingAddressDTO.setPostalCode("1002");
        billingAddressDTO.setStreetName("Mladost2");

        List<AddressDTO> addressesList = new ArrayList<>();
        addressesList.add(shippingAddressDTO);
        addressesList.add(billingAddressDTO);

        return addressesList;
    }

    private List<AddressEntity> getAddressEntity() {
        List<AddressDTO> addresses = getAddressesDTO();

        //Type listType = new TypeToken<List<AddressEntity>>() {}.getType();
        return new ModelMapper().map(addresses, new TypeToken<List<AddressEntity>>() {}.getType());
    }
}
