package ru.ezuykow.eqb_bot.games;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author ezuykow
 */
@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
}