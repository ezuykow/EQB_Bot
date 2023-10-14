package ru.ezuykow.eqb_bot.questions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author ezuykow
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {

    Question findByOrderPosition(int position);
}