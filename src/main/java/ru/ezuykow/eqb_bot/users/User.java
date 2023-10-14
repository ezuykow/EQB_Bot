package ru.ezuykow.eqb_bot.users;

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
@Table(name = "user_")
@Getter
@Setter
public class User {

    @Id
    @Column(name = "ID")
    private UUID id;

    @Column(name = "username", nullable = false)
    protected String username;

    @Column(name = "password")
    protected String password;

    @Column(name = "FIRST_NAME")
    protected String firstName;

    @Column(name = "LAST_NAME")
    protected String lastName;

    @Column(name = "EMAIL")
    protected String email;

    @Column(name = "ACTIVE")
    protected Boolean active;

    @Column(name = "TIME_ZONE_ID")
    protected String timeZoneId;

    @Column(name = "TELEGRAM_USER_ID")
    private Long telegramUserId;
}