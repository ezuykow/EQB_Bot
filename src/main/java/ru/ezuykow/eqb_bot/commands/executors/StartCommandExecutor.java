package ru.ezuykow.eqb_bot.commands.executors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ezuykow.eqb_bot.questions.QuestionService;
import ru.ezuykow.eqb_bot.messages.MessageSender;
import ru.ezuykow.eqb_bot.messages.MessageService;
import ru.ezuykow.eqb_bot.prepared_games.PreparedGame;
import ru.ezuykow.eqb_bot.prepared_games.PreparedGameService;
import ru.ezuykow.eqb_bot.questions.Question;
import ru.ezuykow.eqb_bot.teams.Team;
import ru.ezuykow.eqb_bot.teams.TeamService;
import ru.ezuykow.eqb_bot.updates.Update;
import ru.ezuykow.eqb_bot.users.UserService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ezuykow
 */
@Component
@RequiredArgsConstructor
public class StartCommandExecutor implements CommandExecutor{

    private final TeamService teamService;
    private final UserService userService;
    private final MessageSender messageSender;
    private final MessageService messageService;
    private final QuestionService questionService;
    private final PreparedGameService preparedGameService;

    //region API

    @Override
    public void exec(Update update) {
        if (!userService.isExistByTgUserId(update.getSenderId())) {
            messageSender.sendMessage(update.getChatId(), "command-for-admin-only");
            return;
        }

        PreparedGame preparedGame = preparedGameService.findFirst();
        if (preparedGame == null) {
            messageSender.sendMessage(update.getChatId(), "prepared-game-not-exist");
            return;
        }
        if (preparedGame.getStartedAt() != null) {
            messageSender.sendMessage(update.getChatId(), "game-already-started");
            return;
        }

        preparedGame.setStartedAt(LocalDateTime.now());
        preparedGame.setMinToEnd(preparedGame.getGame().getDuration());
        preparedGameService.save(preparedGame);

        List<Team> teams = teamService.findByPreparedGame(preparedGame);
        Question firstQuestion = questionService.findByOrderPosition(1);
        teams.forEach(t -> {
            t.setQuestionAnswered(0);
            messageSender.sendAndPinText(t.getChatId(),
                    firstQuestion.getText());
            if (preparedGame.getGame().isAdditionalWithQuestion() && firstQuestion.getAddInfo() != null) {
                messageSender.sendText(t.getChatId(),
                        messageService.get("additional-info") + " " + firstQuestion.getAddInfo());
            }
        });
    }

    //endregion
}