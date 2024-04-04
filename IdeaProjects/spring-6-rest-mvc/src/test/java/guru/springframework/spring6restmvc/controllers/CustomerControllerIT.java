package guru.springframework.spring6restmvc.controllers;

import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
class CustomerControllerIT {

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;


    @Test
    void testPatchCustomerNotFound() {
        assertThrows(NotFoundException.class, () -> customerController.patchById(UUID.randomUUID(), CustomerDTO.builder().build()));
    }

    @Rollback
    @Transactional
    @Test
    void testPatchCustomer() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO dto = customerMapper.customerToCustomerDto(customer);
        dto.setId(null);
        dto.setVersion(null);
        final String newName = "Updated customer";
        dto.setName(newName);

        ResponseEntity responseEntity = customerController.patchById(customer.getId(), dto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(customerRepository.findById(customer.getId()).get().getName()).isEqualTo(newName);
    }

    @Test
    void testUpdateCustomerNotFound() {
        assertThrows(NotFoundException.class, () -> customerController.updateById(UUID.randomUUID(), CustomerDTO.builder().build()));
    }

    @Rollback
    @Transactional
    @Test
    void testUpdateCustomer() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO dto = customerMapper.customerToCustomerDto(customer);
        dto.setId(null);
        dto.setVersion(null);
        final String newName = "Updated customer";
        dto.setName(newName);

        ResponseEntity responseEntity = customerController.updateById(customer.getId(), dto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(customerRepository.findById(customer.getId()).get().getName()).isEqualTo(newName);
    }

    @Test
    void testDeleteCustomerNotFound() {
        assertThrows(NotFoundException.class, () -> customerController.deleteById(UUID.randomUUID()));
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteCustomerFound() {
        Customer customer = customerRepository.findAll().get(0);
        ResponseEntity responseEntity = customerController.deleteById(customer.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(customerRepository.findById(customer.getId())).isEmpty();
    }

    @Transactional
    @Rollback
    @Test
    void testSaveNewCustomer() {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .name("new customer")
                .build();

        ResponseEntity responseEntity = customerController.handlePost(customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Customer customer = customerRepository.findById(savedUUID).get();
        assertThat(customer).isNotNull();
    }

    @Test
    void testGetByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.getCustomerById(UUID.randomUUID());
        });
    }

    @Test
    void testGetById() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO dto = customerController.getCustomerById(customer.getId());
        assertThat(dto).isNotNull();
    }

    @Test
    void testListCustomers() {
        List<CustomerDTO> dtos = customerController.getAllCustomers();
        assertThat(dtos.size()).isEqualTo(3);
    }


    @Rollback
    @Transactional
    @Test
    void testEmptyCustomers() {
        customerRepository.deleteAll();

        List<CustomerDTO> dtos = customerController.getAllCustomers();
        assertThat(dtos.size()).isEqualTo(0);
    }
}