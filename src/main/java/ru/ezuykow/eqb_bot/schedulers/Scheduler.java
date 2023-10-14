package ru.ezuykow.eqb_bot.schedulers;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.ezuykow.eqb_bot.prepared_games.PreparedGame;
import ru.ezuykow.eqb_bot.prepared_games.PreparedGameService;

import java.util.List;

/**
 * @author ezuykow
 */
@Component
@RequiredArgsConstructor
public class Scheduler {

    private static final int MIN_IN_MILLIS = 60_000;

    private final PreparedGameService preparedGameService;

    //region API

    @Scheduled(fixedRate = MIN_IN_MILLIS)
    public void gameTimer() {
        List<PreparedGame> startedGames = preparedGameService.findAllStarted();

        startedGames.forEach(sg -> {
            sg.setMinToEnd(sg.getMinToEnd() - 1);
            if (sg.getMinToEnd() == 0) {
                preparedGameService.endGame(sg);
            }
        });
        preparedGameService.saveAll(startedGames);
    }

    //endregion
}