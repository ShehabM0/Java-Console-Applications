package readability;

interface ReadabilityIndex {
    int MIN_SCORE = 1, MAX_SCORE = 14;
    int MIN_AGE = 6, MAX_AGE = 22;
    int AGE_OFFSET = 4;
    int[] AGE_GAP = {1, 4};

    double calcScore();
    static int calcAge(double score) {
        int upperScore = (int) Math.ceil(score);
        upperScore = Math.min(Math.max(upperScore, MIN_SCORE), MAX_SCORE);
        int from = upperScore + AGE_OFFSET;
        int to = from + (upperScore == MAX_SCORE ? AGE_GAP[1] : AGE_GAP[0]);
        return Math.min(Math.max(to, MIN_AGE), MAX_AGE);
    }

    double getScore();
    int getAge();
}
