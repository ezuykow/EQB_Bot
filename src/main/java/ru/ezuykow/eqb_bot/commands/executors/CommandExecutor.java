package ru.ezuykow.eqb_bot.commands.executors;

import ru.ezuykow.eqb_bot.updates.Update;

/**
 * @author ezuykow
 */
public interface CommandExecutor {

    void exec(Update update);
}