package com.habit_tracker.habitiq.Repository;

import com.habit_tracker.habitiq.Entity.Mood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MoodRepository extends JpaRepository<Mood, Long> {

    Optional<Mood> findByDate(LocalDate date);

    List<Mood> findTop7ByOrderByDateDesc();

}