package ru.kharina.study.springsecurityconference.repositorydb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kharina.study.springsecurityconference.modeldb.Speaker;

@Repository
public interface SpeakerRepository extends JpaRepository<Speaker, Integer> {
}
