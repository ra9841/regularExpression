package com.rabin.practice.project.restApi.intel.project.service;

import com.rabin.practice.project.restApi.intel.project.dto.CustomerDto;
import com.rabin.practice.project.restApi.intel.project.entity.CustomerEntity;
import com.rabin.practice.project.restApi.intel.project.exception.UserAlreadyExistException;
import com.rabin.practice.project.restApi.intel.project.repository.CustomerRepository;
import io.micrometer.observation.annotation.Observed;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository mockCustomerRepository;

    @InjectMocks
    private CustomerServiceImpl customerServiceImplUnderTest;


//    @Override
//    @Observed(name="saving.customer")//custom metrics
//    public CustomerDto savingCustomerDetails(CustomerDto customerDto) {
//        CustomerEntity customerEntity = new CustomerEntity();
//        BeanUtils.copyProperties(customerDto, customerEntity);
//        Optional<CustomerEntity> exist = customerRepository.findByUsername(customerDto.getUsername());
//        if (exist.isPresent()) {
//            log.info("userAlready existed in record:" + customerDto.getUsername());
//            throw new UserAlreadyExistException("user already exist");
//
//        } else {
//            customerRepository.save(customerEntity);
//            log.info("Customer record saved in database:" + customerEntity);
//            return customerDto;
//        }
//    }

    @Test
    void testSavingCustomerDetails_ThrowsUserAlreadyExistException() {
        // Setup
        final CustomerDto customerDto = new CustomerDto("username", "password", "email", "phoneNumber", "age");

        // Configure CustomerRepository.findByUsername(...).
        final CustomerEntity customerEntity1 = new CustomerEntity();
        customerEntity1.setId(0L);
        customerEntity1.setUsername("username");
        customerEntity1.setPassword("password");
        customerEntity1.setEmail("email");
        customerEntity1.setPhoneNumber("phoneNumber");
        final Optional<CustomerEntity> customerEntity = Optional.of(customerEntity1);
        when(mockCustomerRepository.findByUsername("username")).thenReturn(customerEntity);

        // Run the test
        assertThatThrownBy(() -> customerServiceImplUnderTest.savingCustomerDetails(customerDto))
                .isInstanceOf(UserAlreadyExistException.class);
    }

    @Test
    void testSavingCustomerDetails_CustomerRepositoryFindByUsernameReturnsAbsent() {
        // Setup
        final CustomerDto customerDto = new CustomerDto("username", "password", "email", "phoneNumber", "age");
        when(mockCustomerRepository.findByUsername("username")).thenReturn(Optional.empty());

        // Run the test
        final CustomerDto result = customerServiceImplUnderTest.savingCustomerDetails(customerDto);

        // Verify the results
        verify(mockCustomerRepository).save(any(CustomerEntity.class));
    }


//    @Override
//    @Observed(name = "get.customer")
//    @Scheduled(fixedRate = 50000)
//    public List<CustomerDto> getListOFRecords() {
//        log.info("The time is now {}", dateFormat.format(new Date()));
//        return customerRepository.findAll()
//                .stream().map(m -> {
//                    CustomerDto customerDto = new CustomerDto();
//                    BeanUtils.copyProperties(m, customerDto);
//                    return customerDto;
//                })
//
//                .collect(Collectors.toList());
//    }

    @Test
    void testGetListOFRecords() {
        // Setup
        // Configure CustomerRepository.findAll(...).
        final CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(0L);
        customerEntity.setUsername("username");
        customerEntity.setPassword("password");
        customerEntity.setEmail("email");
        customerEntity.setPhoneNumber("phoneNumber");
        final List<CustomerEntity> customerEntities = List.of(customerEntity);
        when(mockCustomerRepository.findAll()).thenReturn(customerEntities);

        // Run the test
        final List<CustomerDto> result = customerServiceImplUnderTest.getListOFRecords();

        // Verify the results
    }

    @Test
    void testGetListOFRecords_CustomerRepositoryReturnsNoItems() {
        // Setup
        when(mockCustomerRepository.findAll()).thenReturn(Collections.emptyList());

        // Run the test
        final List<CustomerDto> result = customerServiceImplUnderTest.getListOFRecords();

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }


