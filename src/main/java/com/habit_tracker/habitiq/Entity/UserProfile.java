package com.habit_tracker.habitiq.Entity;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @Column(nullable = false)
    private String focusArea;

    @Column(nullable = false)
    private String habitDifficultyPreference;

    private LocalTime preferredReminderTime;

    private String notificationIntensity;

    @Column(nullable = false)
    private String timezone;

    @OneToOne(mappedBy = "userProfile")
    private User user;

    @Column(length = 1000)
    private String privacySettings;

    /* ================= PROFILE IMAGE ================= */

    @Lob
    @Column(name = "profile_image", columnDefinition = "LONGBLOB")
    private byte[] profileImage;

    private String imageType; // image/jpeg, image/png

    /* ================= GETTERS / SETTERS ================= */

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getFocusArea() {
        return focusArea;
    }

    public void setFocusArea(String focusArea) {
        this.focusArea = focusArea;
    }

    public String getHabitDifficultyPreference() {
        return habitDifficultyPreference;
    }

    public void setHabitDifficultyPreference(String habitDifficultyPreference) {
        this.habitDifficultyPreference = habitDifficultyPreference;
    }

    public LocalTime getPreferredReminderTime() {
        return preferredReminderTime;
    }

    public void setPreferredReminderTime(LocalTime preferredReminderTime) {
        this.preferredReminderTime = preferredReminderTime;
    }

    public String getNotificationIntensity() {
        return notificationIntensity;
    }

    public void setNotificationIntensity(String notificationIntensity) {
        this.notificationIntensity = notificationIntensity;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getPrivacySettings() {
        return privacySettings;
    }

    public void setPrivacySettings(String privacySettings) {
        this.privacySettings = privacySettings;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public byte[] getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }
}