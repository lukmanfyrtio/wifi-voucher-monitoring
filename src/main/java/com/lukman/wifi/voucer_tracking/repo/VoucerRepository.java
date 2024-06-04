package com.lukman.wifi.voucer_tracking.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lukman.wifi.voucer_tracking.model.Voucher;

public interface VoucerRepository extends JpaRepository<Voucher, Long> {
	 Optional<Voucher> findFirstByKodeVoucherOrderByWaktuLoginDesc(String kodeVoucher);
}
