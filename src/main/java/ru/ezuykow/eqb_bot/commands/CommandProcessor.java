package ru.ezuykow.eqb_bot.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ezuykow.eqb_bot.commands.executors.OwnerCommandExecutor;
import ru.ezuykow.eqb_bot.commands.executors.RegCommandExecutor;
import ru.ezuykow.eqb_bot.commands.executors.StartCommandExecutor;
import ru.ezuykow.eqb_bot.commands.executors.StopCommandExecutor;
import ru.ezuykow.eqb_bot.messages.MessageSender;
import ru.ezuykow.eqb_bot.updates.Update;

/**
 * @author ezuykow
 */
@Component
@RequiredArgsConstructor
public class CommandProcessor {

    private final MessageSender messageSender;

    private final RegCommandExecutor regCommandExecutor;
    private final StartCommandExecutor startCommandExecutor;
    private final OwnerCommandExecutor ownerCommandExecutor;
    private final StopCommandExecutor stopCommandExecutor;

    //region API

    public void perform(Update update) {
        switch (update.getFullCommand().command()) {
            case OWNER -> ownerCommandExecutor.exec(update);
            case REG -> regCommandExecutor.exec(update);
            case START -> startCommandExecutor.exec(update);
            case STOP -> stopCommandExecutor.exec(update);
            case UNKNOWN -> messageSender.sendMessage(update.getChatId(), "unknown-command");
        }
    }

    //endregion
}