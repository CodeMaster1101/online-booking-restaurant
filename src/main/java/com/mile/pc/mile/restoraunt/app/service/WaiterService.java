package com.mile.pc.mile.restoraunt.app.service;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mile.pc.mile.restoraunt.app.constants.CONSTANTS;
import com.mile.pc.mile.restoraunt.app.model.CustomTable;
import com.mile.pc.mile.restoraunt.app.model.User;
import com.mile.pc.mile.restoraunt.app.repo.CustomTableRepository;
import com.mile.pc.mile.restoraunt.app.repo.ReservationRepository;
import com.mile.pc.mile.restoraunt.app.repo.UserRepository;

@Service @Transactional
public class WaiterService {

	@Autowired CustomTableRepository tRepo;
	@Autowired ReservationRepository reservations;
	@Autowired UserRepository urepo;
	public void setAvailable(long tableid) {
		CustomTable table = tRepo.findById(tableid).get();
		if(!(settingTableAvaRequirements(table)))
			return;
        userArrived(table);
		table.setAvailable(true);
		table.setBusy(false);
		table.getReservation().getUser().setReservation(null);
		table.setReservation(null);
		tRepo.saveAndFlush(table);
	}
	public void setBusy(long tableid) {
		CustomTable table = tRepo.findById(tableid).get();
		if(!(settingTableUnaReq(table)))
			return;
		table.setAvailable(false);
		table.setBusy(true);
		table.setArrived(true);
		tRepo.saveAndFlush(table);
	}

	//PRIVATE HELPING METHODS

	private boolean settingTableAvaRequirements(CustomTable table) {
		if(table.getAvailable() != false) 
			return false;
		return true;

	}private boolean settingTableUnaReq(CustomTable table) {
		if(table.getAvailable() != true) {
			if(table.getAvailable() == null) {
			}else {
				return true;
			}
		}
		if(table.getBusy() == true)
			return false;
		return true;
	}
	private boolean checkUserOnTime(CustomTable table) {
		if(table.getReservation() == null) {
			return false;
		}
		User user = table.getReservation().getUser();
		LocalTime currentTime = LocalTime.now();
		if(!(currentTime.isAfter(CONSTANTS.parseLocalTime(user.getReservation().getTime())) &&
				currentTime.isBefore(CONSTANTS.parseLocalTime(user.getReservation().getTime()).plusMinutes(31)))) {
			return false;
		}
		return true;
	}
	private void userArrived(CustomTable table) {
		if(checkUserOnTime(table)) {
				User user = table.getReservation().getUser();
				user.setBalance(user.getBalance() + CONSTANTS.fee);
				urepo.saveAndFlush(user);
		}
	}
	public void setAvailableNoReser(long tableid) {
		CustomTable table = tRepo.findById(tableid).get();
		if(!(settingTableAvaRequirements(table)))
			return;
		table.setAvailable(true);
		table.setBusy(false);
		table.setReservation(null);
		tRepo.saveAndFlush(table);
	}

}
