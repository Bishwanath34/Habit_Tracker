package com.habit_tracker.habitiq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HabitiqApplication {

	public static void main(String[] args) {
		SpringApplication.run(HabitiqApplication.class, args);
	}

}
