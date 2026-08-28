package readability;

import java.io.FileNotFoundException;
import java.io.File;
import java.util.*;

class FileAnalyzer {
    private final List<String> paragraphs;
    private final Scanner sc;
    private final File file;
    private ReadabilityIndex automatedReadabilityIndex, colemanLiauIndex, fleschKincaidIndex, smogIndex;
    private TextStatistics fileStatistics;
    private int paragraphsCount;

    FileAnalyzer(File file, Scanner sc) {
        this.file = file;
        this.sc = sc;

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

            automatedReadabilityIndex = new AutomatedReadabilityIndex(paragraphStatistics);
            fleschKincaidIndex = new FleschKincaidIndex(paragraphStatistics);
            smogIndex = new SmogIndex(paragraphStatistics);
            colemanLiauIndex = new ColemanLiauIndex(paragraphStatistics);
            double paragraphReadabilityAge = calcReadabilityAvgAge();

            System.out.printf(
                    "Sentences: %d%nWords: %d%nCharacters: %d%nSyllables: %d%nPolysyllables: %d%n",
                    paragraphStatistics.sentences(),
                    paragraphStatistics.words(),
                    paragraphStatistics.characters(),
                    paragraphStatistics.syllablePair().syllables(),
                    paragraphStatistics.syllablePair().polysyllables()
            );
            menu();
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

        automatedReadabilityIndex = new AutomatedReadabilityIndex(fileStatistics);
        fleschKincaidIndex = new FleschKincaidIndex(fileStatistics);
        smogIndex = new SmogIndex(fileStatistics);
        colemanLiauIndex = new ColemanLiauIndex(fileStatistics);
        double fileReadabilityAvgAge = calcReadabilityAvgAge();
        displayReadabilityIndices(new ReadabilityIndex[] {
                automatedReadabilityIndex,
                fleschKincaidIndex,
                smogIndex,
                colemanLiauIndex
        });

        displayReadabilityAvgAge(fileReadabilityAvgAge, true);
    }

    void menu() {
        String action;
        while (true) {
            displayActions();
            action = sc.next();
            try {
                ReadabilityIndex[] readabilityIndices = switch (Action.valueOf(action.toUpperCase())) {
                    case ARI -> new ReadabilityIndex[] { automatedReadabilityIndex };
                    case FK -> new ReadabilityIndex[] { fleschKincaidIndex };
                    case SMOG -> new ReadabilityIndex[] { smogIndex };
                    case CL -> new ReadabilityIndex[] { colemanLiauIndex };
                    case ALL -> new ReadabilityIndex[] {
                            automatedReadabilityIndex,
                            fleschKincaidIndex,
                            smogIndex,
                            colemanLiauIndex
                    };
                };
                displayReadabilityIndices(readabilityIndices);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Unknown action: " + action);
            }
        }
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

    private double calcReadabilityAvgAge() {
        ReadabilityIndex[] readabilityIndices = {
                automatedReadabilityIndex,
                colemanLiauIndex,
                fleschKincaidIndex,
                smogIndex
        };

        int ageSum = 0;
        for(ReadabilityIndex readabilityIndex : readabilityIndices) {
            double readabilityIndexScore = readabilityIndex.getScore();
            ageSum += ReadabilityIndex.calcAge(readabilityIndexScore);
        }
        return (double) ageSum / readabilityIndices.length;
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

    private void displayActions() {
        System.out.printf("%nEnter the score you want to calculate (");
        Action[] actions = Action.values();
        int actionsLen = actions.length;
        for (int i = 0; i < actionsLen; i++)
            System.out.printf(
                    "%s%s",
                    actions[i],
                    i == actionsLen - 1 ? "" : ", "
            );
        System.out.print("): ");
    }

    private void displayReadabilityIndices(ReadabilityIndex[] readabilityIndices) {
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
