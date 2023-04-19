package ibf2022.paf.day28workshop.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ibf2022.paf.day28workshop.model.Game;
import ibf2022.paf.day28workshop.model.Review;
import ibf2022.paf.day28workshop.model.ReviewResult;
import ibf2022.paf.day28workshop.service.BoardgameService;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

@RestController
@RequestMapping(path = "/games")
public class BoardgameRESTController {
    
    @Autowired
    BoardgameService service;

    @GetMapping("{gameId}/reviews")
    public ResponseEntity<String> getGameReviewsHistory(@PathVariable String gameId) {
        JsonObject result = null;
        Optional<Game> game = service.aggregateGameReviews(gameId);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("reviews", game.get().toJSON());
        result = builder.build();

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(result.toString());
    }

    @GetMapping("/highest")
    public ResponseEntity<String> getHighestRatedGames(@RequestParam String user, @RequestParam Integer limit) {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(getResultFromRatedReviews(user, limit, "highest").toString());
    }

    @GetMapping("/lowest")
    public ResponseEntity<String> getLowestRatedGames(@RequestParam String user, @RequestParam Integer limit) {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(getResultFromRatedReviews(user, limit, "lowest").toString());
    }


    private JsonObject getResultFromRatedReviews(String user, Integer limit, String rating) {
        JsonObject result = null;
        List<Review> reviews = service.aggregateMinMaxReviews(limit, user, rating);
        JsonObjectBuilder job = Json.createObjectBuilder();
        ReviewResult rr = new ReviewResult();
        rr.setRating(String.valueOf(rating));
        rr.setTimestamp(LocalDateTime.now().toString());
        rr.setGames(reviews);
        job.add("result", rr.toJSON());
        result = job.build();
        return result;
    }
}
