package ru.ezuykow.eqb_bot.questions;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ezuykow.eqb_bot.questions.Question;
import ru.ezuykow.eqb_bot.questions.QuestionRepository;

/**
 * @author ezuykow
 */
@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    //region API

    @Nullable
    public Question findByOrderPosition(int position) {
        return questionRepository.findByOrderPosition(position);
    }

    //endregion
}