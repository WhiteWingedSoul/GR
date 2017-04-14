package com.hedspi.hoangviet.eslrecom;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.hedspi.hoangviet.eslrecom.adapters.TestViewPagerAdapter;
import com.hedspi.hoangviet.eslrecom.commons.Preference;
import com.hedspi.hoangviet.eslrecom.helpers.TestHelper;
import com.hedspi.hoangviet.eslrecom.libraries.NonSwipeableViewPager;
import com.hedspi.hoangviet.eslrecom.managers.DatabaseManager;
import com.hedspi.hoangviet.eslrecom.models.AdapterItem;
import com.hedspi.hoangviet.eslrecom.models.UserProfile;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    private NonSwipeableViewPager viewPager;
    private TestViewPagerAdapter adapter;
    private List<AdapterItem> mItems;
    private UserProfile userProfile;

    boolean inited =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager.enableDebugLogging(true);
        setContentView(R.layout.activity_test);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frameLayout);

        viewPager = (NonSwipeableViewPager) findViewById(R.id.viewPager);
        mItems = new ArrayList<>();
        mItems.add(new AdapterItem() {});
        mItems.add(new AdapterItem() {});

        TestHelper.initTextHelper(DatabaseManager.getQuestionListFromServer());

        adapter = new TestViewPagerAdapter(getSupportFragmentManager(), mItems);
        viewPager.setAdapter(adapter);

    }

    public void initInputProfile(String level){
        if (!inited) {
            inited = true;

            TestHelper.initNewLevelTest(Preference.TEST_GRAMMAR, level);
            userProfile = DatabaseManager.getUserProfile();

            askQuestion();
        }
    }

    public void reevalute(Boolean result){
        switch (TestHelper.reevaluate(result)){
            case TestHelper.STATUS_CONTINUE:
                askQuestion();
                break;
            case TestHelper.STATUS_END:
                startNextTest();
                break;
        }
    }

    private void startNextTest(){
        switch (TestHelper.getCurrentType()){
            case Preference.TEST_GRAMMAR:
                userProfile.setGrammarScore(TestHelper.getCurrentUserProficiency());
                TestHelper.initNewLevelTest(Preference.TEST_VOCAB, TestHelper.getCurrentUserProficiency());

                askQuestion();
                break;
            case Preference.TEST_VOCAB:
                userProfile.setVocabularyScore(TestHelper.getCurrentUserProficiency());
                TestHelper.initNewLevelTest(Preference.TEST_READING, TestHelper.getCurrentUserProficiency());

                askQuestion();
                break;
            case Preference.TEST_READING:
                userProfile.setReadingScore(TestHelper.getCurrentUserProficiency());
                finish();
                break;
        }
    }

    private void askQuestion(){
        adapter.addData(TestHelper.getQuestion());
        toNextFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

//    public boolean isChangingFragment() {
//        return changingFragment;
//    }

    private void toPreviousFragment() {
        int currentPosition = viewPager.getCurrentItem();

        if (currentPosition == 0) {
            viewPager.setCurrentItem(currentPosition, true);
        } else {
            viewPager.setCurrentItem(currentPosition - 1, true);
        }
    }

    public void toNextFragment() {
        int currentPosition = viewPager.getCurrentItem();

        if ((currentPosition + 1) == mItems.size()) {
            finish();
        } else {
            viewPager.setCurrentItem(currentPosition + 1, true);
        }
    }

}
