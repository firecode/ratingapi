package com.auburn.hacks.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import com.auburn.hacks.domain.Rating;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class RatingService {

	private FirebaseApp defaultApp;

	private Firestore db;

	@PostConstruct
	public void init() throws IOException {

		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource resource = resolver.getResource("classpath:ratingapi.json");

		InputStream serviceAccount = resource.getInputStream();

//		FileInputStream serviceAccount = new FileInputStream(
//				"/Users/rbongula/auburnhacks/RatingApi/src/main/resources/ratingapi.json");

		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setDatabaseUrl("https://ratingapi-cc9af.firebaseio.com").build();

		defaultApp = FirebaseApp.initializeApp(options);

		db = FirestoreClient.getFirestore();
	}

	public List<Rating> getRatings() throws InterruptedException, ExecutionException {
		CollectionReference ref = db.collection("ratings");
		ApiFuture<QuerySnapshot> apiFuture = ref.get();
		QuerySnapshot querySnapshot = apiFuture.get();

		List<QueryDocumentSnapshot> docs = querySnapshot.getDocuments();
		List<Rating> ratings = new ArrayList<Rating>();

		for (QueryDocumentSnapshot it : docs) {
			Map<String, Object> fields = it.getData();
			Rating rating = new Rating();
			if (fields.containsKey("comments")) {
				rating.setComments(fields.get("comments").toString());
			}

			if (fields.containsKey("title")) {
				rating.setTitle(fields.get("title").toString());
			}
			if (fields.containsKey("ratingId")) {
				rating.setRatingId(fields.get("ratingId").toString());
			}
			if (fields.containsKey("rater")) {
				rating.setRater(fields.get("rater").toString());
			}
			if (fields.containsKey("rating")) {
				rating.setRating(Integer.valueOf(fields.get("rating").toString()));
			}
			ratings.add(rating);
		}
		return ratings;

	}

	public List<Rating> create(Rating rating) throws InterruptedException, ExecutionException {
		CollectionReference ref = db.collection("ratings");

		rating.setRatingId(UUID.randomUUID().toString());
		Map<String, Object> fields = new HashMap<String, Object>();

		fields.put("comments", rating.getComments());
		fields.put("title", rating.getTitle());
		fields.put("ratingId", rating.getRatingId());
		fields.put("rater", rating.getRater());
		fields.put("rating", rating.getRating());

		ApiFuture<DocumentReference> docref = ref.add(fields);
		docref.get();
		return getRatings();
	}
}
