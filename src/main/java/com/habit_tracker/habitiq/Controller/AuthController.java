package com.habit_tracker.habitiq.Controller;

import com.habit_tracker.habitiq.Entity.User;
import com.habit_tracker.habitiq.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // -------- Register Form --------
    @GetMapping("/regisForm")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    // -------- Login Form --------
    @GetMapping("/loginForm")
    public String showLoginForm(Model model) {
        model.addAttribute("logUser", new User());
        return "login";
    }

    // -------- REGISTER --------
    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user, Model model) {

        try {

            User savedUser = userService.register(user);

            model.addAttribute("Id", savedUser.getUserId());
            model.addAttribute("email", savedUser.getEmail());

            return "verify";

        } catch (RuntimeException e) {

            model.addAttribute("error", e.getMessage());
            return "registration";
        }
    }

    // -------- LOGIN --------
    @PostMapping("/login")
    public String login(@ModelAttribute("logUser") User user,
                        HttpSession session,
                        Model model) {

        try {

            User loggedUser = userService.login(user.getEmail(), user.getPassword(), session);
            System.out.println("LOGIN SUCCESS");

            // 🔴 STORE USER IN SESSION
            session.setAttribute("loggedUser", loggedUser);
            return "redirect:/api/v1/habits/dashboard";

        } catch (RuntimeException e) {

            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    // -------- VERIFY OTP --------
    @PostMapping("/verify-otp/{id}")
    public String verifyOtp(@RequestParam String d1,
                            @RequestParam String d2,
                            @RequestParam String d3,
                            @RequestParam String d4,
                            @RequestParam String d5,
                            @RequestParam String d6,
                            @PathVariable Long id,
                            Model model) {

        String enteredOtp = d1 + d2 + d3 + d4 + d5 + d6;

        boolean valid = userService.validateOtp(enteredOtp, id);

        if (valid) {
            return "redirect:/api/v1/auth/loginForm";
        } else {

            model.addAttribute("error", "Invalid OTP. Please try again.");
            model.addAttribute("Id", id);

            return "verify";
        }
    }

    // -------- RESEND OTP --------
    @GetMapping("/resend-otp/{id}")
    public String resendOtp(@PathVariable Long id, Model model) {

        try {

            userService.resendOtp(id);

            model.addAttribute("message", "A new OTP has been sent to your email.");
            model.addAttribute("Id", id);

            return "verify";

        } catch (RuntimeException e) {

            model.addAttribute("error", e.getMessage());
            model.addAttribute("Id", id);

            return "verify";
        }
    }

    // -------- LOGOUT --------
    @GetMapping("/logout")
    public String logout(HttpSession session){

        session.invalidate();

        return "redirect:/api/v1/auth/loginForm";
    }
}