package ru.ezuykow.eqb_bot.teams;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ezuykow.eqb_bot.prepared_games.PreparedGame;

import java.util.List;
import java.util.UUID;

/**
 * @author ezuykow
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {

    boolean existsByChatId(Long chatId);

    List<Team> findByPreparedGame(PreparedGame preparedGame);

    Team findByChatId(Long chatId);
}