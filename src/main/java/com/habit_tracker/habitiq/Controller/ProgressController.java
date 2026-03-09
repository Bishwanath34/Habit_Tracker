package com.habit_tracker.habitiq.Controller;

import com.habit_tracker.habitiq.Entity.Habit;
import com.habit_tracker.habitiq.Entity.HabitLog;
import com.habit_tracker.habitiq.Entity.User;
import com.habit_tracker.habitiq.Entity.UserProfile;
import com.habit_tracker.habitiq.Repository.HabitLogRepository;
import com.habit_tracker.habitiq.Repository.HabitRepository;
import com.habit_tracker.habitiq.Service.HabitService;
import com.habit_tracker.habitiq.Service.ProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/api/v1/progress")
public class ProgressController {

    @Autowired
    private HabitRepository habitRepository;
    @Autowired
    private HabitService habitService;
    @Autowired
    private HabitLogRepository habitLogRepository;
    @Autowired
    private ProfileService profileService;

    @GetMapping
    public String showProgress(@RequestParam(defaultValue="weekly") String range,
                               Model model, HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/api/v1/auth/loginForm";

        // Fetch habits
        List<Habit> habits = habitRepository.findByUser(user);

        // Streaks
        int currentStreak = habits.stream().mapToInt(Habit::getCurrentStreak).max().orElse(0);
        int longestStreak = habits.stream().mapToInt(Habit::getLongestStreak).max().orElse(0);

        // Habit logs
        List<HabitLog> logs = habitLogRepository.findByHabitUser(user);

        // Weekly & monthly stats
        List<Object[]> weeklyStats = habitLogRepository.getWeeklyStats(
                user.getUserId(), LocalDate.now().minusDays(7)
        );

        List<Object[]> monthlyStats = habitLogRepository.getWeeklyStats(
                user.getUserId(), LocalDate.now().minusDays(30)
        );

        // Heatmap streaks (last 98 days)
        List<Integer> streakData = habitService.getLastNDaysStreaks(user, 98);

        // Add profile for navbar
        UserProfile profile = profileService.getUserProfile(user.getUserId());

        // Add to model
        model.addAttribute("habits", habits);
        model.addAttribute("logs", logs);
        model.addAttribute("currentStreak", currentStreak);
        model.addAttribute("longestStreak", longestStreak);
        model.addAttribute("weeklyStats", weeklyStats);
        model.addAttribute("monthlyStats", monthlyStats);
        model.addAttribute("streakData", streakData);
        model.addAttribute("profile", profile);
        model.addAttribute("selectedRange", range);

        return "progress";
    }
}