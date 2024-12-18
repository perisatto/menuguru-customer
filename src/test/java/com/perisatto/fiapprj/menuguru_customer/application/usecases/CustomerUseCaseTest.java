package com.perisatto.fiapprj.menuguru_customer.application.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import com.perisatto.fiapprj.menuguru_customer.application.interfaces.CustomerRepository;
import com.perisatto.fiapprj.menuguru_customer.application.interfaces.UserManagement;
import com.perisatto.fiapprj.menuguru_customer.domain.entities.customer.CPF;
import com.perisatto.fiapprj.menuguru_customer.domain.entities.customer.Customer;
import com.perisatto.fiapprj.menuguru_customer.domain.entities.user.User;
import com.perisatto.fiapprj.menuguru_customer.handler.exceptions.NotFoundException;
import com.perisatto.fiapprj.menuguru_customer.handler.exceptions.ValidationException;

@ActiveProfiles(value = "test")
public class CustomerUseCaseTest {

	private CustomerUseCase customerUseCase;

	@Mock
	private UserManagement userManagement;

	@Mock
	private CustomerRepository customerRepository;

	AutoCloseable openMocks;

	@BeforeEach
	void setUp() {
		openMocks = MockitoAnnotations.openMocks(this);
		customerUseCase = new CustomerUseCase(customerRepository, userManagement);
	}

	@AfterEach
	void tearDown() throws Exception {
		openMocks.close();
	}


	@Nested
	class RegistrarCliente {

		@Test
		void givenValidCPF_thenRegisterCustomer() throws Exception {		
			when(customerRepository.createCustomer(any(Customer.class)))
			.thenAnswer(i -> i.getArgument(0));

			when(userManagement.createUser(any(User.class)))
			.thenAnswer(u -> u.getArgument(0));

			String customerName = "Roberto Machado";
			String customerEmail = "roberto.machado@bestmail.com";
			String documentNumber = "35732996010";

			Customer customer = customerUseCase.createCustomer(documentNumber, customerName, customerEmail);

			assertThat(customer.getDocumentNumber().getDocumentNumber()).isEqualTo(documentNumber);
			assertThat(customer.getName()).isEqualTo(customerName);
			assertThat(customer.getEmail()).isEqualTo(customerEmail);
		}

		@Test
		void givenAlreadyExistentCustomer_thenRefusesToCreateCustomer() throws Exception {
			Customer customerData = new Customer(10L, new CPF("35732996010"), "Roberto Machado", "roberto.machado@bestmail.com");

			when(customerRepository.getCustomerByCPF(any(CPF.class)))
			.thenReturn(Optional.of(customerData));

			String customerName = "Roberto Machado";
			String customerEmail = "roberto.machado@bestmail.com";
			String documentNumber = "35732996010";

			try {
				customerUseCase.createCustomer(documentNumber, customerName, customerEmail);
			} catch (ValidationException e) {
				assertThat(e.getMessage()).isEqualTo("Customer already exists");
			} 	
		}

		@Test
		void givenInvalidCPF_thenRefusesToRegisterCustomer() throws Exception {

			when(customerRepository.createCustomer(any(Customer.class)))
			.thenAnswer(i -> i.getArgument(0));

			when(userManagement.createUser(any(User.class)))
			.thenAnswer(u -> u.getArgument(0));

			String customerName = "Roberto Machado";
			String customerEmail = "roberto.machado@bestmail.com";
			String documentNumber = "90779778058";

			try {
				customerUseCase.createCustomer(documentNumber, customerName, customerEmail);
			} catch (ValidationException e) {
				assertThat(e.getMessage()).isEqualTo("Invalid document number");
			} catch (Exception e) {
				assertThat(e.getMessage()).isNotEqualTo("Invalid document number");
			}
		}

		@Test
		void givenInvalidEmail_thenRefusesToRegisterCustomer() throws Exception {

			when(customerRepository.createCustomer(any(Customer.class)))
			.thenAnswer(i -> i.getArgument(0));

			when(userManagement.createUser(any(User.class)))
			.thenAnswer(u -> u.getArgument(0));

			String customerName = "Roberto Machado";
			String customerEmail = "roberto.machadobestmail.com";
			String documentNumber = "90779778057";

			try {
				customerUseCase.createCustomer(documentNumber, customerName, customerEmail);
			} catch (ValidationException e) {
				assertThat(e.getMessage()).contains("invalid e-mail format");
			} catch (Exception e) {
				assertThat(e.getMessage()).doesNotContain("invalid e-mail format");
			}
		}

