package br.car.registration.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.time.LocalDate;

@Data
@MappedSuperclass
public abstract class TemporalEntity {

    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;

    public boolean isValidAt(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date must not be null");
        }
        boolean validFrom = !date.isBefore(fromDate); // date >= fromDate
        boolean validTo = (toDate == null) || !date.isAfter(toDate); // date <= toDate, if present
        return validFrom && validTo;
    }
}
