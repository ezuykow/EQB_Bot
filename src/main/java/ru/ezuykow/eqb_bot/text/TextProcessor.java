package ru.ezuykow.eqb_bot.text;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ezuykow.eqb_bot.hints.Hint;
import ru.ezuykow.eqb_bot.questions.QuestionService;
import ru.ezuykow.eqb_bot.messages.MessageSender;
import ru.ezuykow.eqb_bot.messages.MessageService;
import ru.ezuykow.eqb_bot.questions.Question;
import ru.ezuykow.eqb_bot.teams.Team;
import ru.ezuykow.eqb_bot.teams.TeamService;
import ru.ezuykow.eqb_bot.updates.Update;

import java.time.LocalDateTime;
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
    private final QuestionService questionService;

    //region API

    public void perform(Update update) {
        Team team = teamService.findByChatId(update.getChatId());
        if (team == null) {
            return;
        }

        Question currentQuestion =  questionService.findByOrderPosition(team.getQuestionAnswered() + 1);
        if (currentQuestion == null) {
            return;
        }

        Set<String> answers = Arrays.stream(currentQuestion.getAnswers().split(","))
                .map(a -> {
                    a = a.trim();
                    a = a.toUpperCase();
                    return a;
                })
                .collect(Collectors.toSet());

        String teamAnswer = update.getMessageText().trim().toUpperCase();

        if (answers.contains(teamAnswer)) {
            boolean addInfoWithQuestion = team.getPreparedGame().getGame().isAdditionalWithQuestion();
            messageSender.sendText(team.getChatId(),
                    messageService.get("accepted-answer") + " " + teamAnswer);
            if (!addInfoWithQuestion && currentQuestion.getAddInfo() != null) {
                messageSender.sendText(team.getChatId(),
                        messageService.get("additional-info") + " " + currentQuestion.getAddInfo());
            }

            team.setQuestionAnswered(team.getQuestionAnswered() + 1);
            team.setLastQuestionAnsweredAt(LocalDateTime.now());
            team.setHintsUsed(0);
            teamService.save(team);

            Question nextQuestion = questionService.findByOrderPosition(currentQuestion.getOrderPosition() + 1);
            if (nextQuestion != null) {
                messageSender.sendAndPinText(team.getChatId(), nextQuestion.getText());
                if (addInfoWithQuestion && nextQuestion.getAddInfo() != null) {
                    messageSender.sendText(team.getChatId(),
                            messageService.get("additional-info") + " " + nextQuestion.getAddInfo());
                }
            } else {
                messageSender.sendText(team.getChatId(),
                        messageService.get("questions-ended"));
            }
        } else {
            int maxHintsCount = team.getPreparedGame().getGame().getMaxHintsPerQuestion();
            List<Hint> hints = currentQuestion.getHints();
            if (maxHintsCount <= team.getHintsUsed() || hints.size() <= team.getHintsUsed()) {
                return;
            }

            Optional<Hint> currHint = hints.stream()
                    .sorted(Comparator.comparing(Hint::getId)).skip(team.getHintsUsed()).findFirst();

            currHint.ifPresent(hint -> messageSender.sendText(team.getChatId(),
                    messageService.get("hint") + "\n" + hint.getText()));

            team.setHintsUsed(team.getHintsUsed() + 1);
            teamService.save(team);
        }
    }

    //endregion
}