import java.util.Scanner;

// --- 步驟 1：建立共用的資料載體 (Data Context) ---
class ProbabilityData {
    double n_S;   // 樣本空間總數 n(S)
    double n_A;   // 事件 A 發生的次數 n(A)
    double p_A;   // P(A)
    double p_B;   // P(B)
    double p_A_and_B; // P(A ∩ B) 交集
    double p_B_given_A; // P(B|A)
    double p_J;   // 學校例子：建中人數 J
    double p_G;   // 學校例子：北一女人數 B (Girl)

    // 建構子：接收使用者一次性輸入的所有數值
    public ProbabilityData(double n_S, double n_A, double p_A, double p_B, 
                           double p_A_and_B, double p_B_given_A, double p_J, double p_G) {
        this.n_S = n_S;
        this.n_A = n_A;
        this.p_A = p_A;
        this.p_B = p_B;
        this.p_A_and_B = p_A_and_B;
        this.p_B_given_A = p_B_given_A;
        this.p_J = p_J;
        this.p_G = p_G;
    }
}

// --- 步驟 2：統一介面，改為接收資料載體 ---
interface ProbabilityPage {
    void process(ProbabilityData data);
}

// --- 步驟 3：13 個獨立的 Class，對應講義內容 ---

class Page2_Definition implements ProbabilityPage {
    public void process(ProbabilityData data) {
        System.out.println("\n[第2頁] 機率的定義：P(A) = n(A) / n(S)");
        System.out.printf("👉 運算結果：P(A) = %.0f / %.0f = %.2f\n", data.n_A, data.n_S, (data.n_A / data.n_S));
    }
}

class Page3_SampleSpace implements ProbabilityPage {
    public void process(ProbabilityData data) {
        System.out.println("\n[第3頁] 樣本空間 (Sample Space)：所有可能結果的集合。");
        System.out.printf("👉 系統資訊：當前設定的樣本空間總數 S 為 %.0f\n", data.n_S);
    }
}

class Page4_Event implements ProbabilityPage {
    public void process(ProbabilityData data) {
        System.out.println("\n[第4頁] 事件 (Event)：我們關心的結果。");
        System.out.printf("👉 系統資訊：當前關心的事件 A 發生次數為 %.0f\n", data.n_A);
    }
}

class Page5_BasicFormula implements ProbabilityPage {
    public void process(ProbabilityData data) {
        System.out.println("\n[第5頁] 基本公式：我要的 / 全部。");
        System.out.printf("👉 運算結果：(我要的 %.0f) / (全部 %.0f) = %.2f\n", data.n_A, data.n_S, (data.n_A / data.n_S));
    }
}

class Page6_Complement implements ProbabilityPage {
    public void process(ProbabilityData data) {
        System.out.println("\n[第6頁] 補事件：P(A^c) = 1 - P(A)");
        System.out.printf("👉 運算結果：1 - %.2f = %.2f\n", data.p_A, (1 - data.p_A));
    }
}

class Page7_Union implements ProbabilityPage {
    public void process(ProbabilityData data) {
        System.out.println("\n[第7頁] 聯集 (OR)：P(A∪B) = P(A) + P(B) - P(A∩B)");
        double union = data.p_A + data.p_B - data.p_A_and_B;
        System.out.printf("👉 運算結果：%.2f + %.2f - %.2f = %.2f\n", data.p_A, data.p_B, data.p_A_and_B, union);
    }
}

class Page8_Intersection implements ProbabilityPage {
    public void process(ProbabilityData data) {
        System.out.println("\n[第8頁] 交集 (AND)：P(A∩B) = P(A) * P(B|A)");
        double intersection = data.p_A * data.p_B_given_A;
        System.out.printf("👉 運算結果：%.2f * %.2f = %.2f\n", data.p_A, data.p_B_given_A, intersection);
    }
}

class Page9_ConditionalProb implements ProbabilityPage {
    public void process(ProbabilityData data) {
        System.out.println("\n[第9頁] 條件機率：P(A|B) = P(A∩B) / P(B)");
        double p_A_given_B = data.p_A_and_B / data.p_B;
        System.out.printf("👉 運算結果：%.2f / %.2f = %.2f\n", data.p_A_and_B, data.p_B, p_A_given_B);
    }
}

