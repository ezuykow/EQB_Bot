package ru.ezuykow.eqb_bot.commands.executors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ezuykow.eqb_bot.messages.MessageSender;
import ru.ezuykow.eqb_bot.messages.MessageService;
import ru.ezuykow.eqb_bot.questions_for_teams.QuestionForTeam;
import ru.ezuykow.eqb_bot.questions_for_teams.QuestionForTeamService;
import ru.ezuykow.eqb_bot.teams.Team;
import ru.ezuykow.eqb_bot.teams.TeamService;
import ru.ezuykow.eqb_bot.updates.Update;
import ru.ezuykow.eqb_bot.utils.Validator;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * @author ezuykow
 */
@Component
@RequiredArgsConstructor
public class StartCommandExecutor implements CommandExecutor{

    private final Validator validator;
    private final TeamService teamService;
    private final MessageSender messageSender;
    private final MessageService messageService;
    private final QuestionForTeamService questionForTeamService;

    //region API

    @Override
    public void exec(Update update) {
        if (validator.isCommandFromNotAdmin(update)) {
            return;
        }

        Team team = teamService.findByChatId(update.getChatId());
        if (team == null) {
            messageSender.sendMessage(update.getChatId(), "team-not-registered");
            return;
        }

        if (!isQuestionsForTeamPrepared(team)) {
            return;
        }

        team.setStartedAt(LocalDateTime.now());
        team.setCurrentQuestionPosition(1);
        team.setHintsUsed(0);
        teamService.save(team);

        QuestionForTeam questionForTeam = questionForTeamService.findByTeamAndOrderPosition(team, 1);
        messageSender.sendAndPinText(team.getChatId(),
                questionForTeam.getQuestion().getText());
        if (team.getPreparedGame().getGame().isAdditionalWithQuestion() && questionForTeam.getQuestion().getAddInfo() != null) {
            messageSender.sendText(team.getChatId(),
                    messageService.get("additional-info") + " " + questionForTeam.getQuestion().getAddInfo());
        }
    }

    //endregion

    private boolean isQuestionsForTeamPrepared(Team team) {
        int gameQuestionsCount = team.getPreparedGame().getGame().getQuestionsCount();
        List<QuestionForTeam> questions =  questionForTeamService.findAllByTeam(team);
        questions.sort(Comparator.comparing(QuestionForTeam::getOrderPosition, Comparator.nullsLast(Comparator.naturalOrder())));

        try {
            for (int pointer = 0; pointer < gameQuestionsCount; pointer++) {
                Integer orderPosition = questions.get(pointer).getOrderPosition();
                if (orderPosition == null || orderPosition != pointer + 1) {
                    messageSender.sendMessage(team.getChatId(), "questions-not-prepared");
                    return false;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            messageSender.sendMessage(team.getChatId(), "questions-not-prepared");
            return false;
        }


        questionForTeamService.deleteAll(questions.subList(gameQuestionsCount, questions.size()));
        return true;
    }
}