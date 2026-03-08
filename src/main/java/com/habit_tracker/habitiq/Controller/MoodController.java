package com.habit_tracker.habitiq.Controller;

import com.habit_tracker.habitiq.Entity.Mood;
import com.habit_tracker.habitiq.Repository.MoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/moods")
public class MoodController {

    @Autowired
    private MoodRepository moodRepository;

    @PostMapping("/save")
    public String saveMood(
            @RequestParam String moodType,
            @RequestParam(required = false) String reflection) {

        Optional<Mood> existingMood =
                moodRepository.findByDate(LocalDate.now());

        Mood mood;

        if(existingMood.isPresent()) {

            mood = existingMood.get();
            mood.setMoodType(moodType);
            mood.setReflection(reflection);

        } else {

            mood = new Mood();
            mood.setMoodType(moodType);
            mood.setReflection(reflection);
            mood.setDate(LocalDate.now());
        }

        moodRepository.save(mood);

        return "redirect:/api/v1/habits/dashboard";
    }
}