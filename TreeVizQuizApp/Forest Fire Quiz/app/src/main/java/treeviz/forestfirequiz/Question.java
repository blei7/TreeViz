package treeviz.forestfirequiz;

import android.app.Activity;

public class Question extends Activity {
    private int id;
    private String question;
    private String sA;
    private String sB;
    private String sC;
    private String sD;
    private String ans;

    public Question(String q, String a, String b, String c, String d, String ans) {

        question = q;
        sA = a;
        sB = b;
        sC = c;
        sD = d;
        this.ans = ans;
    }

    public Question() {
        id = 0;
        question = "";
        sA = "";
        sB = "";
        sC = "";
        sD = "";
        ans = "";
    }

    public String getQuestion() {
        return question;
    }

    public String getA() {
        return sA;
    }

    public String getB() {
        return sB;
    }

    public String getC() {
        return sC;
    }

    public String getD() {
        return sD;
    }

    public String getAns() {
        return ans;
    }

    public void setId(int i) {
        id = i;
    }

    public void setQuestion(String q1) {
        question = q1;
    }

    public void setOptA(String o1) {
        sA = o1;
    }

    public void setOptB(String o2) {
        sB = o2;
    }

    public void setOptC(String o3) {
        sC = o3;
    }

    public void setOptD(String o4) {
        sD = o4;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }


}
