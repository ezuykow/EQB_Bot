package ru.ezuykow.eqb_bot.prepared_games;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author ezuykow
 */
@Repository
public interface PreparedGameRepository extends JpaRepository<PreparedGame, UUID> {

    PreparedGame findByKey(String key);
}