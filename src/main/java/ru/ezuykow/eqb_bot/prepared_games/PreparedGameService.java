package ru.ezuykow.eqb_bot.prepared_games;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ezuykow.eqb_bot.messages.MessageSender;
import ru.ezuykow.eqb_bot.messages.MessageService;
import ru.ezuykow.eqb_bot.teams.Team;
import ru.ezuykow.eqb_bot.teams.TeamService;
import ru.ezuykow.eqb_bot.users.UserService;
import ru.ezuykow.eqb_bot.utils.TeamForPlaceComparator;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ezuykow
 */
@Service
@RequiredArgsConstructor
public class PreparedGameService {

    private final TeamService teamService;
    private final UserService userService;
    private final MessageSender messageSender;
    private final MessageService messageService;
    private final PreparedGameRepository preparedGameRepository;

    //region API

    public List<PreparedGame> findAllStarted() {
        return preparedGameRepository.findByStartedAtNotNull();
    }

    @Nullable
    public PreparedGame findFirst() {
        List<PreparedGame> preparedGames = preparedGameRepository.findAll();
        if (preparedGames.isEmpty()) {
            return null;
        }
        return preparedGames.get(0);
    }

    public void save(PreparedGame preparedGame) {
        preparedGameRepository.save(preparedGame);
    }

    public void saveAll(List<PreparedGame> games) {
        preparedGameRepository.saveAll(games);
    }

    public void endGame(PreparedGame preparedGame) {
        List<Team> teams = teamService.findByPreparedGame(preparedGame);

        StringBuilder sb = new StringBuilder();
        sb.append("Results:\n\n");
        AtomicInteger place = new AtomicInteger(1);
        teams.sort(new TeamForPlaceComparator());
        teams.forEach(t -> {
            t.setPlace(place.get());
            messageSender.sendText(t.getChatId(),
                    messageService.get("game-over") + "\n"
                            + messageService.get("tasks-completed") + " " + t.getQuestionAnswered() + "\n"
                            + messageService.get("place") + " " + t.getPlace());
            sb.append("Place: ").append(place.getAndIncrement()).append(", Team: ").append(t.getName()).append(";\n");
        });

        preparedGame.setStartedAt(null);
        preparedGame.setMinToEnd(null);
        save(preparedGame);

        messageSender.sendText(userService.findByUsername("owner").getTelegramUserId(), sb.toString());

        teamService.removeAll(teams);
    }

    //endregion
}