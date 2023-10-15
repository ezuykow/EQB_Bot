package ru.ezuykow.eqb_bot.teams;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ezuykow.eqb_bot.prepared_games.PreparedGame;

import java.util.List;
import java.util.UUID;

/**
 * @author ezuykow
 */
@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    //region API

    public List<Team> findByPreparedGame(PreparedGame preparedGame) {
        return teamRepository.findByPreparedGame(preparedGame);
    }

    @Nullable
    public Team findByChatId(Long chatId) {
        return teamRepository.findByChatId(chatId);
    }

    public void save(Team team) {
        team.setVersion(1);
        if (team.getId() == null) {
            team.setId(UUID.randomUUID());
        }
        teamRepository.save(team);
    }

    public void removeAll(List<Team> teams) {
        teamRepository.deleteAll(teams);
    }

    //endregion
}