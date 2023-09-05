package com.rabin.practice.project.restApi.intel.project.entity;

import com.rabin.practice.project.restApi.intel.project.validation.ValidateCustomerName;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name="customer_entity")
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

   // @NotBlank(message = "name should not be null or empty")
    //custom validation
    @ValidateCustomerName
    private String username;
   @Pattern(regexp = "[a-zA-z0-9]*[@!#$%&]",message = "invalid password")
    private String password;

    @Pattern(regexp = "[a-zA-Z0-9_.]*@[a-zA-Z]+(\\.[a-zA-z]+)+",message = "invalid email id")
    private String email;

    @Pattern(regexp = "[0-9]{3}-[0-9]{3}-[0-9]{4}",message="invalid mobile number entered")
    private String phoneNumber;
    @Min(18)
    @Max(70)
    private String age;

    public CustomerEntity() {
    }

    public CustomerEntity(long id, String username, String password, String email, String phoneNumber, String age) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "CustomerEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
