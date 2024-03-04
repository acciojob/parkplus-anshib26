package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Reservation;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot newParkingLot = new ParkingLot();
        newParkingLot.setName(name);
        newParkingLot.setAddress(address);
        newParkingLot.setSpotList(new ArrayList<Spot>());
        return parkingLotRepository1.save(newParkingLot);
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        Spot newSpot = new Spot();
        newSpot.setPricePerHour(pricePerHour);
        newSpot.setOccupied(false);
        newSpot.setReservationList(new ArrayList<>());
        if(numberOfWheels==1 || numberOfWheels==2){
            newSpot.setSpotType(SpotType.TWO_WHEELER);
        }
        else if(numberOfWheels==3 || numberOfWheels==4){
            newSpot.setSpotType(SpotType.FOUR_WHEELER);
        }
        else{
            newSpot.setSpotType(SpotType.OTHERS);
        }
        newSpot.setParkingLot(parkingLot);
        parkingLot.getSpotList().add(newSpot);

        ParkingLot savedObj = parkingLotRepository1.save(parkingLot);
        return savedObj.getSpotList().get(savedObj.getSpotList().size()-1);
    }

    @Override
    public void deleteSpot(int spotId) {
        spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        List<Spot> spotList = parkingLot.getSpotList();
        Spot savedObj = new Spot();
        for(Spot spot : spotList){
            if(spot.getId()==spotId){
                spot.setPricePerHour(pricePerHour);
                savedObj = spotRepository1.save(spot);
            }
        }
        return savedObj;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
