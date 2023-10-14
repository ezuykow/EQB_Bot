package ru.ezuykow.eqb_bot.utils;

import ru.ezuykow.eqb_bot.teams.Team;

import java.util.Comparator;

/**
 * @author ezuykow
 */
public class TeamForPlaceComparator implements Comparator<Team> {

    @Override
    public int compare(Team t1, Team t2) {
        if (t1.getQuestionAnswered() > t2.getQuestionAnswered()) {
            return -1;
        }
        if (t1.getQuestionAnswered() < t2.getQuestionAnswered()) {
            return 1;
        }
        if (t1.getLastQuestionAnsweredAt() == null && t2.getLastQuestionAnsweredAt() == null) {
            return 0;
        }
        if (t1.getLastQuestionAnsweredAt() == null) {
            return 1;
        }
        if (t2.getLastQuestionAnsweredAt() == null) {
            return -1;
        }
        if (t1.getLastQuestionAnsweredAt().isAfter(t1.getLastQuestionAnsweredAt())) {
            return 1;
        } else {
            return -1;
        }
    }
}