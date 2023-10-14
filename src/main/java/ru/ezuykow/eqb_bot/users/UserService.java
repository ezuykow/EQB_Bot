package ru.ezuykow.eqb_bot.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author ezuykow
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    //region API

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public boolean isExistByTgUserId(Long tgUserId) {
        return userRepository.existsByTelegramUserId(tgUserId);
    }

    //endregion
}