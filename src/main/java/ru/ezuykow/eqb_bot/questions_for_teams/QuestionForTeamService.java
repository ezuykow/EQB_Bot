package ru.ezuykow.eqb_bot.questions_for_teams;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ezuykow.eqb_bot.teams.Team;

import java.util.List;

/**
 * @author ezuykow
 */
@Service
@RequiredArgsConstructor
public class QuestionForTeamService {

    private final QuestionForTeamRepository questionForTeamRepository;

    //region API

    public void saveAll(List<QuestionForTeam> questions) {
        questionForTeamRepository.saveAll(questions);
    }

    public List<QuestionForTeam> findAllByTeam(Team team) {
        return questionForTeamRepository.findAllByTeam(team);
    }

    public QuestionForTeam findByTeamAndOrderPosition(Team team, int orderPosition) {
        return questionForTeamRepository.findByTeamAndOrderPosition(team, orderPosition);
    }

    public void deleteAll(List<QuestionForTeam> questions) {
        questionForTeamRepository.deleteAll(questions);
    }

    //endregion
}