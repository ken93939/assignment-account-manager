package com.acmebank.accountmanager.config;

import com.acmebank.accountmanager.entity.Customer;
import com.acmebank.accountmanager.model.CustomUserDetails;
import com.acmebank.accountmanager.repository.CustomerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerService implements UserDetailsService {

	private CustomerRepository customerRepository;

	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Customer customer = customerRepository.findUserByUserName(userName);
		if (customer == null) {
			throw new UsernameNotFoundException(userName);
		}
		return new CustomUserDetails(customer);
	}
}
