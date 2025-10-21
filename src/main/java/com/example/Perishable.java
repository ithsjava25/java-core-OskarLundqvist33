package com.example;

import java.time.LocalDate;

public interface Perishable {

    LocalDate expirationDate();

    default boolean isExpired()
    {
        LocalDate today = LocalDate.now();
        return expirationDate().isBefore(today);
    }
}
