package ru.ezuykow.eqb_bot.text;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ezuykow.eqb_bot.hints.Hint;
import ru.ezuykow.eqb_bot.messages.MessageSender;
import ru.ezuykow.eqb_bot.messages.MessageService;
import ru.ezuykow.eqb_bot.questions.Question;
import ru.ezuykow.eqb_bot.questions_for_teams.QuestionForTeam;
import ru.ezuykow.eqb_bot.questions_for_teams.QuestionForTeamService;
import ru.ezuykow.eqb_bot.teams.Team;
import ru.ezuykow.eqb_bot.teams.TeamService;
import ru.ezuykow.eqb_bot.updates.Update;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ezuykow
 */
@Component
@RequiredArgsConstructor
public class TextProcessor {

    private final TeamService teamService;
    private final MessageSender messageSender;
    private final MessageService messageService;
    private final QuestionForTeamService questionForTeamService;

    //region API

    public void perform(Update update) {
        Team team = teamService.findByChatId(update.getChatId());
        if (team == null) {
            return;
        }

        Question currentQuestion =
                questionForTeamService.findByTeamAndOrderPosition(team, team.getCurrentQuestionPosition()).getQuestion();

        Set<String> answers = getAnswersSet(currentQuestion);

        String teamAnswer = update.getMessageText().trim().toUpperCase();

        if (answers.contains(teamAnswer)) {
            teamAnsweredQuestion(team, currentQuestion, teamAnswer);
        } else {
            teamNotAnsweredQuestion(team, currentQuestion);
        }
        teamService.save(team);
    }

    //endregion

    private Set<String> getAnswersSet(Question question) {
        return Arrays.stream(question.getAnswers().split(","))
                .map(a -> {
                    a = a.trim();
                    a = a.toUpperCase();
                    return a;
                })
                .collect(Collectors.toSet());
    }

    private void teamAnsweredQuestion(Team team, Question currentQuestion, String teamAnswer) {
        boolean addInfoWithQuestion = team.getPreparedGame().getGame().isAdditionalWithQuestion();
        messageSender.sendText(team.getChatId(),
                messageService.get("accepted-answer") + " " + teamAnswer);
        if (!addInfoWithQuestion && currentQuestion.getAddInfo() != null) {
            messageSender.sendText(team.getChatId(),
                    messageService.get("additional-info") + " " + currentQuestion.getAddInfo());
        }

        team.setPoints(team.getPoints() + 1);

        sendNextQuestion(team);
    }

    private void teamNotAnsweredQuestion(Team team, Question currentQuestion) {
        int maxAttemptCount = team.getPreparedGame().getGame().getAttemptToAnswerCount();
        List<Hint> hints = currentQuestion.getHints();

        if (team.getHintsUsed() >= (maxAttemptCount - 1) || team.getHintsUsed() >= hints.size()) {
            messageSender.sendText(team.getChatId(),
                    messageService.get("cant-answer") + " " + currentQuestion.getText() + "\n"
                            + messageService.get("right-answers") + " " + currentQuestion.getAnswers());

            sendNextQuestion(team);
        } else {
            sendHint(team, hints);
        }
    }

    private void sendNextQuestion(Team team) {
        team.setCurrentQuestionPosition(team.getCurrentQuestionPosition() + 1);
        team.setHintsUsed(0);

        QuestionForTeam nextQuestionForTeam =
                questionForTeamService.findByTeamAndOrderPosition(team, team.getCurrentQuestionPosition());
        if (nextQuestionForTeam != null) {
            Question nextQuestion = nextQuestionForTeam.getQuestion();
            messageSender.sendAndPinText(team.getChatId(), nextQuestion.getText());
            if (team.getPreparedGame().getGame().isAdditionalWithQuestion() && nextQuestion.getAddInfo() != null) {
                messageSender.sendText(team.getChatId(),
                        messageService.get("additional-info") + " " + nextQuestion.getAddInfo());
            }
        } else {
            messageSender.sendText(team.getChatId(),
                    messageService.get("questions-ended"));
        }
    }

    private void sendHint(Team team, List<Hint> hints) {
        Optional<Hint> currHint = hints.stream()
                .sorted(Comparator.comparing(Hint::getId)).skip(team.getHintsUsed()).findFirst();

        currHint.ifPresent(hint -> messageSender.sendText(team.getChatId(),
                messageService.get("hint") + "\n" + hint.getText()));

        team.setHintsUsed(team.getHintsUsed() + 1);
    }

}