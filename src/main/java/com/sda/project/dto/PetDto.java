package com.sda.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetDto {

    // needed to press Update pet
    private Long id;
    private String name;
    private String category;
    private String description;
}
