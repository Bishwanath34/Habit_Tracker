package com.habit_tracker.habitiq.Controller;

import com.habit_tracker.habitiq.Entity.User;
import com.habit_tracker.habitiq.Entity.UserProfile;
import com.habit_tracker.habitiq.Service.ProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/v1/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    // ================= PROFILE PAGE =================
    @GetMapping
    public String profilePage(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }

        UserProfile profile = profileService.getUserProfile(loggedUser.getUserId());
        model.addAttribute("user", loggedUser);    // For name/email
        model.addAttribute("profile", profile);    // For image
        return "profile";
    }

    // ================= UPLOAD IMAGE =================
    @PostMapping("/upload-image")
    public String uploadImage(@RequestParam("file") MultipartFile file,
                              HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser != null) {
            profileService.uploadProfileImage(loggedUser.getUserId(), file);
        }
        return "redirect:/api/v1/profile";
    }

    // ================= DELETE IMAGE =================
    @PostMapping("/remove-image")
    public String removeImage(HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser != null) {
            profileService.removeProfileImage(loggedUser.getUserId());
        }
        return "redirect:/api/v1/profile";
    }

    // ================= GET IMAGE =================
    @GetMapping("/image")
    @ResponseBody
    public ResponseEntity<byte[]> getProfileImage(HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            System.out.println("[DEBUG] No logged user in session!");
            return ResponseEntity.notFound().build();
        }

        byte[] image = profileService.getProfileImage(loggedUser.getUserId());
        if (image == null) {
            System.out.println("[DEBUG] User " + loggedUser.getUserId() + " has no profile image.");
            return ResponseEntity.notFound().build();
        }

        String contentType = profileService.getImageType(loggedUser.getUserId());
        System.out.println("[DEBUG] Returning profile image for user " + loggedUser.getUserId() + ", size: " + image.length + ", type: " + contentType);
        return ResponseEntity.ok()
                .header("Content-Type", contentType)
                .body(image);
    }
}