package com.habit_tracker.habitiq.Controller;

import com.habit_tracker.habitiq.Entity.ReminderSetting;
import com.habit_tracker.habitiq.Entity.User;
import com.habit_tracker.habitiq.Entity.UserProfile;
import com.habit_tracker.habitiq.Service.ProfileService;
import com.habit_tracker.habitiq.Service.ReminderSettingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/settings")
public class ReminderSettingsController {

    @Autowired
    private ReminderSettingService settingService;

    @Autowired
    private ProfileService profileService;

    @GetMapping
    public String getSettings(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return  "redirect:/api/v1/auth/loginForm";

        // Load reminder settings
        ReminderSetting setting = settingService.getSetting(user);
        model.addAttribute("setting", setting);
        model.addAttribute("frequencies", com.habit_tracker.habitiq.Enum.HabitEnum.FrequencyEnum.values());
        model.addAttribute("timesOfDay", com.habit_tracker.habitiq.Enum.HabitEnum.PreferredTimeOfDayEnum.values());

        // Load user profile for navbar
        UserProfile profile = profileService.getUserProfile(user.getUserId());
        model.addAttribute("profile", profile);

        return "setting"; // Thymeleaf template
    }

    @PostMapping
    public String updateSettings(@RequestParam String frequency,
                                 @RequestParam String preferredTime,
                                 @RequestParam(required = false) boolean enabled,
                                 HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return  "redirect:/api/v1/auth/loginForm";
        settingService.updateSetting(user, frequency, preferredTime, enabled);
        return "redirect:/api/v1/settings?success";
    }
}