class Page10_Independence implements ProbabilityPage {
    public void process(ProbabilityData data) {
        System.out.println("\n[第10頁] 獨立事件：若獨立，P(A∩B) = P(A) * P(B)");
        double expectedIntersection = data.p_A * data.p_B;
        System.out.printf("👉 系統分析：若 A, B 獨立，交集應為 %.2f * %.2f = %.2f\n", data.p_A, data.p_B, expectedIntersection);
        System.out.printf("   (您輸入的實際交集為 %.2f，兩者若不相等則不獨立)\n", data.p_A_and_B);
    }
}

class Page11_BayesTheorem implements ProbabilityPage {
    public void process(ProbabilityData data) {
        System.out.println("\n[第11頁] 貝氏定理：P(A|B) = P(B|A)P(A) / P(B)");
        double bayes = (data.p_B_given_A * data.p_A) / data.p_B;
        System.out.printf("👉 運算結果：(%.2f * %.2f) / %.2f = %.2f\n", data.p_B_given_A, data.p_A, data.p_B, bayes);
    }
}

class Page12_TotalProb implements ProbabilityPage {
    public void process(ProbabilityData data) {
        System.out.println("\n[第12頁] 全機率公式：將所有可能原因導致結果的機率加總。");
        System.out.println("👉 系統資訊：這通常是計算貝氏定理分母 P(B) 的過程。");
    }
}

class Page13_ExampleBase implements ProbabilityPage {
    public void process(ProbabilityData data) {
        System.out.println("\n[第13頁] 學校例子：P(建中) = J/N");
        System.out.printf("👉 運算結果：建中人數 %.0f / 總人數 %.0f = %.2f\n", data.p_J, data.n_S, (data.p_J / data.n_S));
    }
}

class Page14_ExampleUnion implements ProbabilityPage {
    public void process(ProbabilityData data) {
        System.out.println("\n[第14頁] 例子-建中或北一女：P(建中∪北一女) = P(J) + P(B)");
        double pJ = data.p_J / data.n_S;
        double pG = data.p_G / data.n_S;
        System.out.printf("👉 運算結果：%.2f + %.2f = %.2f\n", pJ, pG, (pJ + pG));
    }
}

// --- 步驟 4：主程式 (一次輸入，批次產出) ---

public class BatchProbabilitySystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== 批次機率講義運算系統 ===");
        System.out.println("請一次性輸入情境數值，系統將自動套用至 13 頁講義中：\n");

        System.out.print("1. 樣本空間總數 n(S) (例如: 100): "); double n_S = sc.nextDouble();
        System.out.print("2. 事件 A 發生次數 n(A) (例如: 20): "); double n_A = sc.nextDouble();
        System.out.print("3. P(A) (0~1): "); double p_A = sc.nextDouble();
        System.out.print("4. P(B) (0~1): "); double p_B = sc.nextDouble();
        System.out.print("5. 交集 P(A∩B) (0~1): "); double p_A_and_B = sc.nextDouble();
        System.out.print("6. 條件機率 P(B|A) (0~1): "); double p_B_given_A = sc.nextDouble();
        System.out.print("7. 學校例子：建中人數: "); double p_J = sc.nextDouble();
        System.out.print("8. 學校例子：北一女人數: "); double p_G = sc.nextDouble();

        // 將輸入的資料封裝進資料載體
        ProbabilityData dataContext = new ProbabilityData(n_S, n_A, p_A, p_B, p_A_and_B, p_B_given_A, p_J, p_G);

        // 將 13 個物件放入陣列
        ProbabilityPage[] pages = {
            new Page2_Definition(), new Page3_SampleSpace(), new Page4_Event(),
            new Page5_BasicFormula(), new Page6_Complement(), new Page7_Union(),
            new Page8_Intersection(), new Page9_ConditionalProb(), new Page10_Independence(),
            new Page11_BayesTheorem(), new Page12_TotalProb(), new Page13_ExampleBase(),
            new Page14_ExampleUnion()
        };

        System.out.println("\n================= 講義運算結果總覽 =================");
        
        // 批次執行！一個迴圈就搞定 13 個 Class
        for (ProbabilityPage page : pages) {
            page.process(dataContext);
        }
        
        System.out.println("\n====================================================");
        System.out.println("作業完成：已成功將一組數值餵給 13 個獨立類別並產出結果。");
        sc.close();
    }
}
