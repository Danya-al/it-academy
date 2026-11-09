package ru.academy.textapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto {

    private Long id;
    private String lastName;
    private String firstName;
    private String middleName;
    private Integer birthYear;
    private String country;
}
