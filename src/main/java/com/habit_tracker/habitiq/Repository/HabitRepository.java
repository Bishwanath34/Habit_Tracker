package com.habit_tracker.habitiq.Repository;

import com.habit_tracker.habitiq.Entity.Habit;
import com.habit_tracker.habitiq.Entity.User;
import com.habit_tracker.habitiq.Enum.HabitEnum.StatusEnum;
import com.habit_tracker.habitiq.Enum.HabitLog.completionStatusEnum;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public interface HabitRepository extends JpaRepository<Habit, Long> {

    @Query("""
SELECT h FROM Habit h
WHERE h.user = :user
AND h.statusEnum = :habitStatus
AND h.habitId NOT IN (
    SELECT hl.habit.habitId
    FROM HabitLog hl
    WHERE hl.logDate = :today
    AND hl.status = :logStatus
)
AND (
    h.frequencyEnum = 'Daily'
    OR (h.frequencyEnum = 'Weekly' AND h.dayOfWeek = :todayDay)
    OR (h.frequencyEnum = 'Monthly' AND h.dayOfMonth = :todayDate)
)
""")
    Page<Habit> findDashboardHabits(
            @Param("user") User user,
            @Param("today") LocalDate today,
            @Param("todayDay") DayOfWeek todayDay,
            @Param("todayDate") Integer todayDate,
            @Param("habitStatus") StatusEnum habitStatus,
            @Param("logStatus") completionStatusEnum logStatus,
            Pageable pageable
    );
    List<Habit> findByUser(User user);

    Page<Habit> findByUser(User user, Pageable pageable);
}