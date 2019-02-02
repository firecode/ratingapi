package com.auburn.hacks.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auburn.hacks.domain.Rating;
import com.auburn.hacks.service.RatingService;

@RestController
public class RatingController {

	@Autowired
	private RatingService ratingService;

	@GetMapping("/ratings")
	public List<Rating> getRating() throws InterruptedException, ExecutionException {
		return ratingService.getRatings();
	}

	@PostMapping("/rating")
	public List<Rating> createRating(@RequestBody Rating rating) throws InterruptedException, ExecutionException {
		return ratingService.create(rating);
	}
}
