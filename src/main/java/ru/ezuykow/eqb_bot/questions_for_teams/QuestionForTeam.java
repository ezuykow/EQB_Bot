package ru.ezuykow.eqb_bot.questions_for_teams;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.ezuykow.eqb_bot.questions.Question;
import ru.ezuykow.eqb_bot.teams.Team;

import java.util.UUID;

/**
 * @author ezuykow
 */
@Entity
@Table(name = "questions_for_team")
@Getter
@Setter
public class QuestionForTeam {

    @Id
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name = "order_position")
    private Integer orderPosition;
}