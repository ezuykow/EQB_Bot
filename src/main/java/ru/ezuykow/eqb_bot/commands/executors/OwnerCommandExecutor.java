package ru.ezuykow.eqb_bot.commands.executors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ezuykow.eqb_bot.messages.MessageSender;
import ru.ezuykow.eqb_bot.updates.Update;
import ru.ezuykow.eqb_bot.users.User;
import ru.ezuykow.eqb_bot.users.UserService;

/**
 * @author ezuykow
 */
@Component
@RequiredArgsConstructor
public class OwnerCommandExecutor implements CommandExecutor {

    private final UserService userService;
    private final MessageSender messageSender;

    //region API

    @Override
    public void exec(Update update) {
        User user = userService.findByUsername("owner");

        if (user.getTelegramUserId() == null) {
            user.setTelegramUserId(update.getSenderId());
            userService.save(user);
            messageSender.sendMessage(update.getChatId(), "owner-assigned");
        } else {
            messageSender.sendMessage(update.getChatId(), "owner-already-assigned");
        }
    }

    //endregion
}