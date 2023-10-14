package ru.ezuykow.eqb_bot.messages;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author ezuykow
 */
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    //region API

    public String get(String key) {
        return messageRepository.findByKey(key).getMessage();
    }

    //endregion
}