package com.hedspi.hoangviet.eslrecom.managers;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hedspi.hoangviet.eslrecom.DataDownloadListener;
import com.hedspi.hoangviet.eslrecom.R;
import com.hedspi.hoangviet.eslrecom.commons.Common;
import com.hedspi.hoangviet.eslrecom.commons.Preference;
import com.hedspi.hoangviet.eslrecom.fragments.PreferenceInquiryFragment;
import com.hedspi.hoangviet.eslrecom.models.ChildTag;
import com.hedspi.hoangviet.eslrecom.models.Material;
import com.hedspi.hoangviet.eslrecom.models.Question;
import com.hedspi.hoangviet.eslrecom.models.Tag;
import com.hedspi.hoangviet.eslrecom.models.UserProfile;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by hoangviet on 1/8/16.
 */
    public class DatabaseManager {
    private static DatabaseManager mInstance = null;
    private Context mContext;
//    private DatabaseHelper dbHelper;

    private static DatabaseReference database = null;
    private static long materialCount;
    private static long bookProfilesCount;
    private static Preference preference;
    private static List<Tag> tagList = new ArrayList<>();
    private static List<Question> questionList = new ArrayList<>();
    private static List<Material> materialList = new ArrayList<>();
    private static List<String> tagStringList = new ArrayList<>();
    private static UserProfile userProfile;

    DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private static final String DB_NAME = "esldatabase";
    private static final String DB_FILE = "data.db";
    private static final int DB_VERSION = 1;

    private DatabaseManager(Context context){
        mContext = context;
//        dbHelper = new DatabaseHelper(mContext);
    }

    public static DatabaseReference getDatabase() {
        if(database == null){
            database = FirebaseDatabase.getInstance().getReference();
        }
        return database;
    }

    public static List<Tag> getTagList() {
        return tagList;
    }

    public static List<String> getTagStringList() {
        return tagStringList;
    }

    public static void setTagList(List<Tag> tagList) {
        DatabaseManager.tagList = tagList;
    }

    public static long getMaterialCount() {
        return materialCount;
    }

    public static void setMaterialCount(long materialCount) {
        DatabaseManager.materialCount = materialCount;
    }

    public static Preference getPreference() {
        return preference;
    }

    public static void setPreference(Preference preference) {
        DatabaseManager.preference = preference;
    }

    public static long getBookProfilesCount() {
        return bookProfilesCount;
    }

    public static void setBookProfilesCount(long bookProfilesCount) {
        DatabaseManager.bookProfilesCount = bookProfilesCount;
    }

    public static List<Tag> getTagListFromServer(final DataDownloadListener listener){
        if (tagList == null || tagList.size() == 0) {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            database.child(Common.TAG).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        Map<String, Object> datas = (Map<String, Object>) dataSnapshot.getValue();
                        for (Map.Entry<String, Object> entry : datas.entrySet()) {
                            //Get user map
                            Map singleData = (Map) entry.getValue();
                            Tag tag = new Tag();
                            tag.setName((String) singleData.get("name"));
                            tag.setScore((long) singleData.get("score"));
                            List<ChildTag> listChilds = new ArrayList<ChildTag>();
                            for (Map relevantTag : (ArrayList<Map>) singleData.get("relevantTag")) {
                                ChildTag childTag = new ChildTag();
                                childTag.setName((String) relevantTag.get("name"));
                                childTag.setScore((long) relevantTag.get("score"));
                                childTag.setRealScore((long) relevantTag.get("realScore"));
                                listChilds.add(childTag);
                            }
                            tag.setRelevantTag(listChilds);
                            tagList.add(tag);
                            tagStringList.add(tag.getName());
                        }


                        Collections.sort(tagList, new Comparator<Tag>() {
                            @Override
                            public int compare(Tag o1, Tag o2) {
                                return Double.compare(o2.getScore(), o1.getScore());
                            }
                        });
                        listener.onDataDownloaded(Common.SUCCESS);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    listener.onDataDownloaded(Common.FAIL);
                }
            });
        }else {
            listener.onDataDownloaded(Common.SUCCESS);
        }

        return tagList;
    }

    public static void setUserProfile(UserProfile userProfile) {
        DatabaseManager.userProfile = userProfile;
    }

    public static UserProfile getUserProfile(){
        return userProfile;
    }

    public static List<Question> getQuestionListFromServer(final DataDownloadListener listener){
        if (questionList == null || questionList.size() == 0) {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            database.child(Common.QUESTION).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        Map<String, Object> datas = (Map<String, Object>) dataSnapshot.getValue();
                        for (Map.Entry<String, Object> entry : datas.entrySet()) {
                            //Get user map
                            Map singleData = (Map) entry.getValue();
                            Question question = new Question();
                            question.setQuestion((String) singleData.get("question"));
                            question.setDifficulty((String) singleData.get("difficulty"));
                            question.setType((String) singleData.get("type"));
                            question.setTrueAnswer((String) singleData.get("trueAnswer"));
                            question.setWrongAnswersStr((String) singleData.get("wrongAnswersStr"));
                            questionList.add(question);
                        }

                        listener.onDataDownloaded(Common.SUCCESS);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    listener.onDataDownloaded(Common.FAIL);
                }
            });
        }else {
            listener.onDataDownloaded(Common.SUCCESS);
        }

        return questionList;
    }

    public static List<Material> getMaterialListFromServer(final DataDownloadListener listener){
        if (materialList == null || materialList.size() == 0) {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            database.child(Common.MATERIAL).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {

                        Map<String, Object> datas = (Map<String, Object>) dataSnapshot.getValue();
                        for (Map.Entry<String, Object> entry : datas.entrySet()) {
                            //Get user map
                            Map singleData = (Map) entry.getValue();
                            Material material = new Material();
                            material.setName((String) singleData.get("name"));
                            material.setAbstractString((String) singleData.get("abstractString"));
                            material.setAuthor((String) singleData.get("author"));
                            material.setContent((String) singleData.get("content"));
                            material.setCoverLink((String) singleData.get("coverLink"));
                            material.setDescription((String) singleData.get("description"));
                            material.setDocumentType((String) singleData.get("documentType"));
                            material.setEdition_format((String) singleData.get("edition_format"));
                            material.setGenre_form((String) singleData.get("genre_form"));
                            material.setNote((String) singleData.get("note"));
                            material.setOnlineLink((String) singleData.get("onlineLink"));
                            material.setOnlineName((String) singleData.get("onlineName"));
                            material.setPublisher((String) singleData.get("publisher"));
                            material.setSellerLink((String) singleData.get("sellerLink"));
                            material.setSellerName((String) singleData.get("sellerName"));
                            material.setSellerPrice((String) singleData.get("sellerPrice"));
                            material.setSubjects((String) singleData.get("subjects"));
                            material.setSummary((String) singleData.get("summary"));
                            material.setTag((String) singleData.get("tag"));

                            materialList.add(material);
                        }

                        listener.onDataDownloaded(Common.SUCCESS);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    listener.onDataDownloaded(Common.SUCCESS);
                }
            });

        }else {
            listener.onDataDownloaded(Common.SUCCESS);
        }

        return materialList;
    }

    public static DatabaseManager getInstance(Context context){
        if(mInstance == null){
            mInstance = new DatabaseManager(context);
        }
        return mInstance;
    }

