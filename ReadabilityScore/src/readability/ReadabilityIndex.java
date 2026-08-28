package readability;

interface ReadabilityIndex {
    int MAX_SCORE = 14;
    int MAX_AGE = 22;

    double calcScore();
    static int calcAge(double score) {
        int upperScore = (int) Math.ceil(score);
        int from = upperScore + 4;
        int to = from + (upperScore >= MAX_SCORE ? 4 : 1);
        return Math.min(to, MAX_AGE);
    }

    double getScore();
    int getAge();
}
