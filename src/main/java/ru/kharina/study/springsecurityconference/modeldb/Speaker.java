package ru.kharina.study.springsecurityconference.modeldb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "speakers")
public class Speaker {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private String age;

    @Column(name = "description")
    private String description;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "security_id")
    private int securityId;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @OneToMany(mappedBy = "speaker")
    List<Report> reportList;

    public Speaker(String name, String email, int securityId) {
        this.name = name;
        this.email = email;
        this.securityId = securityId;
    }
}
