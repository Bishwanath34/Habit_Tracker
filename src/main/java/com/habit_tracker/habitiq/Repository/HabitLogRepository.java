package com.habit_tracker.habitiq.Repository;

import com.habit_tracker.habitiq.Entity.Habit;
import com.habit_tracker.habitiq.Entity.HabitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HabitLogRepository extends JpaRepository<HabitLog, Long> {

    List<HabitLog> findByHabitUser(com.habit_tracker.habitiq.Entity.User user);

    // Weekly & monthly stats
    @Query("""
        SELECT h.logDate, COUNT(h)
        FROM HabitLog h
        WHERE h.habit.user.userId = :userId
        AND h.status = 'COMPLETED'
        AND h.logDate >= :startDate
        GROUP BY h.logDate
        ORDER BY h.logDate
    """)
    List<Object[]> getWeeklyStats(@Param("userId") Long userId,
                                  @Param("startDate") LocalDate startDate);

    // Heatmap stats
    @Query("""
        SELECT h.logDate, COUNT(h)
        FROM HabitLog h
        WHERE h.habit.user.userId = :userId
        AND h.status = 'COMPLETED'
        AND h.logDate BETWEEN :startDate AND :endDate
        GROUP BY h.logDate
        ORDER BY h.logDate
    """)
    List<Object[]> getHeatmapStats(@Param("userId") Long userId,
                                   @Param("startDate") LocalDate startDate,
                                   @Param("endDate") LocalDate endDate);

    Optional<HabitLog> findByHabitAndLogDate(Habit habit, LocalDate today);
}