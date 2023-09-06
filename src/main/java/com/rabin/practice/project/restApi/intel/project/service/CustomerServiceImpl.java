package com.rabin.practice.project.restApi.intel.project.service;

import com.rabin.practice.project.restApi.intel.project.controller.MyController;
import com.rabin.practice.project.restApi.intel.project.dto.CustomerDto;
import com.rabin.practice.project.restApi.intel.project.entity.CustomerEntity;
import com.rabin.practice.project.restApi.intel.project.exception.UserAlreadyExistException;
import com.rabin.practice.project.restApi.intel.project.repository.CustomerRepository;
import io.micrometer.observation.annotation.Observed;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    private final static Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    @Observed(name="saving.customer")//custom metrics
    public CustomerDto savingCustomerDetails(CustomerDto customerDto) {
        CustomerEntity customerEntity = new CustomerEntity();
        BeanUtils.copyProperties(customerDto, customerEntity);
        Optional<CustomerEntity> exist = customerRepository.findByUsername(customerDto.getUsername());
        if (exist.isPresent()) {
            log.info("userAlready existed in record:" + customerDto.getUsername());
            throw new UserAlreadyExistException("user already exist");

        } else {
            customerRepository.save(customerEntity);
            log.info("Customer record saved in database:" + customerEntity);
            return customerDto;
        }
    }

    @Override
    @Observed(name="get.customer")
    public List<CustomerDto> getListOFRecords() {
        List<CustomerEntity> customerEntities = customerRepository.findAll();
        List<CustomerDto> customerDtos = new ArrayList<>();
        for (CustomerEntity customer : customerEntities) {
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);
            customerDtos.add(customerDto);
        }
        log.info("List of Record from database:" + customerDtos);
        return customerDtos;
    }


    @Override
    @Cacheable(cacheNames = "customer",key="#username")
    @Observed(name="get.particularCustomers")
    public CustomerDto getParticularRecordOfUsername(String username) throws NoSuchFieldException {
       log.info("fetching customer record form database");
        Optional<CustomerEntity> existUsername = customerRepository.findByUsername(username);
        if (existUsername.isPresent()) {
            CustomerEntity customerEntity = existUsername.get();
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customerEntity, customerDto);
            return customerDto;
        } else {
            throw new NoSuchFieldException("usernot found");
        }

    }


    @Override
   @CachePut(cacheNames = "customer", key = "#customerDto.username")
    public CustomerDto updateRecordByUsername(CustomerDto customerDto, String username) throws NoSuchFieldException {
        CustomerDto customerDto1 = getParticularRecordOfUsername(username);
        customerDto1.setUsername(customerDto.getUsername());
        customerDto1.setPassword(customerDto.getPassword());
        customerDto1.setEmail(customerDto.getEmail());
        customerDto1.setAge(customerDto.getAge());
        customerDto1.setPhoneNumber(customerDto.getPhoneNumber());

        Optional<CustomerEntity> existCustomer = customerRepository.findByUsername(username);
        if (existCustomer.isPresent()) {
            CustomerEntity customerEntity1 = existCustomer.get();
            customerEntity1.setEmail(customerDto1.getEmail());
            customerEntity1.setUsername(customerDto1.getUsername());
            customerEntity1.setPassword(customerDto1.getPassword());
            customerRepository.save(customerEntity1);
            log.info("customer update with username:"+ customerEntity1);
            CustomerDto customer = new CustomerDto();
            BeanUtils.copyProperties(customerEntity1, customer);
            return customer;
        } else {
            throw new NoSuchFieldException("usernot found");
        }


    }


    @Override
    @CacheEvict(cacheNames = "customer",key="#username")
    public String deleteCustomerRecord(String username) {
        Optional<CustomerEntity> existCustomer = customerRepository.findByUsername(username);
        if (existCustomer.isPresent()) {
            log.info("deleted data from database:"+ existCustomer);
            customerRepository.delete(existCustomer.get());
            return "delete successfully";
        } else {
            return "user not exist";
        }

    }


}
