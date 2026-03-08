package com.habit_tracker.habitiq.Entity;

import com.habit_tracker.habitiq.Enum.HabitLog.completionStatusEnum;
import com.habit_tracker.habitiq.Enum.HabitLog.completionStatusEnum;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "habit_log",
        uniqueConstraints = @UniqueConstraint(columnNames = {"habit_id", "log_date"}))
public class HabitLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "habit_log_id")
    private Long habitLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private completionStatusEnum status;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // getters & setters

    public Long getHabitLogId() {
        return habitLogId;
    }

    public void setHabitLogId(Long habitLogId) {
        this.habitLogId = habitLogId;
    }

    public Habit getHabit() {
        return habit;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
    }

    public LocalDate getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDate logDate) {
        this.logDate = logDate;
    }

    public completionStatusEnum getStatus() {
        return status;
    }

    public void setStatus(completionStatusEnum status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}