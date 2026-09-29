package com.phedi.infrastructure.persistence.party.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

import com.phedi.domain.party.model.PartyType;

@Entity
@Table(name = "person")
@PrimaryKeyJoinColumn(name = "id")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class PersonEntity extends PartyEntity {

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "middle_name", length = 100)
    private String middleName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    // Constructors
    public PersonEntity() {
        super(PartyType.PERSON);
    }

    public PersonEntity(String firstName, String lastName) {
        super(PartyType.PERSON);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Business methods
    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        fullName.append(firstName);

        if (middleName != null && !middleName.isBlank()) {
            fullName.append(" ").append(middleName);
        }

        fullName.append(" ").append(lastName);
        return fullName.toString();
    }
}
