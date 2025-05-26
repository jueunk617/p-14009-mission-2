package com.back;

import java.io.*;
import java.util.*;

public class WiseSayingRepository {
    private final String dirPath = "db/wiseSaying";
    private final String lastIdPath = dirPath + "/lastId.txt";

    public WiseSayingRepository() {
        new File(dirPath).mkdirs();
    }

    public void save(WiseSaying ws) { // {id}.json 형태로 파일 저장
        File file = new File(dirPath + "/" + ws.id + ".json");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(ws.toJson());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteById(int id) {
        File file = new File(dirPath + "/" + id + ".json");
        if (file.exists()) {
            file.delete();
        }
    }

    public void saveLastId(int lastId) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(lastIdPath))) {
            writer.write(String.valueOf(lastId));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int loadLastId() {
        File file = new File(lastIdPath);
        if (!file.exists()) return 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return Integer.parseInt(reader.readLine().trim());
        } catch (IOException e) {
            return 0;
        }
    }

    public List<WiseSaying> loadAll() {
        List<WiseSaying> list = new ArrayList<>();
        File[] files = new File(dirPath).listFiles((dir, name) -> name.endsWith(".json"));

        if (files != null) {
            for (File file : files) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    list.add(WiseSaying.fromJson(sb.toString()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        list.sort(Comparator.comparingInt(ws -> ws.id));
        return list;
    }

    public void buildDataJson(List<WiseSaying> list) { // data.json 파일로 전체 명언 목록 저장
        File file = new File("data.json");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("[\n");
            for (int i = 0; i < list.size(); i++) {
                writer.write(list.get(i).toJson());
                if (i < list.size() - 1) {
                    writer.write(",\n");
                }
            }
            writer.write("\n]");
            System.out.println("data.json 파일의 내용이 갱신되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}