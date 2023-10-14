package ru.ezuykow.eqb_bot.messages;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * @author ezuykow
 */
@Entity
@Table(name = "bot_message")
@Getter
@Setter
public class BotMessage {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "message")
    private String message;

    @Column(name = "default_message")
    private String defaultMessage;

    @Column(name= "key_")
    private String key;
}