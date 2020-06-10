package com.lingogame.service;

import com.lingogame.model.Game;
import com.lingogame.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.desktop.SystemSleepListener;
import java.util.List;
import java.util.Optional;

@Service
public class GameService implements IGameService {

    @Autowired
    private GameRepository repository;

    @Override
    public List<Game> findAll() {
        return (List<Game>) repository.findAll();
    }

    @Override
    public Game save(Game game) {
        return repository.save(game);
    }

    @Override
    public Optional<Game> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public String generateFeedback(String correctWord, String guessedWord) {
        StringBuilder feedbackBuilder = new StringBuilder();
        for(int i = 0; i < correctWord.length(); i++) {
            if(guessedWord.charAt(i) == correctWord.charAt(i)) {
                correctWord = correctWord.substring(0, i) + "." + correctWord.substring(i + 1);
                feedbackBuilder.append("c");
            } else {
                feedbackBuilder.append("f");
            }
        }

        String feedback = feedbackBuilder.toString();

        for(int j = 0; j < correctWord.length(); j++) {
            if(correctWord.indexOf(guessedWord.charAt(j)) != -1) {
                System.out.println(j);
                System.out.println(guessedWord.charAt(j));
                System.out.println(correctWord.indexOf(guessedWord.charAt(j)));
                feedback = feedback.substring(0, j) + "a" + feedback.substring(j + 1);
                correctWord = correctWord.substring(0, correctWord.indexOf(guessedWord.charAt(j))) + "." + correctWord.substring(correctWord.indexOf(guessedWord.charAt(j)) + 1);
            }
        }

        System.out.println(guessedWord);
        System.out.println(correctWord);
        System.out.println(feedback);

        return feedback;
    }
}
