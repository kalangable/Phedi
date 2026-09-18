package com.phedi.domain.party.model;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Organization extends Party {

    private String legalName;

    private String tradeName;

    private String brandName;

    private LocalDate foundingDate;

}
