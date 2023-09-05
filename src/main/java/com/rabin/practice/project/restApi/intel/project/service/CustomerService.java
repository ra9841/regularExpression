package com.rabin.practice.project.restApi.intel.project.service;

import com.rabin.practice.project.restApi.intel.project.dto.CustomerDto;

import java.util.List;

public interface CustomerService {
    CustomerDto savingCustomerDetails(CustomerDto customerDto);


    List<CustomerDto> getListOFRecords();

    CustomerDto updateRecordByUsername(CustomerDto customerDto, String username) throws NoSuchFieldException;

    CustomerDto getParticularRecordOfUsername(String username) throws NoSuchFieldException;

    String deleteCustomerRecord(String username) ;
}
