package readability;

class FleschKincaidIndex extends AbstractReadabilityIndex {

    FleschKincaidIndex(TextStatistics statistics) {
        super(statistics);
    }

    @Override
    public double calcScore() {
        final double sentencesCount = statistics.sentences();
        final double wordsCount = statistics.words();
        final double textSyllables = statistics.syllablePair().syllables();
        final double score = (0.39 * (wordsCount / sentencesCount)) + (11.8 * (textSyllables / wordsCount)) - 15.59;
        return Math.max(score, 0);
    }
}
