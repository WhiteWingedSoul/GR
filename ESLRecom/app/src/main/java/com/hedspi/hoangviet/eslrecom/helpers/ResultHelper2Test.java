package com.hedspi.hoangviet.eslrecom.helpers;

import android.util.Log;

import com.hedspi.hoangviet.eslrecom.managers.DatabaseManager;
import com.hedspi.hoangviet.eslrecom.models.KanseiItem;
import com.hedspi.hoangviet.eslrecom.models.KanseiPreferences;
import com.hedspi.hoangviet.eslrecom.models.MatchResult;
import com.hedspi.hoangviet.eslrecom.models.Material;
import com.hedspi.hoangviet.eslrecom.models.UserProfile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by viet on 4/24/17.
 */

public class ResultHelper2Test {
    private static List<Material> materialBank;
    private static List<MatchResult> sortedMatchResults;
    private static KanseiPreferences userKanseiPreferences;
    private static List<String> showedMaterialTitles;
    private static List<String> likedMaterialTitles;
    private static List<String> likedTags = new ArrayList<>();
    private static UserProfile profile;

    private static int currentItemPos = 0;
    private static int ratedCount = 0;

    public static final int STATUS_END = 1;
    public static final int STATUS_CONTINUE = 2;

    public static void initResultHelper(List<Material> data, UserProfile pf){
        materialBank = data;
        profile = pf;

        userKanseiPreferences = new KanseiPreferences();
        userKanseiPreferences.setDocTypes(new ArrayList<KanseiItem>());
        userKanseiPreferences.setTags(new ArrayList<KanseiItem>());

    }

    public static List<MatchResult> initFirstMatching(){
        List<MatchResult> list = new ArrayList<>();
        sortedMatchResults = new ArrayList<>();
        likedMaterialTitles = new ArrayList<>();

        for (Material material : materialBank) {
            double matchScore = contextMatching(material);
            if (matchScore > DatabaseManager.getPreference().getDecisionBoundary()) {
                MatchResult matchResult = new MatchResult();
                matchResult.setMaterial(material);
                matchResult.setMatchScore(matchScore);

                sortedMatchResults.add(matchResult);
            }
        }

        Collections.sort(sortedMatchResults, new Comparator<MatchResult>() {
            @Override
            public int compare(MatchResult result2, MatchResult result1) {
                return Double.compare(result1.getMatchScore(), result2.getMatchScore());
            }
        });

        list.addAll(sortedMatchResults.subList(0, 5));

        sortedMatchResults = sortedMatchResults.subList(5, sortedMatchResults.size());

        return list;
    }

    public static List<MatchResult> matchingWithKansei(){
        ratedCount = 0;
        List<MatchResult> list = new ArrayList<>();

        for (MatchResult matchResult : sortedMatchResults){
            Material material = matchResult.getMaterial();
            double kanseiScore = kanseiMatching(material);

            matchResult.setKanseiScore(kanseiScore);
        }

        Collections.sort(sortedMatchResults, new Comparator<MatchResult>() {
            @Override
            public int compare(MatchResult result2, MatchResult result1) {
                return Double.compare(result1.getMatchScore()+result1.getKanseiScore(), result2.getMatchScore()+result2.getKanseiScore());
            }
        });

        list.addAll(sortedMatchResults.subList(0, 5));

        sortedMatchResults = sortedMatchResults.subList(5, sortedMatchResults.size());

        return list;
    }

    public static String highLightMatchTags(Material material) {
        String tagsString = new String();
        KanseiItem tagKansei;
        for (String tagName : material.retrieveTagList()) {
            if (DatabaseManager.getTagStringList().contains(tagName)) {
                if (profile.getLearnList().contains(tagName)) {
                    tagsString += "<font color='blue'>" + tagName+ "</font>, ";
                }else if ((tagKansei =userKanseiPreferences.retrieveTag(tagName)) != null && tagKansei.retrieveValue() >= 0.4){
                    tagsString += "<font color='blue'>" + tagName+ "</font>, ";
                }else if ((tagKansei =userKanseiPreferences.retrieveTag(tagName)) != null && tagKansei.retrieveValue() < -0.4){
                    tagsString += "<font color='red'>" + tagName+ "</font>, ";
                }else{
                    tagsString += tagName+ ", ";
                }
            }
        }

        return tagsString;
    }

    public static int reevaluate(double score){
        updateKanseiPreference(score);
        ratedCount++;
        currentItemPos++;
        if (ratedCount == 5){
            return STATUS_END;
        }else{
            return STATUS_CONTINUE;
        }
    }

