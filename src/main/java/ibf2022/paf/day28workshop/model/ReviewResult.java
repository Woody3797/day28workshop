package ibf2022.paf.day28workshop.model;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class ReviewResult {
    
    private String rating;
    private String timestamp;
    private List<Review> games;

    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public List<Review> getGames() {
        return games;
    }
    public void setGames(List<Review> games) {
        this.games = games;
    }

    // public static ReviewResult create(Document d) {
    //     ReviewResult reviewResult = new ReviewResult();
    //     reviewResult.setRating(d.getString("rating"));
    //     reviewResult.setTimestamp(d.getString("c_id"));      
    //     return reviewResult;
    // }
    
    public JsonObject toJSON() {
        JsonArray result = null;
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Review r : this.getGames()) {
            arrayBuilder.add(r.toJSON());
        }
        result = arrayBuilder.build();
        return Json.createObjectBuilder()
        .add("rating", getRating())
        .add("games", result)
        .add("timestamp", getTimestamp())
        .build();
    }
}
