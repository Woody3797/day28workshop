# Day28 Workshop

a) Get board game with all its corresponding reviews

```
db.games.aggregate([
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
])

```


b) Listing board games by their highest or lowest ratings

```
db.reviews.aggregate([
    {
        $match : {$and: [{user: "desertfox2004"}, {rating: {$gt: 5}}] }
    },
    {
        $lookup : {from: "games", localField: "gid", foreignField: "gid", as: "gameReviews"}
    },
    {
        $project: {"_id": 1, "c_id": 1, "user": 1, "rating": 1, "c_text": 1, "gid": 1, "game_name": "$gameReviews.name"}
    }
])
```