//    @Override
//    @Cacheable(cacheNames = "customer",key="#username")
//    @Observed(name="get.particularCustomers")
//    public CustomerDto getParticularRecordOfUsername(String username) throws NoSuchFieldException {
//        log.info("fetching customer record form database");
//        Optional<CustomerEntity> existUsername = customerRepository.findByUsername(username);
//        if (existUsername.isPresent()) {
//            CustomerEntity customerEntity = existUsername.get();
//            CustomerDto customerDto = new CustomerDto();
//            BeanUtils.copyProperties(customerEntity, customerDto);
//            return customerDto;
//        } else {
//            throw new NoSuchFieldException("usernot found");
//        }
//
//    }

    @Test
    void testGetParticularRecordOfUsername() throws Exception {
        // Setup
        // Configure CustomerRepository.findByUsername(...).
        final CustomerEntity customerEntity1 = new CustomerEntity();
        customerEntity1.setId(0L);
        customerEntity1.setUsername("username");
        customerEntity1.setPassword("password");
        customerEntity1.setEmail("email");
        customerEntity1.setPhoneNumber("phoneNumber");
        final Optional<CustomerEntity> customerEntity = Optional.of(customerEntity1);
        when(mockCustomerRepository.findByUsername("username")).thenReturn(customerEntity);

        // Run the test
        final CustomerDto result = customerServiceImplUnderTest.getParticularRecordOfUsername("username");

        // Verify the results
    }

    @Test
    void testGetParticularRecordOfUsername_CustomerRepositoryReturnsAbsent() {
        // Setup
        when(mockCustomerRepository.findByUsername("username")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> customerServiceImplUnderTest.getParticularRecordOfUsername("username"))
                .isInstanceOf(NoSuchFieldException.class);
    }



