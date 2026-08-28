package readability;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class ParagraphStatisticsService {
    private static final Set<Character> VOWELS = Set.of(
            'a','e','i','o','u','y',
            'A','E','I','O','U','Y'
    );

    static TextStatistics of(String paragraph) {
        return new TextStatistics(
                countSentences(paragraph),
                countWords(paragraph),
                countCharacters(paragraph),
                countSyllables(paragraph)
        );
    }

    static int countSentences(String paragraph) {
        return paragraph.trim().split("[.!?]").length;
    }

    static int countWords(String sentence) {
        return sentence.trim().split("\\s+").length;
    }

    static int countCharacters(String word) {
        int charactersCount = 0;
        for (char c : word.toCharArray())
            if (Character.isLetterOrDigit(c))
                charactersCount++;
        return charactersCount;
    }

    static SyllablePair countSyllables(String text) {
        List<String> words = getParagraphWords(text);

        int syllablesCount = 0, polySyllablesCount = 0;
        for(String word : words) {
            int wordLen = word.length();
            char prev = word.charAt(0);
            int count = isVowel(prev) ? 1 : 0;
            for(int i = 1; i < wordLen; i++) {
                char curr = word.charAt(i);
                if(isVowel(prev) && isVowel(curr))
                    continue;
                if(i == wordLen - 1 && Character.toLowerCase(curr) == 'e')
                    continue;
                if(isVowel(curr))
                    count++;
                prev = curr;
            }
            polySyllablesCount += count > 2 ? 1 : 0;
            syllablesCount += Math.max(count, 1);
        }

        return new SyllablePair(syllablesCount, polySyllablesCount);
    }

    private static List<String> getParagraphWords(String text) {
        List<String> words = new ArrayList<>();

        // alphabetical
        String[] sentences = text.split("[.!?]");
        for (String sentence : sentences) {
            String[] sentenceWords = sentence.split("[\\s,;:]+");
            for(String sentenceWord : sentenceWords)
                if(!sentenceWord.isEmpty() && !isNumber(sentenceWord))
                    words.add(sentenceWord);
        }

        // numbers
        Pattern pattern = Pattern.compile("-?\\d+(?:[,.]\\d+)*");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find())
            words.add(matcher.group());

        return words;
    }

    private static boolean isVowel(char c) {
        return VOWELS.contains(c);
    }

    private static boolean isNumber(String word) {
        return word.matches("\\d+");
    }
}
