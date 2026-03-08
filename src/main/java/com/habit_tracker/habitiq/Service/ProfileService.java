package com.habit_tracker.habitiq.Service;

import com.habit_tracker.habitiq.Entity.User;
import com.habit_tracker.habitiq.Entity.UserProfile;
import com.habit_tracker.habitiq.Repository.UserProfileRepository;
import com.habit_tracker.habitiq.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserRepository userRepository;

    // ================= UPLOAD / UPDATE PROFILE IMAGE =================
    public void uploadProfileImage(Long userId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            System.out.println("[DEBUG] No file uploaded for user " + userId);
            return;
        }

        // Fetch existing profile or create a new one
        UserProfile profile = userProfileRepository.findByUser_UserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));

                    // Create new profile
                    UserProfile newProfile = new UserProfile();
                    newProfile.setUser(user); // back-reference
                    newProfile.setFocusArea("General");
                    newProfile.setHabitDifficultyPreference("Medium");
                    newProfile.setTimezone("UTC");

// Save the profile first
                    userProfileRepository.save(newProfile);

// Link profile to user
                    user.setUserProfile(newProfile);
                    userRepository.save(user);

                    System.out.println("[DEBUG] Created new profile for user " + userId);
                    return newProfile;
                });

        try {
            profile.setProfileImage(file.getBytes());
            profile.setImageType(file.getContentType() != null ? file.getContentType() : "image/jpeg");

            userProfileRepository.save(profile);
            System.out.println("[DEBUG] Uploaded profile image for user " + userId +
                    ", size: " + file.getSize() + ", type: " + file.getContentType());
        } catch (Exception e) {
            System.out.println("[DEBUG] Failed to upload image for user " + userId);
            e.printStackTrace();
        }
    }

    // ================= REMOVE PROFILE IMAGE =================
    public void removeProfileImage(Long userId) {
        Optional<UserProfile> profileOpt = userProfileRepository.findByUser_UserId(userId);

        if (profileOpt.isPresent()) {
            UserProfile profile = profileOpt.get();
            profile.setProfileImage(null);
            profile.setImageType(null);
            userProfileRepository.save(profile);
            System.out.println("[DEBUG] Removed profile image for user " + userId);
        } else {
            System.out.println("[DEBUG] No profile found to remove image for user " + userId);
        }
    }

    // ================= GET PROFILE IMAGE =================
    public byte[] getProfileImage(Long userId) {
        Optional<UserProfile> profileOpt = userProfileRepository.findByUser_UserId(userId);

        if (profileOpt.isPresent() && profileOpt.get().getProfileImage() != null) {
            System.out.println("[DEBUG] Returning profile image for user " + userId +
                    ", size: " + profileOpt.get().getProfileImage().length);
            return profileOpt.get().getProfileImage();
        } else {
            System.out.println("[DEBUG] User " + userId + " has no profile image");
            return null;
        }
    }

    // ================= GET IMAGE CONTENT TYPE =================
    public String getImageType(Long userId) {
        return userProfileRepository.findByUser_UserId(userId)
                .map(profile -> profile.getImageType() != null ? profile.getImageType() : "image/jpeg")
                .orElse("image/jpeg");
    }

    // ================= GET FULL PROFILE =================
    public UserProfile getUserProfile(Long userId) {
        return userProfileRepository.findByUser_UserId(userId).orElse(null);
    }
}