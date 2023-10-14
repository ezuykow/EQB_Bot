package ru.ezuykow.eqb_bot.commands.executors;

import com.pengrad.telegrambot.model.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ezuykow.eqb_bot.messages.MessageSender;
import ru.ezuykow.eqb_bot.messages.MessageService;
import ru.ezuykow.eqb_bot.prepared_games.PreparedGame;
import ru.ezuykow.eqb_bot.prepared_games.PreparedGameService;
import ru.ezuykow.eqb_bot.teams.Team;
import ru.ezuykow.eqb_bot.teams.TeamService;
import ru.ezuykow.eqb_bot.updates.Update;
import ru.ezuykow.eqb_bot.users.UserService;

/**
 * @author ezuykow
 */
@Component
@RequiredArgsConstructor
public class RegCommandExecutor implements CommandExecutor{

    private final UserService userService;
    private final TeamService teamService;
    private final MessageSender messageSender;
    private final MessageService messageService;
    private final PreparedGameService preparedGameService;

    //region API

    @Override
    public void exec(Update update) {
        if (!userService.isExistByTgUserId(update.getSenderId())) {
            messageSender.sendMessage(update.getChatId(), "command-for-admin-only");
            return;
        }

        if (teamService.isExistByChatId(update.getChatId())) {
            messageSender.sendMessage(update.getChatId(), "team-already-registered");
            return;
        }

        PreparedGame preparedGame = preparedGameService.findFirst();
        if (preparedGame == null) {
            messageSender.sendMessage(update.getChatId(), "prepared-game-not-exist");
            return;
        }
        if (preparedGame.getStartedAt() != null) {
            messageSender.sendMessage(update.getChatId(), "game-already-started");
            return;
        }

        Team newTeam = new Team();
        newTeam.setName(update.getChat().title());
        newTeam.setChatId(update.getChatId());
        newTeam.setPreparedGame(preparedGame);
        teamService.save(newTeam);

        messageSender.sendText(newTeam.getChatId(),
                messageService.get("team-registered"));
    }

    //endregion
}