package ru.kharina.study.springsecurityconference.modeldto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {

    private int id;
    private String name;
    private String description;
    private Date date;
    private int duration;
    private int speaker;
    private int auditorium;
    private List<Integer> visitorList;
}