//    @Override
//    @CachePut(cacheNames = "customer", key = "#customerDto.username")
//    public CustomerDto updateRecordByUsername(CustomerDto customerDto, String username) throws NoSuchFieldException {
//        CustomerDto customerDto1 = getParticularRecordOfUsername(username);
//        customerDto1.setUsername(customerDto.getUsername());
//        customerDto1.setPassword(customerDto.getPassword());
//        customerDto1.setEmail(customerDto.getEmail());
//        customerDto1.setAge(customerDto.getAge());
//        customerDto1.setPhoneNumber(customerDto.getPhoneNumber());
//
//        Optional<CustomerEntity> existCustomer = customerRepository.findByUsername(username);
//        if (existCustomer.isPresent()) {
//            CustomerEntity customerEntity1 = existCustomer.get();
//            customerEntity1.setEmail(customerDto1.getEmail());
//            customerEntity1.setUsername(customerDto1.getUsername());
//            customerEntity1.setPassword(customerDto1.getPassword());
//            customerRepository.save(customerEntity1);
//            log.info("customer update with username:"+ customerEntity1);
//            CustomerDto customer = new CustomerDto();
//            BeanUtils.copyProperties(customerEntity1, customer);
//            return customer;
//        } else {
//            throw new NoSuchFieldException("usernot found");
//        }
//
//
//    }

    @Test
    void testUpdateRecordByUsername() throws Exception {
        // Setup
        final CustomerDto customerDto = new CustomerDto("username", "password", "email", "phoneNumber", "age");

        // Configure CustomerRepository.findByUsername(...).
        final CustomerEntity customerEntity1 = new CustomerEntity();
        customerEntity1.setId(0L);
        customerEntity1.setUsername("username");
        customerEntity1.setPassword("password");
        customerEntity1.setEmail("email");
        customerEntity1.setPhoneNumber("phoneNumber");
        final Optional<CustomerEntity> customerEntity = Optional.of(customerEntity1);
        when(mockCustomerRepository.findByUsername("username")).thenReturn(customerEntity);

        // Run the test
        final CustomerDto result = customerServiceImplUnderTest.updateRecordByUsername(customerDto, "username");

        // Verify the results
        verify(mockCustomerRepository).save(any(CustomerEntity.class));
    }

    @Test
    void testUpdateRecordByUsername_CustomerRepositoryFindByUsernameReturnsAbsent() {
        // Setup
        final CustomerDto customerDto = new CustomerDto("username", "password", "email", "phoneNumber", "age");
        when(mockCustomerRepository.findByUsername("username")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(
                () -> customerServiceImplUnderTest.updateRecordByUsername(customerDto, "username"))
                .isInstanceOf(NoSuchFieldException.class);
    }



//    @Override
//    @CacheEvict(cacheNames = "customer",key="#username")
//    public String deleteCustomerRecord(String username) {
//        Optional<CustomerEntity> existCustomer = customerRepository.findByUsername(username);
//        if (existCustomer.isPresent()) {
//            log.info("deleted data from database:"+ existCustomer);
//            customerRepository.delete(existCustomer.get());
//            return "delete successfully";
//        } else {
//            return "user not exist";
//        }
//
//    }

    @Test
    void testDeleteCustomerRecord() {
        // Setup
        // Configure CustomerRepository.findByUsername(...).
        final CustomerEntity customerEntity1 = new CustomerEntity();
        customerEntity1.setId(0L);
        customerEntity1.setUsername("username");
        customerEntity1.setPassword("password");
        customerEntity1.setEmail("email");
        customerEntity1.setPhoneNumber("phoneNumber");
        final Optional<CustomerEntity> customerEntity = Optional.of(customerEntity1);
        when(mockCustomerRepository.findByUsername("username")).thenReturn(customerEntity);

        // Run the test
        final String result = customerServiceImplUnderTest.deleteCustomerRecord("username");

        // Verify the results
        assertThat(result).isEqualTo("delete successfully");
        verify(mockCustomerRepository).delete(any(CustomerEntity.class));
    }

    @Test
    void testDeleteCustomerRecord_CustomerRepositoryFindByUsernameReturnsAbsent() {
        // Setup
        when(mockCustomerRepository.findByUsername("username")).thenReturn(Optional.empty());

        // Run the test
        final String result = customerServiceImplUnderTest.deleteCustomerRecord("username");

        // Verify the results
        assertThat(result).isEqualTo("user not exist");
    }



//    @Override
//    public CustomerDto getCustomerRecordByEmail(String email) throws NoSuchFieldException {
//        Optional<CustomerEntity>existCustomer=customerRepository.findByEmail(email);
//        if(existCustomer.isPresent()){
//            CustomerEntity customerEntity=existCustomer.get();
//            log.info("record email from database:" +customerEntity);
//            CustomerDto customerDto=new CustomerDto();
//            BeanUtils.copyProperties(customerEntity,customerDto);
//            log.info("record send to controller:" + customerDto);
//            return customerDto;
//        }else{
//            throw new NoSuchFieldException("such email is not in the record");
//        }
//
//    }

    @Test
    void testGetCustomerRecordByEmail() throws Exception {
        // Setup
        // Configure CustomerRepository.findByEmail(...).
        final CustomerEntity customerEntity1 = new CustomerEntity();
        customerEntity1.setId(0L);
        customerEntity1.setUsername("username");
        customerEntity1.setPassword("password");
        customerEntity1.setEmail("email");
        customerEntity1.setPhoneNumber("phoneNumber");
        final Optional<CustomerEntity> customerEntity = Optional.of(customerEntity1);
        when(mockCustomerRepository.findByEmail("email")).thenReturn(customerEntity);

        // Run the test
        final CustomerDto result = customerServiceImplUnderTest.getCustomerRecordByEmail("email");

        // Verify the results
    }

    @Test
    void testGetCustomerRecordByEmail_CustomerRepositoryReturnsAbsent() {
        // Setup
        when(mockCustomerRepository.findByEmail("email")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> customerServiceImplUnderTest.getCustomerRecordByEmail("email"))
                .isInstanceOf(NoSuchFieldException.class);
    }


//    @Override
//    public List<CustomerDto> getListOfUsernameOnAscending() {
//        List<CustomerDto> customerDto = getListOFRecords();
//        log.info("data get from another method:" + customerDto);
//        return customerDto.stream()
//                .sorted(Comparator.comparing(CustomerDto::getUsername))
//                .collect(Collectors.toList());
//
//    }


    @Test
    void testGetListOfUsernameOnAscending() {
        // Setup
        // Configure CustomerRepository.findAll(...).
        final CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(0L);
        customerEntity.setUsername("username");
        customerEntity.setPassword("password");
        customerEntity.setEmail("email");
        customerEntity.setPhoneNumber("phoneNumber");
        final List<CustomerEntity> customerEntities = List.of(customerEntity);
        when(mockCustomerRepository.findAll()).thenReturn(customerEntities);

        // Run the test
        final List<CustomerDto> result = customerServiceImplUnderTest.getListOfUsernameOnAscending();

        // Verify the results
    }

    @Test
    void testGetListOfUsernameOnAscending_CustomerRepositoryReturnsNoItems() {
        // Setup
        when(mockCustomerRepository.findAll()).thenReturn(Collections.emptyList());

        // Run the test
        final List<CustomerDto> result = customerServiceImplUnderTest.getListOfUsernameOnAscending();

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }



//    @Override
//    public List<CustomerDto> getListOfUsernameOnDscending() {
//        List<CustomerDto> customerDto = getListOFRecords();
//        log.info("data get from another method:" + customerDto);
//        return customerDto.stream()
//                .sorted(Comparator.comparing(CustomerDto::getUsername,Comparator.reverseOrder()))
//                .collect(Collectors.toList());
//    }

    @Test
    void testGetListOfUsernameOnDscending() {
        // Setup
        // Configure CustomerRepository.findAll(...).
        final CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(0L);
        customerEntity.setUsername("username");
        customerEntity.setPassword("password");
        customerEntity.setEmail("email");
        customerEntity.setPhoneNumber("phoneNumber");
        final List<CustomerEntity> customerEntities = List.of(customerEntity);
        when(mockCustomerRepository.findAll()).thenReturn(customerEntities);

        // Run the test
        final List<CustomerDto> result = customerServiceImplUnderTest.getListOfUsernameOnDscending();

        // Verify the results
    }

    @Test
    void testGetListOfUsernameOnDscending_CustomerRepositoryReturnsNoItems() {
        // Setup
        when(mockCustomerRepository.findAll()).thenReturn(Collections.emptyList());

        // Run the test
        final List<CustomerDto> result = customerServiceImplUnderTest.getListOfUsernameOnDscending();

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }
}
