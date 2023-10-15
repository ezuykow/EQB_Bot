package ru.ezuykow.eqb_bot.questions_for_teams;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ezuykow.eqb_bot.teams.Team;

import java.util.List;
import java.util.UUID;

/**
 * @author ezuykow
 */
@Repository
public interface QuestionForTeamRepository extends JpaRepository<QuestionForTeam, UUID> {

    QuestionForTeam findByTeamAndOrderPosition(Team team, Integer orderPosition);

    List<QuestionForTeam> findAllByTeam(Team team);
}