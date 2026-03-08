package com.habit_tracker.habitiq.Service;

import com.habit_tracker.habitiq.DTO.habitResponseDto;
import com.habit_tracker.habitiq.Entity.Habit;
import com.habit_tracker.habitiq.Entity.User;
import com.habit_tracker.habitiq.Enum.HabitEnum.StatusEnum;
import com.habit_tracker.habitiq.Enum.HabitLog.completionStatusEnum;
import com.habit_tracker.habitiq.Repository.HabitLogRepository;
import com.habit_tracker.habitiq.Repository.HabitRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HabitService {

    @Autowired
    private HabitRepository habitRepository;
    @Autowired
    private HabitLogRepository habitLogRepository;

    public habitResponseDto addHabit(Habit habit) {

        habit.setCreatedAt(LocalDateTime.now());
        habitRepository.save(habit);

        return new habitResponseDto(
                habit.getHabitId(),
                habit.getStatusEnum(),
                habit.getCreatedAt()
        );
    }

    public List<Habit> getHabit() {
        return habitRepository.findAll();
    }

    public Habit getHabitById(long id) {
        return habitRepository.findById(id).orElse(null);
    }

    public Habit viewHabit(long id) {
        return habitRepository.findById(id).orElse(null);
    }

    /* ===============================
       DASHBOARD HABITS
       =============================== */

    public Page<Habit> getDashboardHabits(User user, Pageable pageable) {

        LocalDate today = LocalDate.now();

        return habitRepository.findDashboardHabits(
                user,
                today,
                today.getDayOfWeek(),
                today.getDayOfMonth(),
                StatusEnum.Active,
                completionStatusEnum.Completed,
                pageable
        );
    }
    public List<Integer> getLastNDaysStreaks(User user, int days) {
        List<Integer> streakData = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(days - 1);

        // Fetch completed habit counts per day
        List<Object[]> stats = habitLogRepository.getHeatmapStats(
                user.getUserId(), startDate, today
        );

        // Convert to map for easy lookup
        Map<LocalDate, Long> statsMap = stats.stream()
                .collect(Collectors.toMap(
                        obj -> (LocalDate)obj[0],
                        obj -> (Long)obj[1]
                ));

        for (int i = 0; i < days; i++) {
            LocalDate date = startDate.plusDays(i);
            int completed = statsMap.getOrDefault(date, 0L).intValue();

            int level = 0;
            if (completed > 0) level = 1;
            if (completed >= 2) level = 2;
            if (completed >= 3) level = 3;
            if (completed >= 4) level = 4; // adjust max

            streakData.add(level);
        }

        return streakData;
    }
}