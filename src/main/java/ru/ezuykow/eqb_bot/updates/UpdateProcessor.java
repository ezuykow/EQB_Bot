package ru.ezuykow.eqb_bot.updates;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ezuykow.eqb_bot.commands.CommandProcessor;
import ru.ezuykow.eqb_bot.text.TextProcessor;

/**
 * @author ezuykow
 */
@Component
@RequiredArgsConstructor
public class UpdateProcessor {

    private final TextProcessor textProcessor;
    private final CommandProcessor commandProcessor;

    //region API

    public void perform(com.pengrad.telegrambot.model.Update rawUpdate){
        Update update = Update.decorate(rawUpdate);

        switch (update.getUpdateType()) {
            case COMMAND -> commandProcessor.perform(update);
            case TEXT -> textProcessor.perform(update);
        }
    }

    //endregion
}