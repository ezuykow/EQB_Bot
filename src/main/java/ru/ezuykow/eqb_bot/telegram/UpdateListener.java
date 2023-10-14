package ru.ezuykow.eqb_bot.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ezuykow.eqb_bot.updates.UpdateProcessor;

import java.util.List;

/**
 * @author ezuykow
 */
@Component
@RequiredArgsConstructor
public class UpdateListener implements UpdatesListener {

    private final TelegramBot bot;
    private final UpdateProcessor updateProcessor;

    @PostConstruct
    public void init() {
        bot.setUpdatesListener(this);
    }

    //region API

    @Override
    public int process(List<Update> updates) {
        updates.forEach(updateProcessor::perform);
        return CONFIRMED_UPDATES_ALL;
    }

    //endregion
}