package ru.ezuykow.eqb_bot.commands.executors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ezuykow.eqb_bot.messages.MessageSender;
import ru.ezuykow.eqb_bot.prepared_games.PreparedGame;
import ru.ezuykow.eqb_bot.prepared_games.PreparedGameService;
import ru.ezuykow.eqb_bot.updates.Update;
import ru.ezuykow.eqb_bot.users.UserService;

import java.util.List;

/**
 * @author ezuykow
 */
@Component
@RequiredArgsConstructor
public class StopCommandExecutor implements CommandExecutor{

    private final UserService userService;
    private final MessageSender messageSender;
    private final PreparedGameService preparedGameService;

    //region API

    @Override
    public void exec(Update update) {
        if (!userService.isExistByTgUserId(update.getSenderId())) {
            messageSender.sendMessage(update.getChatId(), "command-for-admin-only");
            return;
        }

        List<PreparedGame> startedGames = preparedGameService.findAllStarted();
        startedGames.forEach(preparedGameService::endGame);
    }

    //endregion
}