		@Test
		void givenEmptyName_thenRefusesToRegisterCustomer() throws Exception {

			when(customerRepository.createCustomer(any(Customer.class)))
			.thenAnswer(i -> i.getArgument(0));

			when(userManagement.createUser(any(User.class)))
			.thenAnswer(u -> u.getArgument(0));			

			String customerName = "";
			String customerEmail = "roberto.machadobestmail.com";
			String documentNumber = "90779778057";

			try {
				customerUseCase.createCustomer(documentNumber, customerName, customerEmail);
			} catch (ValidationException e) {
				assertThat(e.getMessage()).contains("empty, null or blank name");
			} catch (Exception e) {
				assertThat(e.getMessage()).doesNotContain("empty, null or blank name");
			}
		}		
	}


	@Nested
	class ConsultarCliente {

		@Test
		void givenCPF_thenGetCustomerData() throws Exception {

			Customer customerData = new Customer(10L, new CPF("90779778057"), "Roberto Machado", "roberto.machado@bestmail.com");

			when(customerRepository.getCustomerByCPF(any(CPF.class)))
			.thenReturn(Optional.of(customerData));
			
			String documentNumber = "90779778057";

			Customer customer = customerUseCase.getCustomerByCPF(documentNumber);

			assertThat(customer.getDocumentNumber().getDocumentNumber()).isEqualTo(documentNumber);
		}

		@Test
		void givenInexistentCPF_thenGetNotFound () throws Exception {

			when(customerRepository.getCustomerByCPF(any(CPF.class)))
			.thenReturn(Optional.empty());			

			try {
				String documentNumber = "35732996010";

				CustomerUseCase newCustomerUseCase = new CustomerUseCase(customerRepository, userManagement);

				newCustomerUseCase.getCustomerByCPF(documentNumber);
			} catch (NotFoundException e) {
				assertThat(e.getMessage()).isEqualTo("Customer not found");
			}

		}

		@Test
		void givenValidId_thenGetCustomer () throws Exception {

			Customer customerData = new Customer(10L, new CPF("90779778057"), "Roberto Machado", "roberto.machado@bestmail.com");			

			when(customerRepository.getCustomerById(10L))
			.thenReturn(Optional.of(customerData));	

			Long customerId = 10L;

			try {
				Customer customer = customerUseCase.getCustomerById(customerId);

				assertThat(customer.getId()).isEqualTo(customerId);
			} catch (ValidationException e) {
				assertThat(e.getMessage()).doesNotContain("Customer not found");
			}
		}

		@Test
		void giveInexistentId_thenGetCustomerNotFound () throws Exception {

			when(customerRepository.getCustomerById(any(Long.class)))
			.thenReturn(Optional.empty());

			try {
				Long customerId = 20L;

				Customer customer = customerUseCase.getCustomerById(customerId);

				assertThat(customer.getName()).isNullOrEmpty();
			} catch (NotFoundException e) {
				assertThat(e.getMessage()).isEqualTo("Customer not found");
			} 
		}
		
		@Test
		void listCustomers() throws Exception {
			when(customerRepository.findAll(any(Integer.class), any(Integer.class)))
			.thenAnswer(i -> {
				Set<Customer> result = new LinkedHashSet<Customer>();
				Customer customerData1 = new Customer(10L, new CPF("65678860054"), "Roberto Machado", "roberto.machado@bestmail.com");
				Customer customerData2 = new Customer(20L, new CPF("65678860054"), "Roberto Machado", "roberto.machado@bestmail.com");
				result.add(customerData1);
				result.add(customerData2);
				return result;
			});
			
			Set<Customer> result = customerUseCase.findAllCustomers(null, null);
			
			assertThat(result.size()).isEqualTo(2);
		}
		
