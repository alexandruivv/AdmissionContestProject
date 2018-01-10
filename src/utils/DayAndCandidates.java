package utils;


import domain.Candidate;

import java.util.List;

public class DayAndCandidates {
    private DayAndMonth dayAndMonth;
    private List<Candidate> candidates;

    public DayAndCandidates(DayAndMonth dayAndMonth, List<Candidate> candidates) {
        this.dayAndMonth = dayAndMonth;
        this.candidates = candidates;
    }

    public DayAndMonth getDayAndMonth() {
        return dayAndMonth;
    }

    public void setDayAndMonth(DayAndMonth dayAndMonth) {
        this.dayAndMonth = dayAndMonth;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }
}
