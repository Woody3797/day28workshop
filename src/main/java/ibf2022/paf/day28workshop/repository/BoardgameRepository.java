package ibf2022.paf.day28workshop.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation.AddFieldsOperationBuilder;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import ibf2022.paf.day28workshop.model.Game;
import ibf2022.paf.day28workshop.model.Review;

@Repository
public class BoardgameRepository {
    
    @Autowired
    private MongoTemplate template;

    /* db.games.aggregate([
    {
        $match : {gid: 3}
    },
    {
        $lookup : 
        {
            from: "reviews",
            localField: "gid",
            foreignField: "gid",
            as: "reviewsDocs"
        }
    },
    {
        $project: {_id: -1, gid: 1, name: 1, year: 1, ranking: 1, users_rated: 1, url: 1, image: 1, reviews: "$reviewsDocs._id", timestamp: "$$NOW"}
    }
    ]) */

    public Optional<Game> aggregateGameReviews(String gameId) {
        MatchOperation matchGameId = Aggregation.match(
            Criteria.where("gid").is(Integer.parseInt(gameId))
        );
        LookupOperation lookup = Aggregation.lookup("reviews", "gid", "gid", "reviewsDocs");

        ProjectionOperation project = Aggregation.project("_id", "gid", "name", "year", "ranking", "users_rated", "url", "image")
        .and("reviewsDocs._id").as("reviews");

        AddFieldsOperationBuilder add = Aggregation.addFields();
        add.addFieldWithValue("timestamp", LocalDateTime.now());
        AddFieldsOperation added = add.build();

        Aggregation pipeline = Aggregation.newAggregation(matchGameId, lookup, project, added);

        AggregationResults<Document> results = template.aggregate(pipeline, "games", Document.class);

        if (!results.iterator().hasNext()) {
            return Optional.empty();
        }
        Document doc = results.iterator().next();
        Game game = Game.create(doc);
        return Optional.of(game);
    }

    /* db.reviews.aggregate([
    {
        $match : {$and: [{user: "desertfox2004"}, {rating: {$gt: 5}}] }
    },
    {
        $lookup : {from: "games", localField: "gid", foreignField: "gid", as: "gameReviews"}
    },
    {
        $project: {"_id": 1, "c_id": 1, "user": 1, "rating": 1, "c_text": 1, "gid": 1, "game_name": "$gameReviews.name"}
    }
    ]) */

    public List<Review> aggregateMinMaxReviews(Integer limit, String user, String rating) {
        Criteria c = null;
        if (rating.equals("highest")) {
            c = new Criteria().andOperator(Criteria.where("user").is(user),
            Criteria.where("rating").gte(5));
        } else {
            c = new Criteria().andOperator(Criteria.where("user").is(user),
            Criteria.where("rating").lte(4));
        }

        MatchOperation match = Aggregation.match(c);
        LookupOperation lookup = Aggregation.lookup("games", "gid", "gid", "gameReview");
        LimitOperation limitOp = Aggregation.limit(limit);
        ProjectionOperation project = Aggregation.project(
            "_id", "c_id", "user", "rating", "c_text", "gid"
        ).and("gameReview.name").as("game_name");
        

        Aggregation pipeline = Aggregation.newAggregation(match, lookup, limitOp, project);
        AggregationResults<Review> results = template.aggregate(pipeline, "reviews", Review.class);
        return results.getMappedResults();
    }

}
