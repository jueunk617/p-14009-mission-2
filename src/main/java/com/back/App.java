package com.back;

import java.io.File;
import java.util.*;

public class App {
    Scanner scanner = new Scanner(System.in);
    List<WiseSaying> wiseSayings;
    int lastId;
    WiseSayingRepository wiseSayingRepository = new WiseSayingRepository();

    void run() {
        System.out.println("== 명언 앱 ==");

        wiseSayings = wiseSayingRepository.loadAll(); // 저장된 명언 파일 불러오기
        lastId = wiseSayingRepository.loadLastId(); // 마지막 명언 변호

        while (true) {
            System.out.print("명령) ");
            String cmd = scanner.nextLine().trim();

            if (cmd.equals("종료")) break;
            else if (cmd.equals("등록")) actionWrite();
            else if (cmd.equals("목록")) actionList();
            else if (cmd.startsWith("삭제?id=")) actionDelete(cmd);
            else if (cmd.startsWith("수정?id=")) actionModify(cmd);
            else if (cmd.equals("빌드")) actionBuild();
            // else if (cmd.equals("초기화")) actionReset(); 테스트용 초기화 기능
        }
    }

    void actionWrite() {
        System.out.print("명언 : ");
        String content = scanner.nextLine().trim();
        System.out.print("작가 : ");
        String author = scanner.nextLine().trim();

        int id = ++lastId;
        WiseSaying ws = new WiseSaying(id, content, author);
        wiseSayings.add(ws);

        wiseSayingRepository.save(ws);
        wiseSayingRepository.saveLastId(lastId);

        System.out.println(id + "번 명언이 등록되었습니다.");
    }

    void actionList() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");
        for (int i = wiseSayings.size() - 1; i >= 0; i--) {
            WiseSaying ws = wiseSayings.get(i);
            System.out.println(ws.id + " / " + ws.author + " / " + ws.content);
        }
    }

    void actionDelete(String cmd) {
        int id = Integer.parseInt(cmd.substring("삭제?id=".length()));
        boolean removed = wiseSayings.removeIf(ws -> ws.id == id);
        if (removed) {
            wiseSayingRepository.deleteById(id);
            System.out.println(id + "번 명언이 삭제되었습니다.");
        } else {
            System.out.println(id + "번 명언은 존재하지 않습니다.");
        }
    }

    void actionModify(String cmd) {
        int id = Integer.parseInt(cmd.substring("수정?id=".length()));
        for (WiseSaying ws : wiseSayings) {
            if (ws.id == id) {
                System.out.println("명언(기존) : " + ws.content);
                System.out.print("명언 : ");
                ws.content = scanner.nextLine().trim();

                System.out.println("작가(기존) : " + ws.author);
                System.out.print("작가 : ");
                ws.author = scanner.nextLine().trim();

                wiseSayingRepository.save(ws);
                return;
            }
        }
        System.out.println(id + "번 명언은 존재하지 않습니다.");
    }

    void actionBuild() {
        wiseSayingRepository.buildDataJson(wiseSayings);
    }

    /*
    void actionReset() {
        File dir = new File("db/wiseSaying");

        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
        }

        wiseSayings.clear();
        lastId = 0;

        System.out.println("데이터가 초기화되었습니다.");
    }

     */
}