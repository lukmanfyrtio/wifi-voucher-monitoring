package com.lukman.wifi.voucer_tracking.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "vouchers")
public class Voucher {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // Add an ID field for unique entries
	private String kodeVoucher;
	private String ipAddress;
	private LocalDateTime waktuLogin;
	private String paket;
	private LocalDateTime waktuExpired; // New field
}
