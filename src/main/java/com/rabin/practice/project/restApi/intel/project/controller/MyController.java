package com.rabin.practice.project.restApi.intel.project.controller;

import jakarta.validation.Valid;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.rabin.practice.project.restApi.intel.project.dto.CustomerDto;
import com.rabin.practice.project.restApi.intel.project.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

@RestController
@RequestMapping("/v")
public class MyController {
    private static final Logger log = LoggerFactory.getLogger(MyController.class);

    @Autowired
    private CustomerService customerService;

    @PostMapping("/registers")
    public CustomerDto registeringCustomerDetails(@RequestBody @Valid CustomerDto customerDto) {
        log.info("data from controller:" + customerDto);
        return customerService.savingCustomerDetails(customerDto);

    }


    @GetMapping("/shows")
    public ResponseEntity<List<CustomerDto>> gettingAllRecords() {
        List<CustomerDto> customerDtos = customerService.getListOFRecords();
        log.info("List of Record from controller:" + customerDtos);
        return ResponseEntity.ok(customerDtos);

    }

    @GetMapping("/shows/{username}")
    public ResponseEntity<CustomerDto> getRecordOnBasisOfUsername(@PathVariable String username) throws NoSuchFieldException {
        CustomerDto customerDto=customerService.getParticularRecordOfUsername(username);
        return  ResponseEntity.ok(customerDto);
    }

    @PutMapping("/updates/{username}")
    public ResponseEntity<CustomerDto> updatingRecordBasisOfUsername(@RequestBody @Valid CustomerDto customerDto,@PathVariable String username) throws NoSuchFieldException{
        CustomerDto customerDto1=customerService.updateRecordByUsername(customerDto,username);
        log.info("Record from controller:"+customerDto1);
        if(customerDto1==null){
            log.info("Record from controller exception:"+customerDto1);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            log.info("Record from controller else statement:"+customerDto1);
            return new ResponseEntity<>(customerDto1,HttpStatus.CREATED);
        }
    }


    @DeleteMapping("/deletes/{username}")
    public String deletedTheRecord(@PathVariable String username)  {
        String message=customerService.deleteCustomerRecord(username);
        if(message.equals("delete successfully")){
            return "delete successfully";
        }else{
            return "user not exist";
        }


    }
}
