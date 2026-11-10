package ru.academy.textapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Long id;
    private String title;
    private Integer pages;
    private Integer publishYear;
    private String language;
    private List<AuthorDto> authors;
}
