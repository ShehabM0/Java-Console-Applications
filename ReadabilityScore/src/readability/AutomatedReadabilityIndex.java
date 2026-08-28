package readability;

class AutomatedReadabilityIndex extends AbstractReadabilityIndex {

    AutomatedReadabilityIndex(TextStatistics statistics) {
        super(statistics);
    }

    @Override
    public double calcScore() {
        final double sentencesCount = statistics.sentences();
        final double wordsCount = statistics.words();
        final double charactersCount = statistics.characters();
        final double score = (4.71 * (charactersCount / wordsCount)) + (0.5 * (wordsCount / sentencesCount)) - 21.43;
        return Math.max(score, 0);
    }
}
