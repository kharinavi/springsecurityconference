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
@Table(name = "visitors")
public class Visitor {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "security_id")
    private int securityId;

    @ManyToMany
    @JoinTable(
            name="report_visitor",
            joinColumns = @JoinColumn(name = "visitor_id"),
            inverseJoinColumns = @JoinColumn(name = "report_id"))
    List<Report> reportList;

    public Visitor(String name, String email, int security_id) {
        this.name = name;
        this.email = email;
        this.securityId = security_id;
    }
}
