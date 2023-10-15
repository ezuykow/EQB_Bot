package ru.ezuykow.eqb_bot.commands.executors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ezuykow.eqb_bot.messages.MessageSender;
import ru.ezuykow.eqb_bot.messages.MessageService;
import ru.ezuykow.eqb_bot.prepared_games.PreparedGame;
import ru.ezuykow.eqb_bot.prepared_games.PreparedGameService;
import ru.ezuykow.eqb_bot.questions.Question;
import ru.ezuykow.eqb_bot.questions_for_teams.QuestionForTeam;
import ru.ezuykow.eqb_bot.questions_for_teams.QuestionForTeamService;
import ru.ezuykow.eqb_bot.questions_groups.QuestionsGroup;
import ru.ezuykow.eqb_bot.teams.Team;
import ru.ezuykow.eqb_bot.teams.TeamService;
import ru.ezuykow.eqb_bot.updates.Update;
import ru.ezuykow.eqb_bot.utils.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author ezuykow
 */
@Component
@RequiredArgsConstructor
public class RegCommandExecutor implements CommandExecutor{

    private final Validator validator;
    private final TeamService teamService;
    private final MessageSender messageSender;
    private final MessageService messageService;
    private final PreparedGameService preparedGameService;
    private final QuestionForTeamService questionForTeamService;

    //region API

    @Override
    public void exec(Update update) {
        if (!validator.isFromAdminAndOneArgExist(update)) {
            return;
        }

        String[] args = update.getFullCommand().args();
        PreparedGame preparedGame = preparedGameService.findByKey(args[0]);
        if (validator.isPreparedGameNotExist(preparedGame, update)) {
            return;
        }

        Team team = teamService.findByChatId(update.getChatId());
        if (team != null) {
            if (team.getPreparedGame().getKey().equals(args[0])) {
                messageSender.sendMessage(update.getChatId(), "team-already-registered");
            } else {
                team.setPreparedGame(preparedGame);
                teamService.save(team);
                messageSender.sendText(team.getChatId(),
                        messageService.get("team-registered"));
            }
            return;
        }

        team = new Team();
        team.setName(update.getChat().title());
        team.setChatId(update.getChatId());
        team.setPreparedGame(preparedGame);
        team.setPoints(0);
        teamService.save(team);

        List<QuestionForTeam> questionsForTeam = new ArrayList<>();
        for (QuestionsGroup questionGroup : preparedGame.getQuestionGroups()) {
            for (Question question : questionGroup.getQuestions()) {
                QuestionForTeam q = new QuestionForTeam();
                q.setId(UUID.randomUUID());
                q.setTeam(team);
                q.setQuestion(question);
                questionsForTeam.add(q);
            }
        }
        questionForTeamService.saveAll(questionsForTeam);

        messageSender.sendText(team.getChatId(),
                messageService.get("team-registered"));
    }

    //endregion
}