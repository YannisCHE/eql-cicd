package com.formation;

import com.formation.service.CustomerService;
import com.formation.web.error.ConflictException;
import com.formation.web.error.NotFoundException;
import com.formation.web.model.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class CustomerServiceIntegrationTest {

    @Autowired
    CustomerService customerService;

    @Test
    void getAllCustomer(){
        List<Customer> customers = customerService.getAllCustomers();

        Assertions.assertEquals(5,customers.size());
    }

    @Test
    void getCustomer(){
        Customer customer = customerService.getCustomer("3b6c3ecc-fad7-49db-a14a-f396ed866e50");

        Assertions.assertNotNull(customer);

        Assertions.assertEquals("Brooke", customer.getFirstName());
    }

    @Test
    void getCustomer_NotFound(){
        Assertions.assertThrows(NotFoundException.class, () -> customerService.getCustomer("b6c3ecc-fad7-49db-a14a-f396ed8")
        ,"Should have thrown an exception");
    }

    @Test
    void addCustomer(){
        Customer customer = new Customer("b6c3ecc-fad7-49db-a14a-f396ed8","Yannis",
                "Chenguiti","chenguiti@email.com","123456789","adresse");

        customer = customerService.addCustomer(customer);

        Assertions.assertTrue(StringUtils.isNotBlank(customer.getCustomerId()));

        Assertions.assertEquals("Yannis", customer.getFirstName());

        customerService.deleteCustomer(customer.getCustomerId());
    }

    @Test
    void addCustomer_alreadyExist(){

        Customer customer = new Customer("", "John", "Doe"
                , "penatibus.et@lectusa.com", "555-515-1234", "1234 Main Street; Anytown, KS 66110");

        Assertions.assertThrows(ConflictException.class, () -> customerService.addCustomer(customer));
    }

    @Test
    void updateCustomer(){

        Customer customer = new Customer("", "John", "Doe", "jdoe@test.com", "555-515-1234",
                "1234 Main Street; Anytown, KS 66110");

        customer = customerService.addCustomer(customer);

        customer.setFirstName("Yannis");

        customer = customerService.updateCustomer(customer);

        Assertions.assertEquals("Yannis", customer.getFirstName());

        customerService.deleteCustomer(customer.getCustomerId());
    }
}
