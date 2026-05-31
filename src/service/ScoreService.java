package service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ScoreService {

    private static final String SCORE_FILE = "scores.txt";
    private static final int MAX_ENTRIES = 50;

    public static class ScoreEntry implements Comparable<ScoreEntry> {
        public String name;
        public int score;
        public String date;

        public ScoreEntry(String name, int score, String date) {
            this.name = name;
            this.score = score;
            this.date = date;
        }

        @Override
        public int compareTo(ScoreEntry o) {
            return Integer.compare(o.score, this.score);
        }
    }

    public static void addScore(String name, int score) {
        List<ScoreEntry> entries = loadAll();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        entries.add(new ScoreEntry(name == null || name.isEmpty() ? "Player" : name, score, date));
        Collections.sort(entries);
        while (entries.size() > MAX_ENTRIES) {
            entries.remove(entries.size() - 1);
        }
        saveAll(entries);
    }

    public static List<ScoreEntry> getTopN(int n) {
        List<ScoreEntry> entries = loadAll();
        Collections.sort(entries);
        return entries.subList(0, Math.min(n, entries.size()));
    }

    static List<ScoreEntry> loadAll() {
        List<ScoreEntry> entries = new ArrayList<>();
        File file = new File(SCORE_FILE);
        if (!file.exists()) return entries;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 3);
                if (parts.length >= 2) {
                    entries.add(new ScoreEntry(parts[0], Integer.parseInt(parts[1]),
                            parts.length > 2 ? parts[2] : ""));
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return entries;
    }

    static void saveAll(List<ScoreEntry> entries) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SCORE_FILE))) {
            for (ScoreEntry entry : entries) {
                writer.println(entry.name + "|" + entry.score + "|" + entry.date);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
