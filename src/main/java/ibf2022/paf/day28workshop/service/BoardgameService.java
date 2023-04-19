package ibf2022.paf.day28workshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ibf2022.paf.day28workshop.model.Game;
import ibf2022.paf.day28workshop.model.Review;
import ibf2022.paf.day28workshop.repository.BoardgameRepository;

@Service
public class BoardgameService {
    
    @Autowired
    private BoardgameRepository repository;


    public Optional<Game> aggregateGameReviews(String gameId) {
        return repository.aggregateGameReviews(gameId);
    }

    public List<Review> aggregateMinMaxReviews(Integer limit, String user, String rating) {
        return repository.aggregateMinMaxReviews(limit, user, rating);
    }



}
