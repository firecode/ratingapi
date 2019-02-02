package com.auburn.hacks;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RatingController {

	@Autowired
	private RatingService ratingService;

	@GetMapping("/ratings")
	public List<Rating> getRating() throws InterruptedException, ExecutionException {
		return ratingService.getRatings();
	}

	@PostMapping("/ratings")
	public List<Rating> createRating(@RequestBody Rating rating) throws InterruptedException, ExecutionException {
		return ratingService.create(rating);
	}
}
