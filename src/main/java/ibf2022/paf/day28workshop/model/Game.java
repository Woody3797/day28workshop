package ibf2022.paf.day28workshop.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class Game {
    
    private Integer gid;
    private String name;
    private Integer year;
    private Integer ranking;
    private Integer users_rated;
    private String url;
    private String image;
    private LocalDateTime timestamp;
    private String[] reviews;

    public Integer getGid() {
        return gid;
    }
    public void setGid(Integer gid) {
        this.gid = gid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getYear() {
        return year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }
    public Integer getRanking() {
        return ranking;
    }
    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }
    public Integer getUsers_rated() {
        return users_rated;
    }
    public void setUsers_rated(Integer users_rated) {
        this.users_rated = users_rated;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public String[] getReviews() {
        return reviews;
    }
    public void setReviews(String[] reviews) {
        this.reviews = reviews;
    }

    public static Game create(Document d) {
        Game game = new Game();
        List<Object> reviewsArr = (ArrayList<Object>) d.get("reviews");
        List<String> reviewsURLs = new LinkedList<>();

        for (Object o : reviewsArr) {
            ObjectId objId = (ObjectId) o;
            reviewsURLs.add("/review/" + objId.toString());
        }
        game.setGid(d.getInteger("gid"));
        game.setName(d.getString("name"));
        game.setYear(d.getInteger("year"));
        game.setRanking(d.getInteger("ranking"));
        game.setUsers_rated(d.getInteger("users_rated"));
        game.setUrl(d.getString("url"));
        game.setImage(d.getString("image"));
        game.setTimestamp(LocalDateTime.now());
        game.setReviews((String[]) reviewsURLs.toArray(new String[reviewsURLs.size()]));
        return game;
    }
    
    public JsonObject toJSON() {
        JsonArray reviews = null;
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (String s : this.getReviews()) {
            arrayBuilder.add(s);
        }
        reviews = arrayBuilder.build();

        return Json.createObjectBuilder()
        .add("gid", getGid())
        .add("name", getName())
        .add("year", getYear())
        .add("ranking", getRanking())
        .add("users_rated", getUsers_rated() != null ? getUsers_rated() : 0)
        .add("url", getUrl())
        .add("image", getImage())
        .add("timestamp", getTimestamp().toString())
        .add("reviews", reviews.toString())
        .build();
    }
}
