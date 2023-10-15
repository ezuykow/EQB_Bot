package ru.ezuykow.eqb_bot.prepared_games;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ezuykow.eqb_bot.messages.MessageSender;
import ru.ezuykow.eqb_bot.messages.MessageService;
import ru.ezuykow.eqb_bot.teams.Team;
import ru.ezuykow.eqb_bot.teams.TeamService;
import ru.ezuykow.eqb_bot.users.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

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

    @Nullable
    public PreparedGame findByKey(String key) {
        return preparedGameRepository.findByKey(key);
    }

    public void endGame(PreparedGame preparedGame) {
        List<Team> teams = teamService.findByPreparedGame(preparedGame);
        LocalDateTime stopTime = LocalDateTime.now();

        StringBuilder sb = new StringBuilder();
        sb.append("Results:\n\n");
        teams.forEach(t -> {
            long playingMinutes = (t.getStartedAt() != null)
                    ? ChronoUnit.MINUTES.between(t.getStartedAt(), stopTime)
                    : 0;
            String points = messageService.get("points") + " " + t.getPoints() + "\n";
            String playingTime = messageService.get("playing-time") + " " + playingMinutes;
            messageSender.sendText(t.getChatId(),
                    messageService.get("game-over") + "\n" + points + playingTime);
            sb.append("Team: ").append(t.getName()).append("\n")
                    .append(points)
                    .append(playingTime).append(";\n\n");
        });

        messageSender.sendText(userService.findByUsername("owner").getTelegramUserId(), sb.toString());

        teamService.removeAll(teams);
    }

    //endregion
}