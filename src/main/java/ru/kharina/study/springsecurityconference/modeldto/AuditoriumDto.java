package ru.kharina.study.springsecurityconference.modeldto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditoriumDto {
    private int id;
    private String number;
    private String description;
    private List<Integer> reportList;
}
