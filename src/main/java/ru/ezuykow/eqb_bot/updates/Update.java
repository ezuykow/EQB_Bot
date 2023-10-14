package ru.ezuykow.eqb_bot.updates;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import lombok.Getter;

/**
 * @author ezuykow
 */
@Getter
public class Update {

    public enum UpdateType {
        COMMAND, TEXT, UNKNOWN;
    }

    private final com.pengrad.telegrambot.model.Update rawUpdate;
    private UpdateType updateType;
    private long chatId;
    private long senderId;

    public Update(com.pengrad.telegrambot.model.Update rawUpdate) {
        this.rawUpdate = rawUpdate;
        checkUpdateType();
        resolveChatId();
        resolveSenderId();
    }

    //region API

    public static Update decorate(com.pengrad.telegrambot.model.Update rawUpdate) {
        return new Update(rawUpdate);
    }


    public boolean hasMessage() {
        return rawUpdate.message() != null;
    }

    public boolean hasMessageText() {
        return hasMessage() && rawUpdate.message().text() != null;
    }

    public String getMessageText() {
        return rawUpdate.message().text();
    }

    public Chat getChat() {
        if (hasMessage()) {
            return rawUpdate.message().chat();
        }
        return null;
    }

    //endregion

    private void checkUpdateType() {
        if (hasMessageText()) {
            updateType = getMessageText().startsWith("/")
                    ? UpdateType.COMMAND
                    : UpdateType.TEXT;
        } else {
            updateType = UpdateType.UNKNOWN;
        }
    }

    private void resolveChatId() {
        if (hasMessage()) {
            chatId = rawUpdate.message().chat().id();
        }
    }

    private void  resolveSenderId() {
        if (hasMessage()) {
            senderId = rawUpdate.message().from().id();
        }
    }
}