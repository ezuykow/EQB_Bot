package ru.ezuykow.eqb_bot.commands.executors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ezuykow.eqb_bot.prepared_games.PreparedGame;
import ru.ezuykow.eqb_bot.prepared_games.PreparedGameService;
import ru.ezuykow.eqb_bot.updates.Update;
import ru.ezuykow.eqb_bot.utils.Validator;

/**
 * @author ezuykow
 */
@Component
@RequiredArgsConstructor
public class StopCommandExecutor implements CommandExecutor{

    private final Validator validator;
    private final PreparedGameService preparedGameService;

    //region API

    @Override
    public void exec(Update update) {
        if (!validator.isFromAdminAndOneArgExist(update)) {
            return;
        }

        String[] args = update.getFullCommand().args();
        PreparedGame preparedGame = preparedGameService.findByKey(args[0]);
        if (validator.isPreparedGameNotExist(preparedGame, update)) {
            return;
        }

        preparedGameService.endGame(preparedGame);
    }

    //endregion
}