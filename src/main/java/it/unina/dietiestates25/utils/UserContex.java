package it.unina.dietiestates25.utils;

import it.unina.dietiestates25.entity.User;


public class UserContex {

    public static User getUserCurrent(){

        //TODO capire come ottente user corrente dal contex
        User userCurrent = User.builder()
                .id(2)
                .email("raimondo@gmail.com")
                .username("Raimon")
                .build();

        return userCurrent;
    }
}
