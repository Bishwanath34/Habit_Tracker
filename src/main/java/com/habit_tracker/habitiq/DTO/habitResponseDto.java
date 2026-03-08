package com.habit_tracker.habitiq.DTO;

import com.habit_tracker.habitiq.Enum.HabitEnum.StatusEnum;

import java.time.LocalDateTime;

public class habitResponseDto {
    private long habitId;
    private StatusEnum statusEnum;
    private LocalDateTime createdAt;

    public habitResponseDto(long habitId, StatusEnum statusEnum, LocalDateTime createdAt) {
        this.habitId = habitId;
        this.statusEnum = statusEnum;
        this.createdAt = createdAt;
    }

    public long getHabitId() {
        return habitId;
    }

    public void setHabitId(long habitId) {
        this.habitId = habitId;
    }

    public StatusEnum getStatusEnum() {
        return statusEnum;
    }

    public void setStatusEnum(StatusEnum statusEnum) {
        this.statusEnum = statusEnum;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}