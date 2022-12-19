package ru.kharina.study.springsecurityconference.modeldto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitorDto {
    private int id;
    private String name;
    private String phone;
    private String email;
    private List<Integer> reportList;
}
