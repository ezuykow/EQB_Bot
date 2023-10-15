package ru.ezuykow.eqb_bot.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ezuykow.eqb_bot.messages.MessageSender;
import ru.ezuykow.eqb_bot.prepared_games.PreparedGame;
import ru.ezuykow.eqb_bot.updates.Update;
import ru.ezuykow.eqb_bot.users.UserService;

/**
 * @author ezuykow
 */
@Component
@RequiredArgsConstructor
public class Validator {

    private final UserService userService;
    private final MessageSender messageSender;

    //region API

    public boolean isFromAdminAndOneArgExist(Update update) {
        return !isCommandFromNotAdmin(update) || isOneArgExist(update);
    }

    public boolean isCommandFromNotAdmin(Update update) {
        if (!userService.isExistByTgUserId(update.getSenderId())) {
            messageSender.sendMessage(update.getChatId(), "command-for-admin-only");
            return true;
        }
        return false;
    }

    public boolean isOneArgExist(Update update) {
        String[] args = update.getFullCommand().args();
        if (args == null || args.length != 1) {
            messageSender.sendMessage(update.getChatId(), "incorrect-args");
            return false;
        }
        return true;
    }

    public boolean isPreparedGameNotExist(PreparedGame preparedGame, Update update) {
        if (preparedGame == null) {
            messageSender.sendMessage(update.getChatId(), "prepared-game-not-exist");
            return true;
        }
        return false;
    }

    //endregion
    //region Utils



    //endregion
}