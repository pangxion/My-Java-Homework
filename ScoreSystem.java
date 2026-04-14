import java.util.Scanner;

// 【第一層：資料結構】對應 Q4
class Student {
    String name;
    int score;

    public Student(String name, int score) {
        this.name = name;
        this.score = score;
    }
}

public class ScoreSystem {

    // ==========================================
    // 【第二層：核心演算法】(老師要求的所有 Methods)
    // ==========================================

    // 對應 Q2：找陣列中的最高分
    public static int findMax(int[] arr) {
        int max = arr[0];
        for (int score : arr) {
            if (score > max) max = score;
        }
        return max;
    }

    // 對應 Q10：找陣列中的最低分
    public static int findMin(int[] arr) {
        int min = arr[0];
        for (int score : arr) {
            if (score < min) min = score;
        }
        return min;
    }

    // 對應 Q7：計算陣列總和
    public static int sum(int[] arr) {
        int total = 0;
        for (int score : arr) {
            total += score;
        }
        return total;
    }

    // 對應 Q3：為陣列中的每個元素加 5 分
    public static void addBonus(int[] scores) {
        for (int i = 0; i < scores.length; i++) {
            scores[i] += 5;
        }
    }

    // 對應 Q5：低於 60 分的學生加 10 分
    public static void curve(Student s) {
        if (s.score < 60) {
            s.score += 10;
        }
    }

    // 對應 Q9：更新特定學生的分數
    public static void updateScore(Student s, int newScore) {
        s.score = newScore;
    }

    // ==========================================
    // 【第三層：互動主程式】
    // ==========================================
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== 歡迎使用動態學生分數系統 ===");
        System.out.print("請輸入你要登記的學生人數: ");
        int numStudents = scanner.nextInt();

        // 建立兩個陣列：一個裝物件 (Q8)，一個純裝分數方便計算
        Student[] students = new Student[numStudents];
        int[] scores = new int[numStudents];

        // 讓使用者動態輸入資料
        for (int i = 0; i < numStudents; i++) {
            System.out.print("請輸入第 " + (i + 1) + " 位學生的姓名: ");
            String name = scanner.next();
            System.out.print("請輸入 " + name + " 的分數: ");
            int score = scanner.nextInt();

            students[i] = new Student(name, score);
            scores[i] = score;
        }

        System.out.println("\n--- 系統分析報告 ---");

        // 對應 Q8：使用迴圈印出所有學生的姓名與分數
        System.out.println("【所有學生名單】");
        for (Student s : students) {
            System.out.println(s.name + ": " + s.score);
        }

        // 對應 Q1 & Q7：計算總和與平均
        int totalSum = sum(scores);
        double average = (double) totalSum / scores.length;
        System.out.println("\n【成績統計】");
        System.out.println("班級總分: " + totalSum);
        System.out.println("班級平均 (Average): " + average);

        // 對應 Q2 & Q10：最高分與最低分
        System.out.println("最高分 (Max): " + findMax(scores));
        System.out.println("最低分 (Min): " + findMin(scores));

        // 對應 Q6：計算及格人數
        int passCount = 0;
        for (int s : scores) {
            if (s >= 60) passCount++;
        }
        System.out.println("及格人數 (>= 60): " + passCount);

      // 對應 Q5 & Q3：調分機制展示
        System.out.println("\n--- 執行調分機制 (Curve & Bonus) ---");
        System.out.println("規則 1：不及格者自動加 10 分 (Curve)");
        System.out.println("規則 2：全班成績加 5 分 (Bonus)");
        
        // 步驟一：先執行不及格加分 (操作物件)
        for (int i = 0; i < students.length; i++) {
            curve(students[i]); // 20分變成 30分
            // 【重要修正】立刻將物件的新分數，同步回純數字陣列中
            scores[i] = students[i].score; // 陣列也更新為 30分
        }

        // 步驟二：執行全體加 5 分 (操作陣列)
        addBonus(scores); // 陣列的 30分變成 35分

        // 步驟三：將陣列的最終結果，更新回物件中 (操作物件)
        for (int i = 0; i < students.length; i++) {
            updateScore(students[i], scores[i]); // 將 35分寫回物件裡
        }

        System.out.println("【調分後最終名單】");
        for (Student s : students) {
            System.out.println(s.name + ": " + s.score);
        }

        scanner.close();
        System.out.println("\n系統執行完畢！這 10 題的考點皆已完美整合。");
    }
}