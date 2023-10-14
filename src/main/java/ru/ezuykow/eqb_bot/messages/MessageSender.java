package ru.ezuykow.eqb_bot.messages;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.PinChatMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author ezuykow
 */
@Component
@RequiredArgsConstructor
public class MessageSender {

    private final TelegramBot bot;
    private final MessageService messageService;

    //region API

    public void sendMessage(long chatId, String key) {
        bot.execute(new SendMessage(chatId, messageService.get(key)));
    }

    public SendResponse sendText(long chatId, String text) {
        return bot.execute(new SendMessage(chatId, text));
    }

    public void sendAndPinText(long chatId, String text) {
        SendResponse response = sendText(chatId, text);
        bot.execute(new PinChatMessage(chatId, response.message().messageId()));
    }

    //endregion
}