package readability;

class ColemanLiauIndex extends AbstractReadabilityIndex {

    ColemanLiauIndex(TextStatistics statistics) {
        super(statistics);
    }

    @Override
    public double calcScore() {
        final double sentencesCount = statistics.sentences();
        final double wordsCount = statistics.words();
        final double charactersCount = statistics.characters();
        final double l = (charactersCount / wordsCount) * 100;
        final double s = (sentencesCount / wordsCount) * 100;
        final double score = (0.0588 * l) - (0.296 * s) - 15.8;
        return Math.max(score, 0);
    }
}
