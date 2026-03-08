package com.habit_tracker.habitiq.Entity;

import com.habit_tracker.habitiq.Enum.HabitEnum.FrequencyEnum;
import com.habit_tracker.habitiq.Enum.HabitEnum.PreferredTimeOfDayEnum;
import jakarta.persistence.*;


@Entity

public class ReminderSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private FrequencyEnum frequency = FrequencyEnum.Daily;

    @Enumerated(EnumType.STRING)
    private PreferredTimeOfDayEnum preferredTime = PreferredTimeOfDayEnum.Morning;

    private boolean enabled = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FrequencyEnum getFrequency() {
        return frequency;
    }

    public void setFrequency(FrequencyEnum frequency) {
        this.frequency = frequency;
    }

    public PreferredTimeOfDayEnum getPreferredTime() {
        return preferredTime;
    }

    public void setPreferredTime(PreferredTimeOfDayEnum preferredTime) {
        this.preferredTime = preferredTime;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}