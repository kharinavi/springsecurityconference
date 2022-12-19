package ru.kharina.study.springsecurityconference.repositorydb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kharina.study.springsecurityconference.modeldb.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
}
