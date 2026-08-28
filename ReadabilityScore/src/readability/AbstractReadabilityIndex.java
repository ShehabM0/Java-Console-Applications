package readability;

abstract class AbstractReadabilityIndex implements ReadabilityIndex {
    final TextStatistics statistics;
    private final double score;
    private final int age;

    AbstractReadabilityIndex(TextStatistics statistics) {
        this.statistics = statistics;
        score = calcScore();
        age = ReadabilityIndex.calcAge(score);
    }

    @Override
    public double getScore() {
        return score;
    }

    @Override
    public int getAge() {
        return age;
    }
}
