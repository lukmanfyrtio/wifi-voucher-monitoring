package com.lukman.wifi.voucer_tracking.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lukman.wifi.voucer_tracking.model.Voucher;
import com.lukman.wifi.voucer_tracking.service.VoucherService;

@RestController
@RequestMapping("/vouchers")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;

    @PostMapping("/create")
    public String createVoucher(
            @RequestParam("kodeVoucher") String kodeVoucher,
            @RequestParam("ipAddress") String ipAddress,
            @RequestParam("waktuLogin") String waktuLoginStr,
            @RequestParam("paket") String paket) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/dd/yyyy HH:mm:ss");
        LocalDateTime waktuLogin;
        waktuLoginStr = waktuLoginStr.substring(0, 1).toUpperCase() + waktuLoginStr.substring(1);
        try {
            waktuLogin = LocalDateTime.parse(waktuLoginStr, formatter);
        } catch (DateTimeParseException e) {
            return "Invalid date format. Please use the format 'MMM/dd/yyyy HH:mm:ss'.";
        }

        Voucher voucher = new Voucher();
        voucher.setKodeVoucher(kodeVoucher);
        voucher.setIpAddress(ipAddress);
        voucher.setWaktuLogin(waktuLogin);
        voucher.setPaket(paket);

        boolean success = voucherService.createVoucher(voucher);

        if (success) {
            return "Voucher created successfully. Expires at: " + voucher.getWaktuExpired();
        } else {
            return "Voucher is still valid in the same period and cannot be reused.";
        }
    }
}