    private static void updateKanseiPreference(double score){
        Material currentMaterial = sortedMatchResults.get(currentItemPos).getMaterial();
        List<KanseiItem> kanseiDocType = userKanseiPreferences.getDocTypes();
        List<KanseiItem> kanseiTag = userKanseiPreferences.getTags();

        if (score > 0){
            likedMaterialTitles.add(currentMaterial.getName());
        }


        KanseiItem docType = userKanseiPreferences.retrieveDocType(currentMaterial.getName());
        if (docType == null){
            docType = new KanseiItem();
            docType.setName(currentMaterial.getDocumentType());
            if (score>=0)
                docType.addGoodScore(score);
            else
                docType.addBadScore(score);
            kanseiDocType.add(docType);
        }else{
            if (score>=0)
                docType.addGoodScore(score);
            else
                docType.addBadScore(score);
            kanseiDocType.set(kanseiDocType.indexOf(docType), docType);
        }

        for (String tagName : currentMaterial.retrieveTagList()){
            if (DatabaseManager.getTagStringList().contains(tagName)) {
                if (!profile.getLearnList().contains(tagName)) {
                    KanseiItem tag = userKanseiPreferences.retrieveTag(tagName);

                    if (tag == null) {
                        tag = new KanseiItem();
                        tag.setName(tagName);
                        if (score>=0)
                            tag.addGoodScore(score);
                        else
                            tag.addBadScore(score);
                        kanseiTag.add(tag);
                    } else {
                        if (score>=0)
                            tag.addGoodScore(score);
                        else
                            tag.addBadScore(score);
                        kanseiTag.set(kanseiTag.indexOf(tag), tag);
                    }
                } else {
                    Log.d("ahihi", "hihi");
                }
            }
        }
    }

    private static double kanseiMatching(Material material){
        double kanseiScore = 0;

        double kanseiDocTypeScore = matchKanseiDocType(material);
        double kanseiTagScore = matchKanseiTag(material);

        kanseiScore = kanseiTagScore + kanseiDocTypeScore;

        return kanseiScore/DatabaseManager.getPreference().getBestMatch();
    }

    private static double matchKanseiTag(Material material) {
        double kanseiScore = 0;

        if (userKanseiPreferences.getTags().size() != 0){
            List<String> profileTags = material.retrieveTagList();
            for (String tag : profileTags){
                for (KanseiItem item : userKanseiPreferences.getTags()){
                    if (item.getName().equals(tag)){
                        int n = item.getTotalTimeRated();
                        double itemScore = material.getKeywordImportantScore(tag) * item.retrieveValue();

                        kanseiScore += itemScore;
                        break;
                    }
                }
            }

            kanseiScore = kanseiScore * DatabaseManager.getPreference().getLearnPreferenceScore() / 2;
        }

        return kanseiScore;
    }

    private static double matchKanseiDocType(Material material){
        double kanseiScore = 0;

        if (userKanseiPreferences.getDocTypes().size() != 0){
            String docType = material.getDocumentType();

            for (KanseiItem item: userKanseiPreferences.getDocTypes()){
                if (item.getName().equals(docType)){
                    int n = item.getTotalTimeRated();
                    double itemScore = item.retrieveValue();

                    kanseiScore += itemScore;
                    break;
                }
            }

            kanseiScore = kanseiScore * UserProfile.MATCH_SCORE_DOCTYPE;
        }

        return kanseiScore;
    }

    private static double contextMatching(Material material){
        double matchRate = 0;

        double levelMatchScore = matchOverall(material);
//        double testMatchScore = matchTestPrefer(bookProfile);
//        double timeMatchScore = matchTimeSpend(bookProfile);
        double learnListMatchScore = matchLearnList(material);

        matchRate = levelMatchScore + learnListMatchScore; //+ testMatchScore + timeMatchScore ;

        return matchRate/DatabaseManager.getPreference().getBestMatch();
    }

    private static double matchOverall(Material material){
        String level = profile.returnOverallPreference();
        if (material.getName().contains(level) || material.getTag().contains(level))
            return DatabaseManager.getPreference().getOverallScore();
        else
            return 0;
    }

    /*private static double matchTestPrefer(BookProfile bookProfile){
        if(bookProfile.getTestPreference() == profile.getTestPreference())
            return DatabaseManager.getPreference().getTestScore();
        else
            return 0;
    }

    private static double matchTimeSpend(BookProfile bookProfile){
        if(bookProfile.getTimePreference() == profile.getTimeCanSpend())
            return DatabaseManager.getPreference().getTimeScore();
        else
            return 0;
    }*/

    private static double matchLearnList(Material material){
        //First test try: use only word people input to compare
        // if word exists in material tag or name -> its matched!
        double learnlistScore = 0;
        int userLearnListCount = profile.getLearnList().size();
        int matchCount = 0;

        if (userLearnListCount != 0) {
            double matchMaterialScore = 0;
            double matchProfileScore = 0;
            for (String userLearnType : profile.getLearnList()) {
                if (material.getName().contains(userLearnType) || material.getTag().contains(userLearnType)) {
                    matchCount++;
                    matchMaterialScore += material.getKeywordImportantScore(userLearnType);
                }
            }

            matchProfileScore += ((double)matchCount/userLearnListCount);

            learnlistScore = (matchMaterialScore + matchProfileScore) * DatabaseManager.getPreference().getLearnPreferenceScore() / 2;
        }else {
            learnlistScore = DatabaseManager.getPreference().getLearnPreferenceScore();
        }
        return learnlistScore;
    }

    public static KanseiPreferences getUserKanseiPreferences() {
        return userKanseiPreferences;
    }
}