//    public ArrayList<Material> getListLevelTest(){
//        ArrayList<Material> list = new ArrayList<Material>();
//        try {
//            list = (ArrayList) dbHelper.getMaterialDao().queryForAll();
//        }catch (SQLException e){
//            e.printStackTrace();
//        }
//        return list;
//    }

    public String readJSONFromAsset(String jsonName) {
        String json = null;
        try {
            InputStream is = mContext.getAssets().open(jsonName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


//    //***************** HELPER ***********************//
//
//    class DatabaseHelper extends OrmLiteSqliteOpenHelper{
//        private Dao<Material, Integer> materialDao;
//
//        public DatabaseHelper(Context context){
//            super(context, DB_NAME, null, DB_VERSION);
//
//            checkDatabase();
//
//            /*File dbFile = mContext.getDatabasePath(DB_NAME);
//            File out = new File(mContext.getExternalFilesDir(null)+"/output");
//            Log.d("DB", out.getAbsolutePath());
//            copyFile(dbFile, out, false);
//            dataEncryptHelper = AESCodeManager.getInstance();*/
//        }
//
//        public void copyFile(File srcFile, File destFile, boolean append) {
//            InputStream in = null;
//            FileOutputStream out = null;
//
//            if (!srcFile.exists()) return;
//
//            try {
//                in = new FileInputStream(srcFile);
//
//                File parentFile = destFile.getParentFile();
//                if (!destFile.exists()) {
//                    if (!parentFile.exists()) parentFile.mkdirs();
//                    destFile.createNewFile();
//                }
//
//                out = new FileOutputStream(destFile, append);
//                byte data[] = new byte[1024];
//                int length = -1;
//                while ((length = in.read(data)) != -1) {
//                    out.write(data, 0, length);
//                }
//                out.flush();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    if (in != null) in.close();
//                    if (out != null) out.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        @Override
//        public void onCreate(SQLiteDatabase database, ConnectionSource source){
//            /*try {
//                TableUtils.createTable(source, UserCurrentPosition.class);
//
//                //encryptDatabase(database);
//
//            }catch (SQLException e){
//                e.printStackTrace();
//            }*/
//        }
//
//        @Override
//        public void onUpgrade(SQLiteDatabase database, ConnectionSource source, int oldVersion, int newVersion){
//            copyDatabase();
//            onCreate(database, source);
//            Log.d("LOG","Database updated!");
//        }
//
//        public Dao<Material, Integer> getMaterialDao() throws SQLException{
//            if(materialDao == null)
//                materialDao = getDao(Material.class);
//            return materialDao;
//        }
//
//        @Override
//        public void close(){
//            super.close();
//            materialDao = null;
//        }
//
//        public void checkDatabase(){
//            File dbFile = mContext.getDatabasePath(DB_NAME);
//            if(!dbFile.exists()){
//                copyDatabase();
//                Log.d("LOG:","DB path: "+dbFile.toString());
//                Log.d("LOG:", "successfully init database");
//            }
//            else{
//                Log.d("LOG:", "already exist database");
//            }
//        }
//
//        public void copyDatabase(){
//            try {
//                InputStream input = mContext.getAssets().open(DB_FILE);
//                File destFile = mContext.getDatabasePath(DB_NAME);
//
//                File parentFile = destFile.getParentFile();
//                if (!destFile.exists()) {
//                    if (!parentFile.exists()) parentFile.mkdirs();
//                    destFile.createNewFile();
//                }
//
//                FileOutputStream output = new FileOutputStream(destFile);
//
//                Log.d("LOG:","parentDir: "+parentFile.toString()+" - "+parentFile.exists());
//                Log.d("LOG:","destDir: "+destFile.toString() + " - "+ destFile.exists());
//
//                byte[] mBuffer = new byte[1024];
//                int mLength;
//                while ((mLength = input.read(mBuffer))>0)
//                {
//                    output.write(mBuffer, 0, mLength);
//                }
//                output.flush();
//                input.close();
//                output.close();
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    }
}