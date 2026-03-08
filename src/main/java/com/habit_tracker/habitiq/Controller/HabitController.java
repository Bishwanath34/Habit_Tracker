package com.habit_tracker.habitiq.Controller;

import com.habit_tracker.habitiq.Entity.Habit;
import com.habit_tracker.habitiq.Entity.Mood;
import com.habit_tracker.habitiq.Entity.User;
import com.habit_tracker.habitiq.Entity.UserProfile;
import com.habit_tracker.habitiq.Enum.HabitEnum.*;
import com.habit_tracker.habitiq.Repository.HabitRepository;
import com.habit_tracker.habitiq.Repository.MoodRepository;
import com.habit_tracker.habitiq.Service.HabitService;
import com.habit_tracker.habitiq.Service.MoodService;
import com.habit_tracker.habitiq.Service.ProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/api/v1/habits")
public class HabitController {

    @Autowired
    private HabitService habitService;

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private MoodRepository moodRepository;

    @Autowired
    private MoodService moodService;

    @Autowired
    private ProfileService profileService;

    /* ===============================
       LANDING PAGE (CREATE HABIT)
       =============================== */
    @GetMapping("/")
    public String showHabitForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if(user == null) return "redirect:/login";

        model.addAttribute("habit", new Habit());
        model.addAttribute("categories", CategoryEnum.values());
        model.addAttribute("frequencies", FrequencyEnum.values());
        model.addAttribute("times", PreferredTimeOfDayEnum.values());
        model.addAttribute("efforts", EffortLevelEnum.values());
        model.addAttribute("statuses", StatusEnum.values());

        // add profile for navbar
        UserProfile profile = profileService.getUserProfile(user.getUserId());
        model.addAttribute("profile", profile);

        return "landingPage";
    }

    /* ===============================
       ADD HABIT (USER SPECIFIC)
       =============================== */
    @PostMapping("/add")
    public String addHabit(@ModelAttribute Habit habit, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if(user == null) return "redirect:/login";

        habit.setUser(user);
        habit.setCreatedAt(LocalDateTime.now());
        habit.setCurrentStreak(0);
        habit.setLongestStreak(0);

        habitService.addHabit(habit);

        return "redirect:/api/v1/habits/dashboard";
    }

    /* ===============================
       DASHBOARD PAGE
       =============================== */
    @GetMapping("/dashboard")
    public String dashBoard(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "4") int size,
            Model model,
            HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");
        if(user == null) return "redirect:/login";

        Pageable pageable = PageRequest.of(pageNo, size);
        Page<Habit> dashboardPage = habitService.getDashboardHabits(user, pageable);
        List<Habit> habits = dashboardPage.getContent();

        List<Habit> allHabits = habitRepository.findByUser(user);
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        // reset streak if last completed date is older than yesterday
        for(Habit habit : allHabits) {
            if(habit.getLastCompletedDate() != null &&
                    !habit.getLastCompletedDate().equals(today) &&
                    !habit.getLastCompletedDate().equals(yesterday)) {
                habit.setCurrentStreak(0);
                habitRepository.save(habit);
            }
        }

        int currentStreak = allHabits.stream()
                .mapToInt(Habit::getCurrentStreak)
                .max()
                .orElse(0);

        int bestStreak = allHabits.stream()
                .mapToInt(Habit::getLongestStreak)
                .max()
                .orElse(0);

        Mood todayMood = moodRepository.findByDate(today).orElse(null);
        String insight = moodService.generateInsight();

        // Add model attributes for Thymeleaf
        model.addAttribute("habits", habits);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", dashboardPage.getTotalPages());
        model.addAttribute("currentStreak", currentStreak);
        model.addAttribute("bestStreak", bestStreak);
        model.addAttribute("todayMood", todayMood);
        model.addAttribute("insight", insight);

        // Add profile for navbar
        UserProfile profile = profileService.getUserProfile(user.getUserId());
        model.addAttribute("profile", profile);

        return "dashBoard";
    }

    /* ===============================
       VIEW SINGLE HABIT
       =============================== */
    @GetMapping("/view/{id}")
    public String viewHabit(@PathVariable long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if(user == null) return "redirect:/login";

        Habit habit = habitService.viewHabit(id);
        if(!habit.getUser().getUserId().equals(user.getUserId())) {
            return "redirect:/api/v1/habits/dashboard";
        }

        model.addAttribute("habit", habit);

        // Add profile for navbar
        UserProfile profile = profileService.getUserProfile(user.getUserId());
        model.addAttribute("profile", profile);

        return "viewHabit";
    }

    /* ===============================
       HABIT TABLE WITH PAGINATION
       =============================== */
    @GetMapping("/table")
    public String showHabitTable(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "5") int size,
            Model model,
            HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");
        if(user == null) return "redirect:/login";

        Pageable pageable = PageRequest.of(pageNo, size, Sort.by("createdAt").descending());
        Page<Habit> habitPage = habitRepository.findByUser(user, pageable);

        model.addAttribute("habits", habitPage.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", habitPage.getTotalPages());
        model.addAttribute("size", size);

        // Add profile for navbar
        UserProfile profile = profileService.getUserProfile(user.getUserId());
        model.addAttribute("profile", profile);

        return "habitTable";
    }

    /* ===============================
       UPDATE HABIT STATUS
       =============================== */
    @PostMapping("/status/{id}")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");
        if(user == null) return "redirect:/login";

        Habit habit = habitRepository.findById(id).orElseThrow();
        if(!habit.getUser().getUserId().equals(user.getUserId())) {
            return "redirect:/api/v1/habits/dashboard";
        }

        switch(status) {
            case "Active" -> habit.setStatusEnum(StatusEnum.Active);
            case "Paused" -> habit.setStatusEnum(StatusEnum.Paused);
            case "Archived" -> habit.setStatusEnum(StatusEnum.Archived);
        }

        habitRepository.save(habit);

        return "redirect:/api/v1/habits/table";
    }
    @PostMapping("/delete/{id}")
    public String deleteHabit(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if(user == null) return "redirect:/login";

        Habit habit = habitRepository.findById(id).orElse(null);
        if(habit != null && habit.getUser().getUserId().equals(user.getUserId())) {
            habitRepository.delete(habit);
        }

        return "redirect:/api/v1/habits/table";
    }
}