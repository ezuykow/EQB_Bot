package ru.ezuykow.eqb_bot.questions_groups;

import jakarta.persistence.*;
import lombok.Getter;
import ru.ezuykow.eqb_bot.questions.Question;

import java.util.Set;
import java.util.UUID;

/**
 * @author ezuykow
 */
@Entity
@Table(name = "questions_group")
@Getter
public class QuestionsGroup {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "questionGroup", fetch = FetchType.EAGER)
    private Set<Question> questions;
}