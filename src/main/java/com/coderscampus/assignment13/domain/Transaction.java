package com.coderscampus.assignment13.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="transactions")
public class Transaction {
	private Long transactionId;
	private LocalDateTime transactionDate;
	private Double amount;
	private String type;
	private Account account;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	@Column(length = 1)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@ManyToOne
	@JoinColumn(name="account_id")
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
}
