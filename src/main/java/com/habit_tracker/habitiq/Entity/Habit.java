package com.habit_tracker.habitiq.Entity;

import com.habit_tracker.habitiq.Enum.HabitEnum.*;
import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "habit")
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long habitId;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String habitName;

    @Enumerated(EnumType.STRING)
    private CategoryEnum categoryEnum;

    @Enumerated(EnumType.STRING)
    private FrequencyEnum frequencyEnum;

    /* ===============================
       Frequency Support Fields
       =============================== */

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;     // used for weekly habits

    private Integer dayOfMonth;      // used for monthly habits


    @Enumerated(EnumType.STRING)
    private PreferredTimeOfDayEnum preferredTimeOfDayEnum;

    @Enumerated(EnumType.STRING)
    private EffortLevelEnum effortLevelEnum;

    @Column(columnDefinition = "TEXT")
    private String purpose;

    @Enumerated(EnumType.STRING)
    private StatusEnum statusEnum;

    /* ===============================
       Streak Tracking
       =============================== */

    private int currentStreak;
    private int longestStreak;

    private LocalDate lastCompletedDate;

    /* ===============================
       Audit Fields
       =============================== */

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /* ===============================
       Habit Logs Relationship
       =============================== */

    @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HabitLog> habitLogs;

    /* ===============================
       Lifecycle Hooks
       =============================== */

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /* ===============================
       Getters and Setters
       =============================== */

    public Long getHabitId() {
        return habitId;
    }

    public void setHabitId(Long habitId) {
        this.habitId = habitId;
    }

    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    public CategoryEnum getCategoryEnum() {
        return categoryEnum;
    }

    public void setCategoryEnum(CategoryEnum categoryEnum) {
        this.categoryEnum = categoryEnum;
    }

    public FrequencyEnum getFrequencyEnum() {
        return frequencyEnum;
    }

    public void setFrequencyEnum(FrequencyEnum frequencyEnum) {
        this.frequencyEnum = frequencyEnum;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public PreferredTimeOfDayEnum getPreferredTimeOfDayEnum() {
        return preferredTimeOfDayEnum;
    }

    public void setPreferredTimeOfDayEnum(PreferredTimeOfDayEnum preferredTimeOfDayEnum) {
        this.preferredTimeOfDayEnum = preferredTimeOfDayEnum;
    }

    public EffortLevelEnum getEffortLevelEnum() {
        return effortLevelEnum;
    }

    public void setEffortLevelEnum(EffortLevelEnum effortLevelEnum) {
        this.effortLevelEnum = effortLevelEnum;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public StatusEnum getStatusEnum() {
        return statusEnum;
    }

    public void setStatusEnum(StatusEnum statusEnum) {
        this.statusEnum = statusEnum;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getLongestStreak() {
        return longestStreak;
    }

    public void setLongestStreak(int longestStreak) {
        this.longestStreak = longestStreak;
    }

    public LocalDate getLastCompletedDate() {
        return lastCompletedDate;
    }

    public void setLastCompletedDate(LocalDate lastCompletedDate) {
        this.lastCompletedDate = lastCompletedDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<HabitLog> getHabitLogs() {
        return habitLogs;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setHabitLogs(List<HabitLog> habitLogs) {
        this.habitLogs = habitLogs;
    }
}