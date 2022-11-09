package ru.plifis.nba_sim_engine.controller;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.plifis.nba_sim_engine.service.GameService;
import ru.plifis.nbasimmodel.model.dto.GameDto;
import ru.plifis.nbasimmodel.model.dto.TeamDto;

import java.util.Map;

@RestController
public class GameControllers {
    private GameService gameService;
    private KafkaTemplate<Long, GameDto> kafkaTemplate;

    public GameControllers(GameService gameService, KafkaTemplate<Long, GameDto> kafkaTemplate) {
        this.gameService = gameService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PutMapping("/simulate_game")
    public void getSimulationGame(@RequestBody Map<String, TeamDto> teams) throws IllegalAccessException {
        GameDto game = gameService.simulationGame(teams);
        kafkaTemplate.send("results", game);
    }

}