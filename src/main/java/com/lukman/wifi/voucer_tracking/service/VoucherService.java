package com.lukman.wifi.voucer_tracking.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.lukman.wifi.voucer_tracking.model.Voucher;
import com.lukman.wifi.voucer_tracking.repo.VoucerRepository;

@Service
public class VoucherService {
	@Autowired
	private VoucerRepository voucherRepository;

	public boolean createVoucher(Voucher voucher) {
		Optional<Voucher> latestVoucherOpt = voucherRepository.findFirstByKodeVoucherOrderByWaktuLoginDesc(voucher.getKodeVoucher());

		if (latestVoucherOpt.isPresent()) {
			Voucher latestVoucher = latestVoucherOpt.get();
			if (latestVoucher.getWaktuExpired().isAfter(LocalDateTime.now())) {
				// Latest voucher is still valid, don't add a new one
				return false;
			}
		}

		// Calculate expiration time
		int hours = getDurationFromPaket(voucher.getPaket());
		voucher.setWaktuExpired(voucher.getWaktuLogin().plusHours(hours));
		System.out.println("Paket : "+voucher.getPaket() +", Hours added : "+hours);
		sendMessage(voucher);
		voucherRepository.save(voucher);
		return true;
	}

	private int getDurationFromPaket(String paket) {
		if (paket.contains("_")) {
			String[] parts = paket.split("_");
			if (parts.length == 2 && parts[1].endsWith("Jam")) {
				return Integer.parseInt(parts[1].replace("Jam", ""));
			}
		}
		throw new IllegalArgumentException("Invalid paket format");
	}
	
	public void sendMessage(Voucher voucher) {
		RestTemplate restTemplate = new RestTemplate();
        String formattedDatetime = voucher.getWaktuLogin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        String formattedDatetimeExpired = voucher.getWaktuExpired().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        String message = "🤖🤑INFO LOGIN🤑🤖"
        				+ "\n📶 Kode Voucher : " + voucher.getKodeVoucher() 
        				+ "\n📶 IP Address : " + voucher.getIpAddress() 
        				+ "\n📶 Waktu Login : " + formattedDatetime 
        				+ "\n📶 Paket : " + voucher.getPaket()
        				+ "\n📶 Waktu Expired : " + formattedDatetimeExpired;

		// Set headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// Create the request entity
		HttpEntity<String> requestEntity = new HttpEntity<>(headers);

		// Send the POST request
		ResponseEntity<String> response = restTemplate.postForEntity(
				"https://api.telegram.org/bot6779206424:AAHuU1XF4i-nQnUJaluvawwpHdUzWeGEVfM/sendMessage?chat_id=5631265269&text="+message,
				requestEntity, String.class);
		// Handle response if needed
		System.out.println(response.getBody());
	}
}
