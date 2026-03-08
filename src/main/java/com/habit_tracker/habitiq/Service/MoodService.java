package com.habit_tracker.habitiq.Service;

import com.habit_tracker.habitiq.Entity.Mood;
import com.habit_tracker.habitiq.Repository.MoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoodService {

    @Autowired
    private MoodRepository moodRepository;

    public String generateInsight() {

        List<Mood> moods = moodRepository.findTop7ByOrderByDateDesc();

        long happyCount = moods.stream()
                .filter(m -> m.getMoodType().equalsIgnoreCase("Happy"))
                .count();

        long sadCount = moods.stream()
                .filter(m -> m.getMoodType().equalsIgnoreCase("Sad"))
                .count();

        if (happyCount >= 5) {
            return "You have been feeling great this week. Your habits are positively impacting your mood!";
        }

        if (sadCount >= 3) {
            return "Your mood seems lower recently. Consider reviewing your habits or taking some rest.";
        }

        return "Your mood pattern is balanced. Keep maintaining consistency.";
    }

}