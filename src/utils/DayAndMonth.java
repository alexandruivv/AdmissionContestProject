package utils;

public class DayAndMonth {
    private int day;
    private int month;

    public DayAndMonth(){}

    public DayAndMonth(int day, int month) {
        this.day = day;
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DayAndMonth dayAndMonth1 = (DayAndMonth) o;

        if (day != dayAndMonth1.day) return false;
        return month == dayAndMonth1.month;
    }

    @Override
    public int hashCode() {
        int result = day;
        result = 31 * result + month;
        return result;
    }

    @Override
    public String toString() {
        return "DayAndMonth{" +
                "day=" + day +
                ", month=" + month +
                '}';
    }
}

