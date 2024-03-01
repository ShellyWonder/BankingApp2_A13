package com.coderscampus.assignment13.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.repository.UserRepository;
import com.coderscampus.assignment13.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
    @Autowired
    private AccountRepository accountRepo;

    
    public UserService(UserRepository userRepo, AccountRepository accountRepo) {
        this.userRepo = userRepo;
        this.accountRepo = accountRepo;
    }

	public List<User> findByUsername(String username) {
		return userRepo.findByUsername(username);
	}

	public List<User> findByNameAndUsername(String name, String username) {
		return userRepo.findByNameAndUsername(name, username);
	}

	public List<User> findByCreatedDateBetween(LocalDate date1, LocalDate date2) {
		return userRepo.findByCreatedDateBetween(date1, date2);
	}

	public User findExactlyOneUserByUsername(String username) {
		List<User> users = userRepo.findExactlyOneUserByUsername(username);
		if (users.size() == 1) {
			return users.get(0);
		} else if (users.size() > 1) {

			System.out.println("More than one user found with username: " + username);

			return null;
		} else {
			return new User();
		}
	}

	public Set<User> findAll() {
		return userRepo.findAllUsersWithAccountsAndAddresses();
	}

	public User findById(Long userId) {
		Optional<User> userOpt = userRepo.findById(userId);
		return userOpt.orElse(new User());
	}

	@Transactional
	public User updateUserAndAddress(User updatedUser) {
    // Check if the user already exists in the database
    Optional<User> existingUserOpt = userRepo.findById(updatedUser.getUserId());
    if (existingUserOpt.isPresent()) {
        User existingUser = existingUserOpt.get();

        // Update fields on the existingUser with values from updatedUser
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setName(updatedUser.getName());
        // ...update other fields as necessary

        // Check and update address
        Address updatedAddress = updatedUser.getAddress();
        if (updatedAddress != null) {
            // update existing address
            if (existingUser.getAddress() != null) {
                Address existingAddress = existingUser.getAddress();
                existingAddress.setAddressLine1(updatedAddress.getAddressLine1());
                existingAddress.setAddressLine2(updatedAddress.getAddressLine2());
                existingAddress.setCity(updatedAddress.getCity());
                existingAddress.setRegion(updatedAddress.getRegion());
                existingAddress.setCountry(updatedAddress.getCountry());
                existingAddress.setZipCode(updatedAddress.getZipCode());
                // Since the existing address is already associated with the user, no need to set the user again
            } else {
                // If the existing user does not have an address, associate the new address with the user
                updatedAddress.setUser(existingUser);
                existingUser.setAddress(updatedAddress);
            }
        }

        // Save the updated existingUser back to the database
        return userRepo.save(existingUser);
    } else {
        // Handle case where the user does not exist, perhaps throw an exception
        throw new EntityNotFoundException("User with ID " + updatedUser.getUserId() + " not found.");
    }
}

@Transactional
public Account createAccountForUser(Long userId, Account account) {
    User user = userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
    account.setUser(user); // Associate the account with the user
    Account savedAccount = accountRepo.save(account); // Save the account, which is now linked to the user
    
    // The user object will automatically be updated because of the cascading settings
    
    return savedAccount; // Return the saved account
}

public User saveUser(User user) {
    if (user.getUserId() == null) {
        Account checking = new Account();
        checking.setAccountName("Checking Account");
        // Link the account to the user
        checking.setUser(user);

        Account savings = new Account();
        savings.setAccountName("Savings Account");
        // Link the account to the user
        savings.setUser(user);

        // Since CascadeType.ALL is set, adding accounts to the user's list is enough
        // Hibernate will automatically save the accounts when the user is saved
        user.getAccounts().add(checking);
        user.getAccounts().add(savings);

        // Save the user and its associated accounts
    
	}
    return userRepo.save(user);
}


	public void delete(Long userId) {
		userRepo.deleteById(userId);
	}
}


