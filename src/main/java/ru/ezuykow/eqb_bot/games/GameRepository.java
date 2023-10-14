package ru.ezuykow.eqb_bot.games;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author ezuykow
 */
@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {
}