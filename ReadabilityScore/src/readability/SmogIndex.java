package readability;

class SmogIndex extends AbstractReadabilityIndex {

    SmogIndex(TextStatistics statistics) {
        super(statistics);
    }

    @Override
    public double calcScore() {
        final double sentencesCount = statistics.sentences();
        final double textPolySyllables = statistics.syllablePair().polysyllables();
        final double score = (1.043 * Math.sqrt(textPolySyllables * (30 / sentencesCount))) + 3.1291;
        return Math.max(score, 0);
    }
}
