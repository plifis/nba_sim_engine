package ru.plifis.nba_sim_engine.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.plifis.nbasimmodel.model.dto.GameDto;
import ru.plifis.nbasimmodel.model.dto.PlayerDto;
import ru.plifis.nbasimmodel.model.dto.SkillDto;
import ru.plifis.nbasimmodel.model.dto.StatisticDto;
import ru.plifis.nbasimmodel.model.dto.TeamDto;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GameService {
    private static final Logger LOGGER = LogManager.getLogger(GameService.class);
    //тестовая симуляция
    public GameDto simulationGame(Map<String, TeamDto> teams) throws IllegalAccessException {
        LOGGER.info(
                String.format("Начата симуляция матча между %s и %s",
                        teams.get("home").getName(), teams.get("away").getName()));

        TeamDto home = teams.get("home");
        TeamDto away = teams.get("away");
        GameDto gameDto = new GameDto();
        gameDto.setHomeTeam(home);
        gameDto.setAwayTeam(away);
        Integer sumHome = calculateSumSkills(home);
        Integer sumAway = calculateSumSkills(away);

        gameDto.
                setHomeStatistic(
                        new StatisticDto().setPointsTotal(sumHome))
                .setAwayStatistic(
                        new StatisticDto().setPointsTotal(sumAway));
        LOGGER.info(
                String.format("Закончена симуляция матча между %s и %s, счёт матча: %s : %s",
                        teams.get("home").getName(), teams.get("away").getName(),
                        gameDto.getHomeStatistic().getPointsTotal(), gameDto.getAwayStatistic().getPointsTotal()));
        return gameDto;
    }
    //симуляция-заглушка, которая просто считает сумму скиллов игрокоов команд
    private Integer calculateSumSkills(TeamDto teamDto) throws IllegalAccessException {
        int sum = 0;

        List<SkillDto> skills = teamDto.getPlayersList()
                .stream()
                .map(PlayerDto::getSkillsets)
                .collect(Collectors.toList());

        for (SkillDto skillDto : skills) {
            Field[] fields = skillDto.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!field.getName().equals("id")) {
                    field.setAccessible(true);
                    Object obj  = field.get(skillDto);
                    sum = obj == null ? sum : sum + (byte) obj;
                }
            }
        }

        return sum;
    }
}