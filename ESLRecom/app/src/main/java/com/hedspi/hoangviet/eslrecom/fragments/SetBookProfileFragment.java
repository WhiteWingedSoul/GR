package com.hedspi.hoangviet.eslrecom.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hedspi.hoangviet.eslrecom.R;
import com.hedspi.hoangviet.eslrecom.commons.Preference;
import com.hedspi.hoangviet.eslrecom.managers.DatabaseManager;
import com.hedspi.hoangviet.eslrecom.models.Material;
import com.hedspi.hoangviet.eslrecom.models.BookProfile;
import com.squareup.picasso.Picasso;

/**
 * Created by hoangviet on 11/20/16.
 */

public class SetBookProfileFragment extends Fragment {
    private View view;
    private Material book;
    private RadioGroup.OnCheckedChangeListener levelListener1;
    private RadioGroup.OnCheckedChangeListener levelListener2;
    private RadioGroup.OnCheckedChangeListener testListener1;
    private RadioGroup.OnCheckedChangeListener testListener2;


    public static SetBookProfileFragment newInstance(Material book){
        SetBookProfileFragment fragment = new SetBookProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("book", book);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        if(view == null)
            view = inflater.inflate(R.layout.fragment_set_book_profile, viewGroup, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        book = (Material) getArguments().getSerializable("book");
        ImageView bookIcon = (ImageView) view.findViewById(R.id.bookIcon);
        Picasso.with(getActivity()).load(book.getCoverLink())
                .fit()
                .into(bookIcon);
        TextView title = (TextView) view.findViewById(R.id.title);
        if (book.getName() == null || book.getName().equals("")){
            title.setText("");
        }else{
            title.setText(book.getName());
        }
        TextView author = (TextView) view.findViewById(R.id.author);
        if (book.getAuthor() == null || book.getAuthor().equals("")){
            author.setText("");
        }else{
            author.setText("bởi "+book.getAuthor());
        }
        TextView publisher = (TextView) view.findViewById(R.id.publisher);
        if (book.getPublisher() == null || book.getPublisher().equals("")){
            publisher.setText("");
        }else{
            publisher.setText(book.getPublisher());
        }
        TextView editionformat= (TextView) view.findViewById(R.id.editionformat);
        if (book.getEdition_format() == null || book.getEdition_format().equals("")){
            editionformat.setText("");
        }else{
            editionformat.setText(book.getEdition_format());
        }
        TextView genreform = (TextView) view.findViewById(R.id.genreform);
        if (book.getGenre_form() == null || book.getGenre_form().equals("")){
            genreform.setText("");
        }else{
            genreform.setText("Thể loại: "+book.getGenre_form());
        }
        TextView docType= (TextView) view.findViewById(R.id.docType);
        if (book.getDocumentType() == null || book.getDocumentType().equals("")){
            docType.setText("");
        }else{
            docType.setText("Loại tài liệu: "+book.getDocumentType());
        }
        TextView content = (TextView) view.findViewById(R.id.content);
        View contentLayout = view.findViewById(R.id.contentLayout);
        if (book.getContent() == null || book.getContent().equals("")){
            contentLayout.setVisibility(View.GONE);
        }else{
            contentLayout.setVisibility(View.VISIBLE);
            content.setText(book.getContent());
        }
        TextView summary = (TextView) view.findViewById(R.id.summary);
        if (book.getSummary() == null || book.getSummary().equals("")){
            summary.setText("");
        }else{
            summary.setText("Tóm tắt: \n\t"+book.getSummary());
        }
        TextView subjects = (TextView) view.findViewById(R.id.subjects);
        View subjectLayout = view.findViewById(R.id.subjectLayout);

        if (book.getSubjects() == null || book.getSubjects().equals("")){
            subjectLayout.setVisibility(View.GONE);
        }else{
            subjectLayout.setVisibility(View.VISIBLE);
            subjects.setText(book.getSubjects());
        }

        final BookProfile bookProfile = new BookProfile();
        bookProfile.setId(book.getId());
        bookProfile.setBook(book);
        final RadioGroup levelGroup1 = (RadioGroup) view.findViewById(R.id.levelanswerGroup1);
        final RadioGroup levelGroup2 = (RadioGroup) view.findViewById(R.id.levelanswerGroup2);
        levelListener1 = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i != -1) {
                    levelGroup2.setOnCheckedChangeListener(null);
                    levelGroup2.clearCheck();
                    levelGroup2.setOnCheckedChangeListener(levelListener2);
                    /*switch (i) {
                        case R.id.l_answer_a:
                            bookProfile.setLevelPreference(Preference.BEGINNER);
                            break;
                        case R.id.l_answer_b:
                            bookProfile.setLevelPreference(Preference.ELEMENTARY);
                            break;
                        case R.id.l_answer_c:
                            bookProfile.setLevelPreference(Preference.INTERMEDIATE);
                            break;
                    }*/
                }
            }
        };
        levelListener2 = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i!=-1) {
                    levelGroup1.setOnCheckedChangeListener(null);
                    levelGroup1.clearCheck();
                    levelGroup1.setOnCheckedChangeListener(levelListener1);
                    /*switch (i) {
                        case R.id.l_answer_d:
                            bookProfile.setLevelPreference(Preference.ADVANCE);
                            break;
                        case R.id.l_answer_e:
                            bookProfile.setLevelPreference(Preference.MASTER);
                            break;
                    }*/
                }
            }
        };
        levelGroup1.setOnCheckedChangeListener(levelListener1);
        levelGroup2.setOnCheckedChangeListener(levelListener2);

        final RadioGroup testGroup1 = (RadioGroup) view.findViewById(R.id.testanswerGroup1);
        final RadioGroup testGroup2 = (RadioGroup) view.findViewById(R.id.testanswerGroup2);
        testListener1 = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i != -1) {
                    testGroup2.setOnCheckedChangeListener(null);
                    testGroup2.clearCheck();
                    testGroup2.setOnCheckedChangeListener(testListener2);
                    switch (i) {
                        case R.id.t_answer_a:
                            bookProfile.setTestPreference(Preference.NONE);
                            break;
                        case R.id.t_answer_b:
                            bookProfile.setTestPreference(Preference.IELTS);
                            break;
                        case R.id.t_answer_c:
                            bookProfile.setTestPreference(Preference.TOEFL);
                            break;
                    }
                }
            }
        };
        testListener2 = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i!=-1) {
                    testGroup1.setOnCheckedChangeListener(null);
                    testGroup1.clearCheck();
                    testGroup1.setOnCheckedChangeListener(testListener1);
                    switch (i) {
                        case R.id.t_answer_d:
                            bookProfile.setTestPreference(Preference.TOEIC);
                            break;
                        case R.id.t_answer_e:
                            bookProfile.setTestPreference(Preference.iTEC);
                            break;
                    }
                }
            }
        };
        testGroup1.setOnCheckedChangeListener(testListener1);
        testGroup2.setOnCheckedChangeListener(testListener2);
        final RadioGroup timeGroup1 = (RadioGroup) view.findViewById(R.id.timeanswerGroup);
        timeGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
                    case R.id.tm_answer_a:
                        bookProfile.setTimePreference(Preference.LESS1);
                        break;
                    case R.id.tm_answer_b:
                        bookProfile.setTimePreference(Preference.FROM1TO3);
                        break;
                    case R.id.tm_answer_c:
                        bookProfile.setTimePreference(Preference.MORE3 );
                        break;
                }
            }
        });

        final CheckBox answer_a = (CheckBox) view.findViewById(R.id.answer_a);
        final CheckBox answer_b = (CheckBox) view.findViewById(R.id.answer_b);
        final CheckBox answer_c = (CheckBox) view.findViewById(R.id.answer_c);
        final CheckBox answer_d = (CheckBox) view.findViewById(R.id.answer_d);
        final CheckBox answer_e = (CheckBox) view.findViewById(R.id.answer_e);
        final CheckBox answer_f = (CheckBox) view.findViewById(R.id.answer_f);
        final CheckBox answer_g = (CheckBox) view.findViewById(R.id.answer_g);

        answer_a.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    bookProfile.getLearnSubjectPreference().add("reading");
                }else{
                    bookProfile.getLearnSubjectPreference().remove("reading");
                }
            }
        });
        answer_b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    bookProfile.getLearnSubjectPreference().add("listening");
                }else{
                    bookProfile.getLearnSubjectPreference().remove("listening");
                }
            }
        });
        answer_c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    bookProfile.getLearnSubjectPreference().add("speaking");
                }else{
                    bookProfile.getLearnSubjectPreference().remove("speaking");
                }
            }
        });
        answer_d.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    bookProfile.getLearnSubjectPreference().add("pronounce");
                }else{
                    bookProfile.getLearnSubjectPreference().remove("pronounce");
                }
            }
        });
        answer_e.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    bookProfile.getLearnSubjectPreference().add("vocabulary");
                }else{
                    bookProfile.getLearnSubjectPreference().remove("vocabulary");
                }
            }
        });
        answer_f.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    bookProfile.getLearnSubjectPreference().add("grammar");
                }else{
                    bookProfile.getLearnSubjectPreference().remove("grammar");
                }
            }
        });
        answer_g.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    bookProfile.getLearnSubjectPreference().add("writting");
                }else{
                    bookProfile.getLearnSubjectPreference().remove("writting");
                }
            }
        });

        Button saveButton = (Button) view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bookProfile.getTestPreference()!=0 && bookProfile.getLevelPreference()!=0
                        && bookProfile.getTimePreference()!=0 && bookProfile.getLearnSubjectPreference().size()!=0){
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                    long booksProfileCount = DatabaseManager.getBookProfilesCount();

                    database.child("book-profiles").child(""+book.getId()).setValue(bookProfile);
                    database.child("books").child(""+book.getId()).removeValue();
                    booksProfileCount = booksProfileCount + 1;
                    database.child("books-count").child(""+book.getId()).removeValue();
                    database.child("bookProfiles-count").child(""+booksProfileCount).setValue(book.getId());
                    getActivity().finish();
                }
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }



}