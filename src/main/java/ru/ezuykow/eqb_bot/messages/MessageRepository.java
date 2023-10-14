package ru.ezuykow.eqb_bot.messages;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ezuykow
 */
@Repository
public interface MessageRepository extends JpaRepository<BotMessage, String> {

    BotMessage findByKey(String key);

}