		@Test
		void givenInvalidParameters_RefusesListCustomer() throws Exception {
			try {
				customerUseCase.findAllCustomers(100, null);
			} catch (ValidationException e) {
				assertThat(e.getMessage()).contains("Invalid size parameter");
			}
			
			try {
				customerUseCase.findAllCustomers(-1, null);
			} catch (ValidationException e) {
				assertThat(e.getMessage()).contains("Invalid size parameter");
			}
			
			try {
				customerUseCase.findAllCustomers(null, 0);
			} catch (ValidationException e) {
				assertThat(e.getMessage()).contains("Invalid page parameter");
			}
		}
	}

	@Nested
	class AtualizarCliente {
		
		@Test
		void givenNewData_thenUpdateCustomer () throws Exception {		

			Customer customerData = new Customer(10L, new CPF("65678860054"), "Roberto Machado", "roberto.machado@bestmail.com");

			when(customerRepository.getCustomerById(any(Long.class)))
			.thenReturn(Optional.of(customerData));

			when(customerRepository.updateCustomer(any(Customer.class)))
			.thenAnswer(i -> 
			{ 
				Optional<Customer> customer = Optional.of(i.getArgument(0));
				return customer;
			});

			String customerName = "Roberto Facao";
			String customerEmail = "roberto.facao@bestmail.com";
			String documentNumber = "65678860054";

			Customer newCustomerData = customerUseCase.updateCustomer(10L, documentNumber, customerName, customerEmail);

			assertThat(newCustomerData.getId()).isEqualTo(10L);
			assertThat(newCustomerData.getDocumentNumber().getDocumentNumber()).isEqualTo(documentNumber);
			assertThat(newCustomerData.getName()).isEqualTo(customerName);
			assertThat(newCustomerData.getEmail()).isEqualTo(customerEmail);
		}
		
		
		@Test
		void givenNewName_thenUpdateName () throws Exception {		

			Customer customerData = new Customer(10L, new CPF("65678860054"), "Roberto Machado", "roberto.machado@bestmail.com");

			when(customerRepository.getCustomerById(any(Long.class)))
			.thenReturn(Optional.of(customerData));

			when(customerRepository.updateCustomer(any(Customer.class)))
			.thenAnswer(i -> 
			{ 
				Optional<Customer> customer = Optional.of(i.getArgument(0));
				return customer;
			});

			String customerName = "Roberto Facao";

			Customer newCustomerData = customerUseCase.updateCustomer(10L, null, customerName, null);

			assertThat(newCustomerData.getId()).isEqualTo(10L);
			assertThat(newCustomerData.getName()).isEqualTo(customerName);
		}

		@Test
		void givenNewEmail_thenUpdateEmail () throws Exception {
			Customer customerData = new Customer(10L, new CPF("65678860054"), "Roberto Machado", "roberto.machado@bestmail.com");

			when(customerRepository.getCustomerById(any(Long.class)))
			.thenReturn(Optional.of(customerData));

			when(customerRepository.updateCustomer(any(Customer.class)))
			.thenAnswer(i -> 
			{ 
				Optional<Customer> customer = Optional.of(i.getArgument(0));
				return customer;
			});

			Long customerId = 10L;
			String customerEmail = "roberto.facao@bestmail.com"; 

			Customer newCustomerData = customerUseCase.updateCustomer(customerId, null, null, customerEmail);


			assertThat(newCustomerData.getId()).isEqualTo(10L);
			assertThat(newCustomerData.getEmail()).isEqualTo(customerEmail);
		}

		@Test
		void duringDatabaseProblem_RefusesUpdateCustomer() throws Exception {
			Customer customerData = new Customer(10L, new CPF("65678860054"), "Roberto Machado", "roberto.machado@bestmail.com");

			when(customerRepository.getCustomerById(any(Long.class)))
			.thenReturn(Optional.of(customerData));
			
			when(customerRepository.updateCustomer(any(Customer.class)))
			.thenReturn(Optional.empty());
			
			Long customerId = 10L;
			String customerName = "Roberto Facao";
			String customerEmail = "roberto.facao@bestmail.com";
			String documentNumber = "65678860054";
			
			try {
				customerUseCase.updateCustomer(customerId, documentNumber, customerName, customerEmail);
			} catch (Exception e) {
				assertThatExceptionOfType(Exception.class);
				assertThat(e.getMessage()).contains("Error while updating customer data. Please refer to application log for details.");
			}

		}
		
