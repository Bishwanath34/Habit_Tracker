package com.habit_tracker.habitiq.Repository;

import com.habit_tracker.habitiq.Entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    // Find profile by the user ID
    Optional<UserProfile> findByUser_UserId(Long userId);
}