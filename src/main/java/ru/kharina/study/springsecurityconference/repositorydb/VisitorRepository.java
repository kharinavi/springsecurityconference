package ru.kharina.study.springsecurityconference.repositorydb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kharina.study.springsecurityconference.modeldb.Visitor;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Integer> {
}