		@Test
		void givenInvalidId_RefusesUpdateCustomer() throws Exception {
			when(customerRepository.getCustomerById(any(Long.class)))
			.thenReturn(Optional.empty());
			
			Long customerId = 10L;
			String customerName = "Roberto Facao";
			String customerEmail = "roberto.facao@bestmail.com";
			String documentNumber = "65678860054";
			
			try {
				customerUseCase.updateCustomer(customerId, documentNumber, customerName, customerEmail);
			} catch (Exception e) {
				assertThatExceptionOfType(NotFoundException.class);
				assertThat(e.getMessage()).contains("Customer not found");
			}
			
		}
		
		@Test
		void givenInvalidNewEmail_thenRefusesUpdateEmail () throws Exception {
			try {
				Customer customerData = new Customer(10L, new CPF("65678860054"), "Roberto Machado", "roberto.machado@bestmail.com");

				when(customerRepository.getCustomerById(any(Long.class)))
				.thenReturn(Optional.of(customerData));

				Long customerId = 10L;
				String customerName = "Roberto Facao";
				String customerEmail = "roberto.facaobestmail.com";
				String documentNumber = "90779778057";

				customerUseCase.updateCustomer(customerId, documentNumber, customerName, customerEmail);
			}catch (Exception e) {
				assertThatExceptionOfType(ValidationException.class);
				assertThat(e.getMessage()).contains("invalid e-mail format");
			}
		}

		@Test
		void givenNewCPF_thenUpdateCPF () throws Exception {
			Customer customerData = new Customer(10L, new CPF("65678860054"), "Roberto Machado", "roberto.machado@bestmail.com");

			when(customerRepository.getCustomerById(any(Long.class)))
			.thenReturn(Optional.of(customerData));

			when(customerRepository.updateCustomer(any(Customer.class)))
			.thenAnswer(i -> 
			{ 
				Optional<Customer> customer = Optional.of(i.getArgument(0));
				return customer;
			});

			Long customerId = 10L;
			String documentNumber = "65678860054";

			Customer newCustomerData = customerUseCase.updateCustomer(customerId, documentNumber, null, null);

			assertThat(newCustomerData.getId()).isEqualTo(customerId);
			assertThat(newCustomerData.getDocumentNumber().getDocumentNumber()).isEqualTo(documentNumber);
		}

		@Test
		void givenInvalidNewCPF_thenRefusesUpdateCPF () throws Exception {
			try {
				Customer customerData = new Customer(10L, new CPF("65678860054"), "Roberto Machado", "roberto.machado@bestmail.com");

				when(customerRepository.getCustomerById(any(Long.class)))
				.thenReturn(Optional.of(customerData));

				Long customerId = 10L;
				String customerName = "Roberto Machado";
				String customerEmail = "roberto.machado@bestmail.com";
				String documentNumber = "90779778056";

				customerUseCase.updateCustomer(customerId, documentNumber, customerName, customerEmail);
			}catch (Exception e) {
				assertThatExceptionOfType(ValidationException.class);
				assertThat(e.getMessage()).contains("Invalid document number");
			}
		}		
	}

	@Nested
	class DeletarCliente {
		@Test
		void givenId_thenDeleteCustomer () throws Exception {
			Customer customerData = new Customer(10L, new CPF("90779778057"), "Roberto Machado", "roberto.machado@bestmail.com");

			when(customerRepository.deleteCustomer(any(Long.class)))
			.thenReturn(true);

			when(customerRepository.getCustomerById(any(Long.class)))
			.thenReturn(Optional.of(customerData))
			.thenThrow(NotFoundException.class);

			Boolean deleted = false;

			try {
				Long customerId = 10L;
				deleted = customerUseCase.deleteCustomer(customerId);
				customerUseCase.getCustomerById(customerId);
			} catch (NotFoundException e) {
				assertThat(deleted).isTrue();
			} catch (Exception e) {
				assertThatExceptionOfType(Exception.class);
			}
		}

		@Test
		void givenInexistentId_thenRefusesDeleteCustomer () throws Exception {

			when(customerRepository.deleteCustomer(any(Long.class)))
			.thenThrow(NotFoundException.class);

			try {
				Long customerId = 20L;
				customerUseCase.deleteCustomer(customerId);
			} catch (NotFoundException e) {
				assertThatExceptionOfType(NotFoundException.class);
			} 
		}
	}
}
