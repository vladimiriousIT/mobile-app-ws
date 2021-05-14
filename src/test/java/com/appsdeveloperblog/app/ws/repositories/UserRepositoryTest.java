package com.appsdeveloperblog.app.ws.repositories;

class UserRepositoryTest {
//    @Autowired
//    public UserRepository userRepository;
//
//    @BeforeEach
//    void setUp() throws Exception {
//        UserEntity userEntity = new UserEntity();
//        userEntity.setId(2L);
//        userEntity.setFirstName("Vladimir");
//        userEntity.setLastName("Stratiev");
//        userEntity.setUser_Id("1a2b3c");
//        userEntity.setEncryptedPassword("xxx");
//        userEntity.setEmail("vladimir@yahoo.com");
//        userEntity.setEmailVerificationStatus(true);
//
//        AddressEntity addressEntity = new AddressEntity();
//        addressEntity.setType("shipping");
//        addressEntity.setAddressId("ahfdshifdsj");
//        addressEntity.setCity("Sofia");
//        addressEntity.setCountry("Bulgaria");
//        addressEntity.setPostalCode("ABCCDA");
//        addressEntity.setStreetName("Mladost1");
//
//        List<AddressEntity> addresses = new ArrayList<>();
//        addresses.add(addressEntity);
//        userEntity.setAddresses(addresses);
//
//        userRepository.save(userEntity);
//
//        UserEntity userEntity2 = new UserEntity();
//        userEntity.setFirstName("Yavor");
//        userEntity.setLastName("Stratiev");
//        userEntity.setUser_Id("1a2b3c");
//        userEntity.setEncryptedPassword("xxx");
//        userEntity.setEmail("yavor@yahoo.com");
//        userEntity.setEmailVerificationStatus(true);
//
//        AddressEntity addressEntity2 = new AddressEntity();
//        addressEntity.setType("shipping");
//        addressEntity.setAddressId("ahfdshifdsad");
//        addressEntity.setCity("Sofia");
//        addressEntity.setCountry("Bulgaria");
//        addressEntity.setPostalCode("ABCCDAE");
//        addressEntity.setStreetName("Mladost2");
//
//        List<AddressEntity> addresses2 = new ArrayList<>();
//        addresses2.add(addressEntity2);
//        userEntity2.setAddresses(addresses2);
//
//        userRepository.save(userEntity2);
//    }
//
//    @Test
//    final void testFindUserEntityByUserId(){
//        String userId = "1a2b3c";
//        UserEntity userEntity = userRepository.findUserEntityByUserId(userId);
//
//        assertNotNull(userEntity);
//        assertTrue(userEntity.getUser_Id().equals(userId));
//    }
//
//    @Test
//    final void testFindUserByFirstName() {
//        String firstName = "Stratiev";
//        List<UserEntity> users = userRepository.findUserByFirstName(firstName);
//        assertNotNull(users);
//        assertTrue(users.size() == 2);
//
//        UserEntity user = users.get(0);
//        assertTrue(user.getFirstName().equals(firstName));
//    }
//
//    @Test
//    final void testUpdateUserEmailVerificationStatus(){
//        boolean newEmailVerificationStatus = false;
//        userRepository.updateUserEmailVerificationStatus(newEmailVerificationStatus, "1a2b3c");
//
//        UserEntity storedUserDetails = userRepository.findUserById("1a2b3c");
//
//        boolean storedEmailVerificationStatus = storedUserDetails.getEmailVerificationStatus();
//        assertTrue(storedEmailVerificationStatus == newEmailVerificationStatus);
//    }
//
//    @Test
//    final void testFindUserByLastName() {
//        String lastName = "Stratiev";
//        List<UserEntity> users = userRepository.findUserByLastName(lastName);
//        assertNotNull(users);
//        assertTrue(users.size() == 2);
//
//        UserEntity user = users.get(0);
//        assertTrue(user.getFirstName().equals(lastName));
//    }
//
//    @Test
//    final void testFindUserByKeyword() {
//        String keyword = "Vla";
//        List<UserEntity> users = userRepository.findUserByKeyword(keyword);
//        assertNotNull(users);
//        assertTrue(users.size() == 2);
//
//        UserEntity user = users.get(0);
//        assertTrue(user.getFirstName().contains(keyword) || user.getLastName().contains(keyword));
//    }
//
//    @Test
//    final void testGetVerifiedUsers() {
//        Pageable pageableRequest = PageRequest.of(0, 2);
//        Page<UserEntity> pages = userRepository.findAllUsersWithConfirmedEmailAddress(pageableRequest);
//        assertNotNull(pages);
//
//        List<UserEntity> userEntities = pages.getContent();
//        assertNotNull(userEntities);
//        assertTrue(userEntities.size() == 2);
//    }

}