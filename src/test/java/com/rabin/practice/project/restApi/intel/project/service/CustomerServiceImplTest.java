package com.rabin.practice.project.restApi.intel.project.service;

import com.rabin.practice.project.restApi.intel.project.dto.CustomerDto;
import com.rabin.practice.project.restApi.intel.project.entity.CustomerEntity;
import com.rabin.practice.project.restApi.intel.project.exception.UserAlreadyExistException;
import com.rabin.practice.project.restApi.intel.project.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
