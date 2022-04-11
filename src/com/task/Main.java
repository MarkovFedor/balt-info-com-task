package com.task;

import java.io.*;
import java.util.*;

public class Main {
    public static String INPUT_FILE_NAME = "D:/lng-big.csv";
    public static String OUTPUT_FILE_NAME = "D:/output.txt";
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        ////////////////////////////////////////////////
	    File file = new File(INPUT_FILE_NAME);
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = null;
        try {
            line = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ////////////////////////////////////////////////
        HashMap<String, Integer> partGroupNumber = new HashMap<String, Integer>();
        HashMap<String, Integer> stringGroupNumber = new HashMap<String, Integer>();
        String[] parts = line.split(";");
        int groupId = 1;
        for(String part: parts) {
            if(part.isEmpty()) continue;
            stringGroupNumber.put(line, groupId);
            partGroupNumber.put(part, groupId);
        }
        groupId++;
        /////////////////////////////////////////////////
        int counter = 0;
        int currentGroup = groupId;
        ////////////////////////////////////////////////
        while(true) {
            ///////////////////////////////////////////
            counter++;
            ///////////////////////////////////////////
            try {
                line = bufferedReader.readLine();
                if(line == null) {
                    break;
                }
                parts = line.split(";");
                boolean status = false;
                for(String part: parts) {
                    if(part.trim().length() == 0 || part.length() == 2) {
                        continue;
                    }
                    if(partGroupNumber.containsKey(part)) {
                        groupId = partGroupNumber.get(part);
                        for(String currentPart: parts) {
                            if(part.trim().length() == 0 || part.length() == 2) continue;
                            if(!partGroupNumber.containsKey(currentPart)) {
                                partGroupNumber.put(currentPart, groupId);
                            }
                        }
                        stringGroupNumber.put(line, groupId);
                        status = true;
                    }
                }
                if(status) {
                    continue;
                }
                for(String part: parts) {
                    if(part.trim().length() == 0 || part.length() == 2) continue;
                    partGroupNumber.put(part, currentGroup);
                }
                stringGroupNumber.put(line, currentGroup);
                currentGroup++;
            } catch (IOException e) {
                break;
            }
        }
        HashMap<Integer, ArrayList<String>> resultHashMap = new HashMap<>();
        for(Map.Entry<String, Integer> entry: stringGroupNumber.entrySet()) {
            int group = entry.getValue();
            if(!resultHashMap.containsKey(group)) {
                ArrayList<String> list = new ArrayList<>();
                list.add(entry.getKey());
                resultHashMap.put(group, list);
            } else {
                resultHashMap.get(group).add(entry.getKey());
            }
        }

        List<List<String>> sortedKeys = new ArrayList(resultHashMap.values());
        sortedKeys.sort(Collections.reverseOrder(Comparator.comparingInt(List::size)));
        try(FileWriter writer = new FileWriter(OUTPUT_FILE_NAME, false)) {
            long count = sortedKeys.stream().filter(i -> i.size() > 1).count();
            writer.write("Групп с более чем 1 элементом: " + String.valueOf(count));
            writer.write("\n");
            for(int i = 0; i < sortedKeys.size();i++) {
                writer.write("Группа: " + (i+1));
                writer.write("\n");
                for(String string: sortedKeys.get(i)) {
                    writer.write(string);
                    writer.write("\n");
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime-startTime) + "ms");
    }
}
