package com.appsdeveloperblog.app.ws.service.impl;

import com.appsdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.repositories.UserRepository;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;
import com.appsdeveloperblog.app.ws.shared.dto.UserDTO;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessages;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  Utils utils;

  @Autowired
  BCryptPasswordEncoder bCryptPasswordEncoder;

  @Override
  public UserDTO createUser(UserDTO userDTO) {

    if (userRepository.findUserByEmail(userDTO.getEmail()) != null)
      throw new RuntimeException("Record " + userDTO.getEmail() + " already exists");

    for(int i = 0; i< userDTO.getAddresses().size(); i++){
      AddressDTO address = userDTO.getAddresses().get(i);
      address.setUserDetails(userDTO);
      address.setAddressId(utils.generateAddressId(30));
      userDTO.getAddresses().set(i,address);
    }
    //BeanUtils.copyProperties(userDTO, userEntity);
    ModelMapper modelMapper = new ModelMapper();
    UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);

    String publicUserId = utils.generateUserId(15);
    userEntity.setUserId(publicUserId);
    userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));

    UserEntity storedUserDetails = userRepository.save(userEntity);

    //UserDTO returnValue = new UserDTO();
    //BeanUtils.copyProperties(storedUserDetails, returnValue);
    UserDTO returnValue = modelMapper.map(storedUserDetails, UserDTO.class);

    return returnValue;
  }

  @Override
  public UserDTO getUserByEmail(String email) {
    UserEntity userEntity = userRepository.findUserByEmail(email);
    if (userEntity == null) throw new UsernameNotFoundException(email);
    UserDTO returnValue = new UserDTO();
    BeanUtils.copyProperties(userEntity, returnValue);
    return returnValue;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    UserEntity userEntity = userRepository.findUserByEmail(email);
    if (userEntity == null) throw new UsernameNotFoundException(email);

    return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
  }

  @Override
  public UserDTO getUserByUserId(String userId) throws UsernameNotFoundException {
    UserDTO returnValue = new UserDTO();
    UserEntity userEntity = userRepository.findUserById(userId);
    if (userEntity == null) throw new UsernameNotFoundException("User with ID: " + userId + " not found!");
    BeanUtils.copyProperties(userEntity, returnValue);
    return returnValue;
  }

  @Override
  public UserDTO updateUser(String userId, UserDTO user) {
    UserDTO returnValue = new UserDTO();
    UserEntity userEntity = userRepository.findUserById(userId);
    if (userEntity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

    userEntity.setFirstName(user.getFirstName());
    userEntity.setLastName(user.getLastName());

    UserEntity updateUserDetails = userRepository.save(userEntity);

    BeanUtils.copyProperties(updateUserDetails, returnValue);
    return returnValue;
  }

  @Override
  public void deleteUser(String userId) {
    UserEntity userEntity = userRepository.findUserById(userId);
    if(userEntity==null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

    userRepository.delete(userEntity);
  }

  @Override
  public List<UserDTO> getUsers(int page, int limit) {
    List<UserDTO> returnValue = new ArrayList<>();
    if(page>0) {
      page -= 1;
    }
    Pageable pageable = PageRequest.of(page,limit);
    Page<UserEntity> usersPage = userRepository.findAll(pageable);
    List<UserEntity> users = usersPage.getContent();
    for (UserEntity userEntity: users){
      UserDTO userDTO = new UserDTO();
      BeanUtils.copyProperties(userEntity, userDTO);
      returnValue.add(userDTO);
    }
    return returnValue;
  }
}
