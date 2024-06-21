package org.braun.digikam.backend.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author mbraun
 */
public class PhotilsWrapper {

    Set<String> tagsUsed = new HashSet<>();
    
    public int work(File directory) {
        int cnt = 0;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(directory, "result.txt")))) {
            File[] sorted = directory.listFiles();
            Arrays.sort(sorted, new FileNameSorter());
            for (File image : sorted) {
                if (image.isDirectory() || !image.getName().endsWith(".jpg")) {
                    continue;
                }
                List<String> tags = getTags(image);
                writer.write(image.getName() + " Tags: " + String.join(", ", tags));
                writer.newLine();
                cnt++;
            }
            List<String> tags = new ArrayList<>(tagsUsed);
            Collections.sort(tags);
            writer.write("Number of tags: " + tags.size());
            writer.newLine();
            writer.write(String.join(", ", tags));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cnt;
    }

    private List<String> getTags(File image) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(
            "/opt/photils-cli-0.4.1-linux-x86_64.AppImage",
            "--image",
            image.getPath(),
            "--with_confidence"
        );
        Process process = pb.start();
        return watch(process);
    }

    private synchronized List<String> watch(Process process) {
        List<String> tags = new ArrayList<>();
        new Thread() {
            public void run() {
                try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = input.readLine()) != null) {
                        TagWeighted tw = TagWeighted.parse(line);
                        if (tw.weight >= 0.99) {
                            tags.add(tw.name);
                            tagsUsed.add(tw.name);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return tags;
    }

    public static class TagWeighted {

        String name;
        double weight;

        public static TagWeighted parse(String line) {
            String[] parts = line.split(":");
            TagWeighted tw = new TagWeighted();
            tw.name = parts[0];
            if (parts.length >= 1) {
                try {
                    tw.weight = Double.parseDouble(parts[1]);
                } catch (NumberFormatException e) {
                    tw.weight = 0d;
                }
            } else {
                tw.weight = 0d;
            }
            return tw;
        }

        @Override
        public String toString() {
            return name + " (" + weight + ")";
         }
        
    }
    
    class FileNameSorter implements Comparator<File> {

        @Override
        public int compare(File f1, File f2) {
            return f1.getName().compareTo(f2.getName());
        }
        
    }
        
    public static void main(String... args) {
        File directory = new File("/data/pictures/2015/07/16/");
        if (directory.exists() && directory.canWrite()) {
            PhotilsWrapper w = new PhotilsWrapper();
            System.out.println("Number of images in " + directory.getPath() + " analysed: " + w.work(directory));
        } else {
            System.out.println("Directory " + directory.getPath() + " does not exist or is write protected.");
        }

    }
}
