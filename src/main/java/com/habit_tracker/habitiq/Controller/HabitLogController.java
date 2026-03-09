package com.habit_tracker.habitiq.Controller;

import com.habit_tracker.habitiq.Entity.Habit;
import com.habit_tracker.habitiq.Entity.User;
import com.habit_tracker.habitiq.Enum.HabitLog.completionStatusEnum;
import com.habit_tracker.habitiq.Repository.HabitRepository;
import com.habit_tracker.habitiq.Service.HabitLogService;
import com.habit_tracker.habitiq.Service.HabitService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/api/v1/habit-log")
public class HabitLogController {

    @Autowired
    private HabitLogService habitLogService;
    @Autowired
    private HabitRepository habitRepository;
    @Autowired
    private HabitService habitService;
    @PostMapping("/{habitId}/complete")
    public String markCompleted(@PathVariable Long habitId, HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");
        if(user == null) return "redirect:/api/v1/auth/loginForm";
        long Id=habitLogService.markHabit(habitId, completionStatusEnum.Completed);
        Habit habit=habitService.getHabitById(habitId);
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        if (habit.getLastCompletedDate() != null &&
                habit.getLastCompletedDate().equals(today)) {

            // already completed today → ignore

        } else if (habit.getLastCompletedDate() != null &&
                habit.getLastCompletedDate().equals(yesterday)) {

            habit.setCurrentStreak(habit.getCurrentStreak() + 1);

        } else {

            if (habit.getCurrentStreak() > habit.getLongestStreak()) {
                habit.setLongestStreak(habit.getCurrentStreak());
            }

            habit.setCurrentStreak(1);
        }

        habit.setLastCompletedDate(today);
        habitRepository.save(habit);

        return "redirect:/api/v1/habits/dashboard?logId=" + Id;
    }

    @PostMapping("/{habitId}/partial")
    public String markPartial(@PathVariable Long habitId) {
        habitLogService.markHabit(habitId, completionStatusEnum.PartiallyCompleted);
        return "redirect:/api/v1/habits/dashboard";
    }

    @PostMapping("/{habitId}/missed")
    public String markMissed(@PathVariable Long habitId) {
        habitLogService.markHabit(habitId, completionStatusEnum.NotStarted);
        return "redirect:/api/v1/habits/dashboard";
    }
}