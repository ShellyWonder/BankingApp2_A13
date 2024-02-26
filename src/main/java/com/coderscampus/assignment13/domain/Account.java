package com.coderscampus.assignment13.domain;

import java.util.ArrayList;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="accounts")
public class Account {
	private Long accountId;
	private String accountName;
	private List<Transaction> transactions = new ArrayList<>();
	private List<User> users = new ArrayList<>();
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	@Column(length = 100)
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	@OneToMany(mappedBy = "account")
	public List<Transaction> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
	@ManyToMany(mappedBy = "accounts")
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
}
