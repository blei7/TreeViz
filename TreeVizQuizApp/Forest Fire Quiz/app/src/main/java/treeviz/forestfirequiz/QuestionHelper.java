package treeviz.forestfirequiz;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


class QuestionHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DB_NAME = "TQuiz.db";

    //If you want to add more questions or wanna update table values
    //or any kind of modification in db just increment version no.
    private static final int DB_VERSION = 5;
    private static final String TABLE = "TQ";
    private static final String ID = "_UID";
    private static final String QUESTION = "QUESTION";
    private static final String A = "OPTA";
    private static final String B = "OPTB";
    private static final String C = "OPTC";
    private static final String D = "OPTD";
    private static final String ANS = "ANSWER";
    //Table with columns in order of id , question , choice A, choice B , choice C , choice D , answer
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE
            + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + QUESTION + " VARCHAR(255), "
            + A + " VARCHAR(255), " + B + " VARCHAR(255), " + C + " VARCHAR(255), " + D + " VARCHAR(255), "
            + ANS + " VARCHAR(255));";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE;

    QuestionHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //OnCreate is called only once
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //OnUpgrade is called when ever we upgrade or increment our database version no
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

    void questionsList() {
        ArrayList<Question> arraylist = new ArrayList<>();

        arraylist.add(new Question("In which region did the biggest forest fire occur in 2017?",
                "Metro Vancouver",
                "Northern BC",
                "Eastern BC",
                "Interior BC",
                "Interior BC"));

        arraylist.add(new Question("What is the leading natural cause of forest fires?",
                "Lightning",
                "Drought",
                "Volcanic eruptions",
                "Flood",
                "Lighting"));

        arraylist.add(new Question("What do forest fires need in order to burn?",
                "Low humidity",
                "Drought",
                "Lightning",
                "Fuel",
                "Fuel"));

        this.addAllQuestions(arraylist);

    }


    private void addAllQuestions(ArrayList<Question> allQuestions) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (Question question : allQuestions) {
                values.put(QUESTION, question.getQuestion());
                values.put(A, question.getA());
                values.put(B, question.getB());
                values.put(C, question.getC());
                values.put(D, question.getD());
                values.put(ANS, question.getAns());
                db.insert(TABLE, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }


    List<Question> getAllQuestions() {

        List<Question> questionsList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String[] coloumn = {ID, QUESTION, A, B, C, D, ANS};
        Cursor cursor = db.query(TABLE, coloumn, null, null, null, null, null);


        while (cursor.moveToNext()) {
            Question question = new Question();
            question.setId(cursor.getInt(0));
            question.setQuestion(cursor.getString(1));
            question.setOptA(cursor.getString(2));
            question.setOptB(cursor.getString(3));
            question.setOptC(cursor.getString(4));
            question.setOptD(cursor.getString(5));
            question.setAns(cursor.getString(6));
            questionsList.add(question);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        db.close();
        return questionsList;
    }
}
