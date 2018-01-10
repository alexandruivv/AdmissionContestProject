package utils;

public class DayCounter {
    private int day;
    private int month;
    private int count;

    public DayCounter(){}

    public DayCounter(int day, int month, int count) {
        this.day = day;
        this.month = month;
        this.count = count;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "DayCounter{" +
                "day=" + day +
                ", month=" + month +
                ", count=" + count +
                '}';
    }
}
