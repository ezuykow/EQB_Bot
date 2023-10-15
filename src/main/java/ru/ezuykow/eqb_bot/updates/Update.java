package ru.ezuykow.eqb_bot.updates;

import com.pengrad.telegrambot.model.Chat;
import lombok.Getter;
import ru.ezuykow.eqb_bot.commands.Command;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author ezuykow
 */
@Getter
public class Update {

    public enum UpdateType {
        COMMAND, TEXT, UNKNOWN
    }

    public record FullCommand(Command command, String[] args) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FullCommand that = (FullCommand) o;
            return command == that.command && Arrays.equals(args, that.args);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(command);
            result = 31 * result + Arrays.hashCode(args);
            return result;
        }

        @Override
        public String toString() {
            return "FullCommand{" +
                    "command=" + command +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }

    private final com.pengrad.telegrambot.model.Update rawUpdate;
    private UpdateType updateType;
    private long chatId;
    private long senderId;
    private FullCommand fullCommand;

    public Update(com.pengrad.telegrambot.model.Update rawUpdate) {
        this.rawUpdate = rawUpdate;
        checkUpdateType();
        resolveChatId();
        resolveSenderId();
        if (updateType == UpdateType.COMMAND) {
            parseFullCommand();
        }
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

    private void parseFullCommand() {
        String text = getMessageText();
        try {
            if (text.contains(" ")) {
                Command command = Command.valueOf(text.substring(1, text.indexOf(" ")).toUpperCase());
                fullCommand = new FullCommand(command, text.substring(text.indexOf(" ") + 1).split(" "));
            } else {
                Command command = Command.valueOf(text.substring(1).toUpperCase());
                fullCommand = new FullCommand(command, null);
            }
        } catch (IllegalArgumentException e) {
            fullCommand = new FullCommand(Command.UNKNOWN, null);
        }

    }
}