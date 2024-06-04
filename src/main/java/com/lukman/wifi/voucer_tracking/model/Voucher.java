package com.lukman.wifi.voucer_tracking.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
