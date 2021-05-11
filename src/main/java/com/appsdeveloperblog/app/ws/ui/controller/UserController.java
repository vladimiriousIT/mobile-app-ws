package com.appsdeveloperblog.app.ws.ui.controller;

import com.appsdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.service.AddressService;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;
import com.appsdeveloperblog.app.ws.shared.dto.UserDTO;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")  // http://localhost:8080/mobile-app-users
public class UserController {

  @Autowired
  UserService userService;

  @Autowired
  AddressService addressService;
  //http://localhost:8080/mobile-app-ws/users/
  @GetMapping (path = (String)"/{userId}",
               produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
  public UserRest getUser(@PathVariable ("userId") String userId) {
    UserRest returnValue = new UserRest();

    UserDTO userDTO = userService.getUserByUserId(userId.toString());
    BeanUtils.copyProperties(userDTO, returnValue);
    return returnValue;
  }

  @PostMapping(
    consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
    produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
  public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
    UserRest returnValue = new UserRest();
    if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
//    UserDTO userDTO = new UserDTO();
//    BeanUtils.copyProperties(userDetails, userDTO);

    ModelMapper modelMapper = new ModelMapper();
    UserDTO userDTO = modelMapper.map(userDetails, UserDTO.class);

    UserDTO createdUser = userService.createUser(userDTO);
//    BeanUtils.copyProperties(createUser, returnValue);
    returnValue = modelMapper.map(createdUser, UserRest.class);

    return returnValue;
  }

  @PutMapping(path ="/{id}",
    consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
    produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
  public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
    UserRest returnValue = new UserRest();
    if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
    UserDTO userDTO = new UserDTO();
    BeanUtils.copyProperties(userDetails, userDTO);

    UserDTO updatedUser = userService.updateUser(id, userDTO);
    BeanUtils.copyProperties(updatedUser, returnValue);

    return returnValue;
  }

  @DeleteMapping(path ="/{id}",produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
  public OperationStatusModel deleteUser(@PathVariable String userId) {
    OperationStatusModel returnValue = new OperationStatusModel();
    returnValue.setOperationName(RequestOperationName.DELETE.name());
    userService.deleteUser(userId);
    returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
    return returnValue;
  }

  @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
  public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "limit", defaultValue = "25") int limit){

    List<UserRest> returnValue = new ArrayList<>();
    List<UserDTO> users = userService.getUsers(page,limit);

    for(UserDTO userDTO: users){
      UserRest userModel = new UserRest();
      BeanUtils.copyProperties(userDTO, userModel);
      returnValue.add(userModel);
    }
    return returnValue;
  }

  //http://localhost:8080/mobile-app-ws/users/addresses
  @GetMapping (path = "/{id}/addresses",
    produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, "application/hal+json" })
  public CollectionModel<AddressesRest> getUserAddresses(@PathVariable String id) {
    List<AddressesRest> addressesListRestModel = new ArrayList<>();

    List<AddressDTO> addressesDTO = addressService.getAddresses(id);
    if(addressesDTO != null && !addressesDTO.isEmpty())
    {
      Type listType = new TypeToken<List<AddressesRest>>() {}.getType();
      addressesListRestModel = new ModelMapper().map(addressesDTO, listType);

      for(AddressesRest addressRest : addressesListRestModel){
        Link addressLink = linkTo(methodOn(UserController.class).getUser(id)).withRel("user");
        addressRest.add(addressLink);

        Link userLink = linkTo(methodOn(UserController.class).getUser(id)).withRel("user");
        addressRest.add(userLink);
      }
    }

    return new CollectionModel<>(addressesListRestModel);
  }

  @GetMapping(path = "/{userId}/addresses/{addressId", produces = {
    MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, "application/hal+json"
  })
  public EntityModel<AddressesRest> getUserAddress(@PathVariable String userId,
                                                       @PathVariable String addressId){
    AddressDTO addressDTO = addressService.getAddress(addressId);

    ModelMapper modelMapper = new ModelMapper();

    Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(userId, addressId)).withSelfRel();
    Link userLink = linkTo(UserController.class).slash(userId).withRel("user");
    Link addressesLink = linkTo(methodOn(UserController.class).getUserAddresses(userId)).withRel("addresses");

    AddressesRest addressesRestModel = modelMapper.map(addressDTO, AddressesRest.class);

    addressesRestModel.add(addressLink);
    //addressesRestModel.add(userLink);
    addressesRestModel.add(addressesLink);

    return new EntityModel<>(addressesRestModel);
  }
}
