package com.perisatto.fiapprj.menuguru_customer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.perisatto.fiapprj.menuguru_customer.application.interfaces.CustomerRepository;
import com.perisatto.fiapprj.menuguru_customer.application.interfaces.OrderRepository;
import com.perisatto.fiapprj.menuguru_customer.application.interfaces.PaymentProcessor;
import com.perisatto.fiapprj.menuguru_customer.application.interfaces.PaymentRepository;
import com.perisatto.fiapprj.menuguru_customer.application.interfaces.ProductRepository;
import com.perisatto.fiapprj.menuguru_customer.application.usecases.OrderUseCase;
import com.perisatto.fiapprj.menuguru_customer.infra.gateways.OrderRepositoryJpa;
import com.perisatto.fiapprj.menuguru_customer.infra.gateways.mappers.OrderMapper;
import com.perisatto.fiapprj.menuguru_customer.infra.persistences.repositories.OrderPersistenceRepository;

@Configuration
public class OrderConfig {

	
	@Bean
	OrderUseCase orderUseCase(OrderRepository orderRepository, CustomerRepository customerRepository, ProductRepository productRepository, 
			PaymentProcessor paymentProcessor, PaymentRepository paymentRepository) {
		return new OrderUseCase(orderRepository, customerRepository, productRepository, paymentProcessor, paymentRepository);
	}
	
	@Bean
	OrderRepositoryJpa orderJpaRepository(OrderPersistenceRepository orderPersistenceRepository, OrderMapper orderMapper) {
		return new OrderRepositoryJpa(orderPersistenceRepository, orderMapper);
	}
	
	@Bean
	OrderMapper orderMapper() {
		return new OrderMapper();
	}
}
