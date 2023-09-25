package com.rabin.practice.project.restApi.intel.project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabin.practice.project.restApi.intel.project.dto.CustomerDto;
import com.rabin.practice.project.restApi.intel.project.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MyController.class)
class MyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService mockCustomerService;

    @Test
    void testRegisteringCustomerDetails() throws Exception {
        // Setup
        // Configure CustomerService.savingCustomerDetails(...).
        final CustomerDto customerDto = new CustomerDto("username", "password", "email", "phoneNumber", "age");
        when(mockCustomerService.savingCustomerDetails(any(CustomerDto.class))).thenReturn(customerDto);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/v/registers")
                        .content(asJasonString(customerDto)).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    }

    private String asJasonString(CustomerDto customerDto) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(customerDto);
    }

    @Test
    void testGettingAllRecords() throws Exception {
        // Setup
        // Configure CustomerService.getListOFRecords(...).
        final List<CustomerDto> customerDtos = List.of(
                new CustomerDto("username", "password", "email", "phoneNumber", "age"));
        when(mockCustomerService.getListOFRecords()).thenReturn(customerDtos);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/v/shows")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    void testGettingAllRecords_CustomerServiceReturnsNoItems() throws Exception {
        // Setup
        when(mockCustomerService.getListOFRecords()).thenReturn(Collections.emptyList());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/v/shows")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    void testGetRecordOnBasisOfUsername() throws Exception {
        // Setup
        // Configure CustomerService.getParticularRecordOfUsername(...).
        final CustomerDto customerDto = new CustomerDto("username", "password", "email", "phoneNumber", "age");
        when(mockCustomerService.getParticularRecordOfUsername("username")).thenReturn(customerDto);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/v/shows/{username}", "username")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    }

    //not correct due to throws exception
    @Test
    void testGetRecordOnBasisOfUsername_CustomerServiceThrowsNoSuchFieldException() throws Exception {
        // Setup
        when(mockCustomerService.getParticularRecordOfUsername("username")).thenThrow(NoSuchFieldException.class);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/v/shows/{username}", "username")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());



    }

    @Test
    void testUpdatingRecordBasisOfUsername() throws Exception {
        // Setup
        // Configure CustomerService.updateRecordByUsername(...).
        final CustomerDto customerDto = new CustomerDto("username", "password", "email", "phoneNumber", "age");
        when(mockCustomerService.updateRecordByUsername(any(CustomerDto.class), eq("username")))
                .thenReturn(customerDto);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(put("/v/updates/{username}", "username")
                        .content(asJasonString(customerDto)).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

    }

    @Test
    void testUpdatingRecordBasisOfUsername_CustomerServiceReturnsNull() throws Exception {
        // Setup
        when(mockCustomerService.updateRecordByUsername(any(CustomerDto.class), eq("username"))).thenReturn(null);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(put("/v/updates/{username}", "username")
                        .content(asJasonString(new CustomerDto())).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());

    }
    //not correct due to throws exception
    @Test
    void testUpdatingRecordBasisOfUsername_CustomerServiceThrowsNoSuchFieldException() throws Exception {
        // Setup
        when(mockCustomerService.updateRecordByUsername(any(CustomerDto.class), eq("username")))
                .thenThrow(NoSuchFieldException.class);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(put("/v/updates/{username}", "username")
                        .content(asJasonString(new CustomerDto())).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());

    }

    @Test
    void testDeletedTheRecord() throws Exception {
        // Setup
        when(mockCustomerService.deleteCustomerRecord("username")).thenReturn("result");

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(delete("/v/deletes/{username}", "username")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    void testGetRecordByEmail() throws Exception {
        // Setup
        // Configure CustomerService.getCustomerRecordByEmail(...).
        final CustomerDto customerDto = new CustomerDto("username", "password", "email", "phoneNumber", "age");
        when(mockCustomerService.getCustomerRecordByEmail("email")).thenReturn(customerDto);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/v/{email}", "email")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    }


    //not correct due to throws exception
    @Test
    void testGetRecordByEmail_CustomerServiceThrowsNoSuchFieldException() throws Exception {
        // Setup
        when(mockCustomerService.getCustomerRecordByEmail("email")).thenThrow(NoSuchFieldException.class);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/v/{email}", "email")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

    }

    @Test
    void testGetListOfRecordOnUsernameAscending() throws Exception {
        // Setup
        // Configure CustomerService.getListOfUsernameOnAscending(...).
        final List<CustomerDto> customerDtos = List.of(
                new CustomerDto("username", "password", "email", "phoneNumber", "age"));
        when(mockCustomerService.getListOfUsernameOnAscending()).thenReturn(customerDtos);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/v/ascSorting")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    void testGetListOfRecordOnUsernameAscending_CustomerServiceReturnsNoItems() throws Exception {
        // Setup
        when(mockCustomerService.getListOfUsernameOnAscending()).thenReturn(Collections.emptyList());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/v/ascSorting")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    void testGetListOfRecordOnUsernameDscending() throws Exception {
        // Setup
        // Configure CustomerService.getListOfUsernameOnDscending(...).
        final List<CustomerDto> customerDtos = List.of(
                new CustomerDto("username", "password", "email", "phoneNumber", "age"));
        when(mockCustomerService.getListOfUsernameOnDscending()).thenReturn(customerDtos);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/v/dscSorting")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    void testGetListOfRecordOnUsernameDscending_CustomerServiceReturnsNoItems() throws Exception {
        // Setup
        when(mockCustomerService.getListOfUsernameOnDscending()).thenReturn(Collections.emptyList());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/v/dscSorting")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }
}
