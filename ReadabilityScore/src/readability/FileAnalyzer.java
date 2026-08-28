package readability;

import java.io.FileNotFoundException;
import java.io.File;
import java.util.*;

class FileAnalyzer {
    private final List<String> paragraphs;
    private final File file;
    private Set<AbstractReadabilityIndex> readabilityIndices;
    private TextStatistics fileStatistics;
    private int paragraphsCount;

    FileAnalyzer(File file) {
        this.file = file;

        fileStatistics = new TextStatistics(0, 0, 0, new SyllablePair(0, 0));
        paragraphs = readFile();
        paragraphsCount = 0;
    }

    void analyze() {
        if(paragraphs.isEmpty()) {
            System.out.println("Couldn't analyze empty file!");
            return;
        }
        analyzeParagraphs();
        analyzeFile();
    }

    private void analyzeParagraphs() {
        for(String paragraph : paragraphs) {
            System.out.printf("----------- Analyzed paragraph #%d -----------%n", ++paragraphsCount);
            System.out.printf("Paragraph is:%n%s%n", paragraph);
            TextStatistics paragraphStatistics = ParagraphStatisticsService.of(paragraph);

            readabilityIndices = createReadabilityIndices(paragraphStatistics);
            double paragraphReadabilityAge = calcReadabilityAvgAge();

            System.out.printf(
                    "Sentences: %d%nWords: %d%nCharacters: %d%nSyllables: %d%nPolysyllables: %d%n",
                    paragraphStatistics.sentences(),
                    paragraphStatistics.words(),
                    paragraphStatistics.characters(),
                    paragraphStatistics.syllablePair().syllables(),
                    paragraphStatistics.syllablePair().polysyllables()
            );
            displayReadabilityIndices();
            displayReadabilityAvgAge(paragraphReadabilityAge, false);

            accumulateFileParagraphsStatistics(paragraphStatistics);
        }
    }

    private void analyzeFile() {
        System.out.println("----------- Analyzed file -----------");
        System.out.printf(
                "Paragraphs: %d%nSentences: %d%nWords: %d%nCharacters: %d%nSyllables: %d%nPolysyllables: %d%n",
                paragraphsCount,
                fileStatistics.sentences(),
                fileStatistics.words(),
                fileStatistics.characters(),
                fileStatistics.syllablePair().syllables(),
                fileStatistics.syllablePair().polysyllables()
        );

        readabilityIndices = createReadabilityIndices(fileStatistics);
        double fileReadabilityAvgAge = calcReadabilityAvgAge();
        displayReadabilityIndices();

        displayReadabilityAvgAge(fileReadabilityAvgAge, true);
    }

    private List<String> readFile() {
        if (file == null)
            return List.of();

        List<String> paragraphs = new ArrayList<>();
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNext()) {
                String text = sc.nextLine().trim();
                if(!text.isEmpty())
                    paragraphs.add(text);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
        return paragraphs;
    }

    private Set<AbstractReadabilityIndex> createReadabilityIndices(TextStatistics textStatistics) {
        return new HashSet<>(Set.of(
                new AutomatedReadabilityIndex(textStatistics),
                new FleschKincaidIndex(textStatistics),
                new SmogIndex(textStatistics),
                new ColemanLiauIndex(textStatistics)
        ));
    }

    private double calcReadabilityAvgAge() {
        int ageSum = 0;
        for(ReadabilityIndex readabilityIndex : readabilityIndices) {
            double readabilityIndexScore = readabilityIndex.getScore();
            ageSum += ReadabilityIndex.calcAge(readabilityIndexScore);
        }
        return (double) ageSum / readabilityIndices.size();
    }

    private void accumulateFileParagraphsStatistics(TextStatistics paragraphStatistics) {
        fileStatistics = new TextStatistics(
                fileStatistics.words() + paragraphStatistics.words(),
                fileStatistics.sentences() + paragraphStatistics.sentences(),
                fileStatistics.characters() + paragraphStatistics.characters(),
                new SyllablePair(
                        fileStatistics.syllablePair().syllables() + paragraphStatistics.syllablePair().syllables(),
                    fileStatistics.syllablePair().polysyllables() + paragraphStatistics.syllablePair().polysyllables()
                )
        );
    }

    private void displayReadabilityIndices() {
        System.out.println();
        for (ReadabilityIndex readabilityIndex : readabilityIndices) {
            double score = readabilityIndex.getScore();
            int age = readabilityIndex.getAge();
            String displayText = switch (readabilityIndex) {
                case AutomatedReadabilityIndex _ -> "Automated Readability Index";
                case FleschKincaidIndex _ -> "Flesch–Kincaid readability tests";
                case SmogIndex _ -> "Simple Measure of Gobbledygook";
                case ColemanLiauIndex _ -> "Coleman–Liau index";
                default -> "Unexpected value";
            };
            System.out.printf("%s: %.2f (about %d-year-olds).%n", displayText, Math.floor(score * 100) / 100, age);
        }
    }

    private void displayReadabilityAvgAge(double readabilityAvgAge, boolean isFile) {
        System.out.printf(
                "%nThis %s should be understood in average by %.2f-year-olds.%n",
                isFile ? "file" : "paragraph",
                Math.floor(readabilityAvgAge * 100) / 100
        );
    }
}
