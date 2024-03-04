package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        //Reserve a spot in the given parkingLot such that the total price is minimum. Note that the price per hour for each spot is different
        //Note that the vehicle can only be parked in a spot having a type equal to or larger than given vehicle
        //If parkingLot is not found, user is not found, or no spot is available, throw "Cannot make reservation" exception.

        Reservation newReservation = new Reservation();
        newReservation.setNumberOfHours(timeInHours);

        if(parkingLotRepository3.findById(parkingLotId)==null || userRepository3.findById(userId)==null){
            throw new Exception("Cannot make reservation");
        }

        ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();
        User user = userRepository3.findById(userId).get();

        int flag = 0;
        int price = Integer.MAX_VALUE;
        Spot reserveSpot = new Spot();
        List<Spot> spotList = parkingLot.getSpotList();
        for(Spot spot : spotList){
            if(!spot.getOccupied()){
                if((numberOfWheels<=2) && (spot.getSpotType().equals(SpotType.TWO_WHEELER))) {
                    if(spot.getPricePerHour()<price){
                        price = spot.getPricePerHour();
                        reserveSpot = spot;
                        flag = 1;
                    }
                }
                else if((numberOfWheels<=4) && (spot.getSpotType().equals(SpotType.FOUR_WHEELER))) {
                    if(spot.getPricePerHour()<price){
                        price = spot.getPricePerHour();
                        reserveSpot = spot;
                        flag = 1;
                    }
                }
                else {
                    if (spot.getPricePerHour() < price) {
                        price = spot.getPricePerHour();
                        reserveSpot = spot;
                        flag = 1;
                    }
                }
            }
        }

        if(flag==0){
            throw new Exception("Cannot make reservation");
        }

        //price *= timeInHours;
        reserveSpot.setOccupied(true);

        newReservation.setUser(user);
        newReservation.setSpot(reserveSpot);

        user.getReservationList().add(newReservation);
        reserveSpot.getReservationList().add(newReservation);

        userRepository3.save(user);
        spotRepository3.save(reserveSpot);
        reservationRepository3.save(newReservation);

        return newReservation;

    }
}
