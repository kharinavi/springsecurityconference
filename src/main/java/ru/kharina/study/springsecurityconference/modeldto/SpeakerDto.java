package ru.kharina.study.springsecurityconference.modeldto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpeakerDto {
    private int id;
    private String name;
    private String age;
    private String description;
    private String phone;
    private String email;
    private byte[] photo;
    private List<Integer> reportList;
}
