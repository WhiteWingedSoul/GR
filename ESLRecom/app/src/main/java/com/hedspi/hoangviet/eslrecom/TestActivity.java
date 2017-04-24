package com.hedspi.hoangviet.eslrecom;

import android.animation.Animator;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.TransitionManager;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hedspi.hoangviet.eslrecom.adapters.TestViewPagerAdapter;
import com.hedspi.hoangviet.eslrecom.commons.Common;
import com.hedspi.hoangviet.eslrecom.commons.Preference;
import com.hedspi.hoangviet.eslrecom.helpers.TestHelper;
import com.hedspi.hoangviet.eslrecom.libraries.NonSwipeableViewPager;
import com.hedspi.hoangviet.eslrecom.managers.AnimationHelper;
import com.hedspi.hoangviet.eslrecom.managers.DatabaseManager;
import com.hedspi.hoangviet.eslrecom.models.AdapterItem;
import com.hedspi.hoangviet.eslrecom.models.Question;
import com.hedspi.hoangviet.eslrecom.models.UserProfile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.grantland.widget.AutofitTextView;

public class TestActivity extends AppCompatActivity {
    private NonSwipeableViewPager viewPager;
    private TestViewPagerAdapter adapter;
    private List<AdapterItem> mItems;
    private UserProfile userProfile;

    private TextView userName;
    private CircleImageView avatar;
    private TextView sumUserName;
    private TextView sumEmail;
    private CircleImageView sumAva;
    private TextView questionNum;
    private TextView subject;
    private AutofitTextView question;
    private Button learnButton;
    private Button backButton;
    private TextView readingProfi;
    private TextView grammarProfi;
    private TextView vocabularyProfi;

    private View summaryProfile;
    private View testProfile;

    boolean inited =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager.enableDebugLogging(true);
        setContentView(R.layout.activity_test);

        UserProfile user = DatabaseManager.getUserProfile();

        userName = (TextView) findViewById(R.id.userName);
        avatar = (CircleImageView) findViewById(R.id.avatar);
        sumUserName = (TextView) findViewById(R.id.sumUserName);
        sumEmail = (TextView) findViewById(R.id.sumEmail);
        sumAva = (CircleImageView) findViewById(R.id.sumAva);
        question = (AutofitTextView) findViewById(R.id.question);
        subject = (TextView) findViewById(R.id.subject);
        questionNum = (TextView) findViewById(R.id.questionNum);
        summaryProfile = (View) findViewById(R.id.summaryProfile);
        testProfile = (View) findViewById(R.id.testProfile);
        learnButton = (Button) findViewById(R.id.learnButton);
        backButton = (Button) findViewById(R.id.backButton);

        grammarProfi = (TextView) findViewById(R.id.grammarProfi);
        readingProfi = (TextView) findViewById(R.id.readingProfi);
        vocabularyProfi = (TextView) findViewById(R.id.vocabularyProfi);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        learnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationHelper.playDisappearAnimation(summaryProfile, 0, new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        summaryProfile.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

                AnimationHelper.playAppearAnimation(viewPager, 250, new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        viewPager.setAlpha(0);
                        viewPager.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        viewPager.setAlpha(1);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

            }
        });

        Picasso.with(this).load(user.getAvatarLink())
                .fit()
                .into(avatar);
        Picasso.with(this).load(user.getAvatarLink())
                .fit()
                .into(sumAva);
        userName.setText(user.getName());
        sumUserName.setText(user.getName());
        sumEmail.setText(user.getEmail());

        viewPager = (NonSwipeableViewPager) findViewById(R.id.viewPager);
        mItems = new ArrayList<>();
        mItems.add(new AdapterItem() {});
        mItems.add(new AdapterItem() {});

        TestHelper.initTextHelper(DatabaseManager.getQuestionListFromServer(this));

        adapter = new TestViewPagerAdapter(getSupportFragmentManager(), mItems);
        viewPager.setAdapter(adapter);

    }

    public void initInputProfile(String level){
        if (!inited) {
            inited = true;

            testProfile.setVisibility(View.VISIBLE);

            TestHelper.initNewLevelTest(Preference.TEST_GRAMMAR, level);
            subject.setText(getResources().getString(R.string.grammar));
            AnimationHelper.playAppearAnimation(subject,0,null);

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
                userProfile.calculateGrammarScore(TestHelper.getCurrentUserProficiency());
                TestHelper.initNewLevelTest(Preference.TEST_VOCAB, TestHelper.getCurrentUserProficiency());
                subject.setText(getResources().getString(R.string.vocabulary));
                AnimationHelper.playAppearAnimation(subject,0,null);
                grammarProfi.setText(TestHelper.getCurrentUserProficiency());

                askQuestion();
                break;
            case Preference.TEST_VOCAB:
                userProfile.calculateVocabularyScore(TestHelper.getCurrentUserProficiency());
                TestHelper.initNewLevelTest(Preference.TEST_READING, TestHelper.getCurrentUserProficiency());
                subject.setText(getResources().getString(R.string.reading));
                AnimationHelper.playAppearAnimation(subject,0,null);
                vocabularyProfi.setText(TestHelper.getCurrentUserProficiency());

                askQuestion();
                break;
            case Preference.TEST_READING:
                userProfile.calculateReadingScore(TestHelper.getCurrentUserProficiency());
                readingProfi.setText(TestHelper.getCurrentUserProficiency());

                userProfile.setTestProf(true);

                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                database.child(Common.USERS).child(userProfile.getUid()).setValue(userProfile);

                viewPager.setVisibility(View.GONE);
                testProfile.setVisibility(View.GONE);
                summaryProfile.setVisibility(View.VISIBLE);
                AnimationHelper.playAppearAnimation(summaryProfile, 0 , null);
                learnButton.setVisibility(View.GONE);
                backButton.setVisibility(View.VISIBLE);

//                finish();
                break;
        }
    }

    private void askQuestion(){
        Question questionStr = TestHelper.getQuestion();
        adapter.addData(questionStr);
        question.setText(questionStr.getQuestion());
        AnimationHelper.playAppearAnimation(question,0,null);
        questionNum.setText(""+TestHelper.getQuestionNum());
        AnimationHelper.playAppearAnimation(questionNum,0,null);

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
