package ru.ezuykow.eqb_bot.questions;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import ru.ezuykow.eqb_bot.hints.Hint;
import ru.ezuykow.eqb_bot.users.User;

import java.util.List;
import java.util.UUID;

/**
 * @author ezuykow
 */
@Entity
@Table(name = "question")
@Getter
@Setter
public class Question {

    @Id
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @Column(name = "question_group_id")
    private UUID questionGroupId;

    @Column(name = "order_position")
    private Integer orderPosition;

    @Column(name = "text")
    private String text;

    @Column(name = "answers")
    private String answers;

    @Column(name = "additional_info")
    private String addInfo;

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    private List<Hint> hints;
}