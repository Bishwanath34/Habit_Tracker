package com.habit_tracker.habitiq.Service;

import com.habit_tracker.habitiq.Entity.Habit;
import com.habit_tracker.habitiq.Entity.HabitLog;
import com.habit_tracker.habitiq.Enum.HabitEnum.StatusEnum;
import com.habit_tracker.habitiq.Enum.HabitLog.completionStatusEnum;
import com.habit_tracker.habitiq.Repository.HabitLogRepository;
import com.habit_tracker.habitiq.Repository.HabitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class HabitLogService {

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private HabitLogRepository habitLogRepository;

    public long markHabit(Long habitId, completionStatusEnum status) {

        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new RuntimeException("Habit not found"));

        if (habit.getStatusEnum() != StatusEnum.Active) {
            throw new IllegalStateException("Habit is not active");
        }

        LocalDate today = LocalDate.now();

        HabitLog log = habitLogRepository
                .findByHabitAndLogDate(habit, today)
                .orElseGet(() -> {
                    HabitLog newLog = new HabitLog();
                    newLog.setHabit(habit);
                    newLog.setLogDate(today);
                    newLog.setCreatedAt(LocalDateTime.now());
                    return newLog;
                });

        log.setStatus(status);
        habitLogRepository.save(log);
        return log.getHabitLogId();
    }
}