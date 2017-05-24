package com.hedspi.hoangviet.eslrecom.helpers;

import android.util.Log;

import com.hedspi.hoangviet.eslrecom.managers.DatabaseManager;
import com.hedspi.hoangviet.eslrecom.models.KanseiItem;
import com.hedspi.hoangviet.eslrecom.models.KanseiKeyword;
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
        userKanseiPreferences.initAttributes();

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
            matchResult = kanseiMatching(material, matchResult);
            double kanseiScore = matchResult.getKanseiScore();

            if (kanseiScore <0 ){
                Log.d("test","ahihi");
            }else{
                Log.d("test","ahihi");
            }

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
        KanseiKeyword tagKansei;
        for (String tagName : material.retrieveTagList()) {
            if (DatabaseManager.getTagStringList().contains(tagName)) {
                if (profile.getLearnList().contains(tagName)) {
                    tagsString += "<font color='blue'>" + tagName+ "</font>, ";
//                }else if ((tagKansei =userKanseiPreferences.retrieveTag(tagName)) != null && tagKansei.retrieveValue() >= 0.4){
//                    tagsString += "<font color='blue'>" + tagName+ "</font>, ";
//                }else if ((tagKansei =userKanseiPreferences.retrieveTag(tagName)) != null && tagKansei.retrieveValue() < -0.4){
//                    tagsString += "<font color='red'>" + tagName+ "</font>, ";
                }else{
                    tagsString += tagName+ ", ";
                }
            }
        }

        return tagsString;
    }

    public static int reevaluate(double interestingScore, double understandableScore, double satisfyScore, double affordableScore){
//        updateKanseiPreference(score);
        updateKanseiInteresting(interestingScore);
        updateKanseiUnderstandable(understandableScore);
        updateKanseiSatisfy(satisfyScore);
        updateKanseiAffordable(affordableScore);

        ratedCount++;
        currentItemPos++;
        if (ratedCount == 1){
            return STATUS_END;
        }else{
            return STATUS_CONTINUE;
        }
    }

    private static void updateKanseiInteresting(double score){
        Material currentMaterial = sortedMatchResults.get(currentItemPos).getMaterial();
        KanseiItem interesting = updateKanseiTag(score, userKanseiPreferences.getInteresting(), currentMaterial);
        interesting.addScore(score);
        userKanseiPreferences.setInteresting(interesting);
    }
    private static void updateKanseiSatisfy(double score){
        Material currentMaterial = sortedMatchResults.get(currentItemPos).getMaterial();
        KanseiItem satisfy = updateKanseiTag(score, userKanseiPreferences.getSatisfy(), currentMaterial);
        satisfy.addScore(score);
        userKanseiPreferences.setSatisfy(satisfy);
    }
    private static void updateKanseiUnderstandable(double score){
        Material currentMaterial = sortedMatchResults.get(currentItemPos).getMaterial();
        KanseiItem understandable = updateKanseiTag(score, userKanseiPreferences.getUnderstandable(), currentMaterial);
        understandable.addScore(score);
        userKanseiPreferences.setUnderstandable(understandable);
    }

    private static KanseiItem updateKanseiTag(double score, KanseiItem inputItem, Material currentMaterial){
        KanseiItem item = inputItem;
        for (String tagName : currentMaterial.retrieveTagList()){
            if (DatabaseManager.getTagStringList().contains(tagName)) {
                if (!profile.getLearnList().contains(tagName)) {
                    KanseiKeyword tag = item.retrieveKeyword(tagName);

                    if (tag == null) {
                        if (score!=0) {
                            tag = new KanseiKeyword();
                            tag.setName(tagName);
                            if (score >= 0)
                                tag.addGoodScore(score);
                            else
                                tag.addBadScore(score);
                            item.getKeywords().add(tag);
                        }
                    } else {
                        if (score!=0) {
                            if (score >= 0)
                                tag.addGoodScore(score);
                            else
                                tag.addBadScore(score);
                            item.getKeywords().set(item.getKeywords().indexOf(tag), tag);
                        }
                    }
                } else {
                    Log.d("ahihi", "hihi");
                }
            }
        }

        return item;
    }

//    private static void updateKanseiAcceptable(double score){
//        Material currentMaterial = sortedMatchResults.get(currentItemPos).getMaterial();
//        KanseiItem acceptable = updateKanseiName(score, userKanseiPreferences.getAcceptable(), currentMaterial);
//
//        acceptable.addScore(score);
//        userKanseiPreferences.setAcceptable(acceptable);
//    }

//    private static KanseiItem updateKanseiName(double score, KanseiItem inputItem, Material currentMaterial){
//        KanseiItem item = inputItem;
//
//        for (String keyWordName : currentMaterial.retrieveKeywordsInName()){
//            KanseiKeyword keyword = item.retrieveKeyword(keyWordName);
//
//            if (keyword == null) {
//                keyword = new KanseiKeyword();
//                keyword.setName(keyWordName);
//                if (score>=0)
//                    keyword.addGoodScore(score);
//                else
//                    keyword.addBadScore(score);
//                item.getKeywords().add(keyword);
//            } else {
//                if (score>=0)
//                    keyword.addGoodScore(score);
//                else
//                    keyword.addBadScore(score);
//                item.getKeywords().set(item.getKeywords().indexOf(keyword), keyword);
//            }
//        }
//
//        return item;
//    }

    //TODO
    private static void updateKanseiAffordable(double score){
        Material currentMaterial = sortedMatchResults.get(currentItemPos).getMaterial();
        KanseiItem affordable = userKanseiPreferences.getAffordable();

        if (currentMaterial.getSellerPrice() != null) {
            double priceValue = Double.valueOf(currentMaterial.getSellerPrice().replace("$",""));
            if (affordable.getKeywords().size()==0){
                if (score!=0) {
                    KanseiKeyword price = new KanseiKeyword();
                    price.setName("price");
                    price.addValue(priceValue * Math.abs(score));

                    affordable.getKeywords().add(price);
                }
            }else{
                if (score>0) {
                    KanseiKeyword price = affordable.getKeywords().get(0);
                    price.addValue(priceValue * Math.abs(score));

                    affordable.getKeywords().set(0, price);
                }
            }
        }

        affordable.addScore(score);
        userKanseiPreferences.setAffordable(affordable);
    }

//    private static void updateKanseiPreference(double score){
//        Material currentMaterial = sortedMatchResults.get(currentItemPos).getMaterial();
//        List<KanseiKeyword> kanseiDocType = userKanseiPreferences.getDocTypes();
//        List<KanseiKeyword> kanseiTag = userKanseiPreferences.getTags();
//
//        if (score > 0){
//            likedMaterialTitles.add(currentMaterial.getName());
//        }
//
//
//        KanseiKeyword docType = userKanseiPreferences.retrieveDocType(currentMaterial.getName());
//        if (docType == null){
//            docType = new KanseiKeyword();
//            docType.setName(currentMaterial.getDocumentType());
//            if (score>=0)
//                docType.addGoodScore(score);
//            else
//                docType.addBadScore(score);
//            kanseiDocType.add(docType);
//        }else{
//            if (score>=0)
//                docType.addGoodScore(score);
//            else
//                docType.addBadScore(score);
//            kanseiDocType.set(kanseiDocType.indexOf(docType), docType);
//        }
//
//        for (String tagName : currentMaterial.retrieveTagList()){
//            if (DatabaseManager.getTagStringList().contains(tagName)) {
//                if (!profile.getLearnList().contains(tagName)) {
//                    KanseiKeyword tag = userKanseiPreferences.retrieveTag(tagName);
//
//                    if (tag == null) {
//                        tag = new KanseiKeyword();
//                        tag.setName(tagName);
//                        if (score>=0)
//                            tag.addGoodScore(score);
//                        else
//                            tag.addBadScore(score);
//                        kanseiTag.add(tag);
//                    } else {
//                        if (score>=0)
//                            tag.addGoodScore(score);
//                        else
//                            tag.addBadScore(score);
//                        kanseiTag.set(kanseiTag.indexOf(tag), tag);
//                    }
//                } else {
//                    Log.d("ahihi", "hihi");
//                }
//            }
//        }
//    }

//    private static double kanseiMatching(Material material){
//        double kanseiScore = 0;
//
//        for(KanseiItem attribute : userKanseiPreferences.allAttributes()){
//            switch (attribute.getTarget()){
//                case KanseiPreferences.TARGET_NAME:
//                    double kanseiNameScore = matchKanseiName(material);
//            }
//        }
//
//        double kanseiDocTypeScore = matchKanseiDocType(material);
//        double kanseiTagScore = matchKanseiTag(material);
//
//        kanseiScore = kanseiTagScore + kanseiDocTypeScore;
//
//        if (kanseiScore <0 ){
//            Log.d("test","ahihi");
//        }else{
//            Log.d("test","ahihi");
//        }
//
//        return kanseiScore/DatabaseManager.getPreference().getBestMatch();
//    }

    private static MatchResult kanseiMatching(Material material, MatchResult result){
        MatchResult matchResult = result;
        double mauso = DatabaseManager.getPreference().getBestMatch()+userKanseiPreferences.getAllWeights();
        double kanseiScore = 0;
        matchResult = matchInteresting(material, matchResult);
        matchResult.setInterestingScore(matchResult.getInterestingScore()/mauso);
        matchResult = matchUnderstandable(material, matchResult);
        matchResult.setUnderstandableScore(matchResult.getUnderstandableScore()/mauso);
        matchResult = matchSatisfy(material, matchResult);
        matchResult.setSatisfyScore(matchResult.getSatisfyScore()/mauso);
        matchResult = matchAffordable(material, matchResult);
        matchResult.setAffordableScore(matchResult.getAffordableScore()/mauso);

        kanseiScore = matchResult.getAffordableScore() + matchResult.getUnderstandableScore()+ matchResult.getSatisfyScore()+ matchResult.getInterestingScore();

        if (kanseiScore <0 ){
            Log.d("test","ahihi");
        }else{
            Log.d("test","ahihi");
        }
        matchResult.setKanseiScore(kanseiScore);
        return matchResult;

    }

    private static MatchResult matchInteresting(Material material, MatchResult result){
        MatchResult matchResult = result;
        KanseiItem matchItem = userKanseiPreferences.getInteresting();
        matchResult = matchKanseiTag(material, matchItem.getKeywords(), matchResult);
        matchResult.setInterestingScore(matchResult.getKanseiScore()*matchItem.retrieveWeight());
        matchResult.setInterestingItems(matchResult.getKanseiItems());

        return matchResult;
    }

    private static MatchResult matchSatisfy(Material material, MatchResult result){
        MatchResult matchResult = result;
        KanseiItem matchItem = userKanseiPreferences.getSatisfy();
        matchResult = matchKanseiTag(material, matchItem.getKeywords(), matchResult);
        matchResult.setSatisfyScore(matchResult.getKanseiScore()*matchItem.retrieveWeight());
        matchResult.setSatisfyItems(matchResult.getKanseiItems());

        return matchResult;
    }

    private static MatchResult matchUnderstandable(Material material, MatchResult result){
        MatchResult matchResult = result;
        KanseiItem matchItem = userKanseiPreferences.getUnderstandable();
        matchResult = matchKanseiTag(material, matchItem.getKeywords(), matchResult);
        matchResult.setUnderstandableScore(matchResult.getKanseiScore()*matchItem.retrieveWeight());
        matchResult.setUnderstandableItems(matchResult.getKanseiItems());

        return matchResult;
    }

    // PRICE DIFFERENT

    private static MatchResult matchAffordable(Material material, MatchResult result) {
        MatchResult matchResult = result;
        KanseiItem matchItem = userKanseiPreferences.getAffordable();
        if (matchItem.getKeywords().size()!=0) {
            KanseiKeyword matchPrice = matchItem.getKeywords().get(0);
            double matchPriceValue = matchPrice.getValue() / matchPrice.getTotalTimeRated();
            matchResult.setAffordableItem(matchPrice);
            double looseRate;
            if (material.getOnlineLink() != null) {
                matchResult.setAffordableScore(matchItem.retrieveWeight());
                return matchResult;
            }
            if (material.getSellerPrice() != null) {
                double priceValue = Double.valueOf(material.getSellerPrice().replace("$", ""));
                if (priceValue <= matchPriceValue) {
                    matchResult.setAffordableScore(matchItem.retrieveWeight());
                    return matchResult;
                }
                else if ((looseRate = Math.abs(matchPriceValue - priceValue) / matchPriceValue) < 0.3) {
                    matchResult.setAffordableScore(matchItem.retrieveWeight()* (1 - looseRate));
                    return matchResult;
                }
                else {
                    matchResult.setAffordableScore(-matchItem.retrieveWeight());
                    return matchResult;
                }
            }
        }
        matchResult.setAffordableScore(0);
        return matchResult;
    }

    private static MatchResult matchKanseiTag(Material material, List<KanseiKeyword> keywords, MatchResult result) {
        MatchResult matchResult = result;
        List<KanseiKeyword> items = new ArrayList<>();
        double kanseiScore = 0;


        if (keywords.size() != 0){
            List<String> profileTags = material.retrieveTagList();
            for (String tag : profileTags){
                for (KanseiKeyword item : keywords){
                    if (item.getName().equals(tag)){
                        int n = item.getTotalTimeRated();
                        double importantScore = material.getKeywordImportantScore(tag);
                        double value = item.retrieveValue();
                        double itemScore =  importantScore * value;


                        kanseiScore += itemScore;
                        KanseiKeyword mItem = new KanseiKeyword();
                        mItem.setName(item.getName());
                        mItem.setValue(itemScore);
                        mItem.setTotalBadRated(item.getTotalBadRated());
                        mItem.setTotalGoodRated(item.getTotalGoodRated());

                        items.add(mItem);
                        break;
                    }
                }
            }

//            kanseiScore = kanseiScore * DatabaseManager.getPreference().getLearnPreferenceScore() / 2;
        }


        matchResult.setKanseiScore(kanseiScore);
        matchResult.setKanseiItems(items);

        return matchResult;
    }

//    private static double matchKanseiTag(Material material) {
//        double kanseiScore = 0;
//
//        if (userKanseiPreferences.getTags().size() != 0){
//            List<String> profileTags = material.retrieveTagList();
//            for (String tag : profileTags){
//                for (KanseiKeyword item : userKanseiPreferences.getTags()){
//                    if (item.getName().equals(tag)){
//                        int n = item.getTotalTimeRated();
//                        double itemScore = material.getKeywordImportantScore(tag) * item.retrieveValue();
//
//                        kanseiScore += itemScore;
//                        break;
//                    }
//                }
//            }
//
//            kanseiScore = kanseiScore * DatabaseManager.getPreference().getLearnPreferenceScore() / 2;
//        }

//        return kanseiScore;
//    }

//    private static double matchKanseiDocType(Material material){
//        double kanseiScore = 0;
//
//        if (userKanseiPreferences.getDocTypes().size() != 0){
//            String docType = material.getDocumentType();
//
//            for (KanseiKeyword item: userKanseiPreferences.getDocTypes()){
//                if (item.getName().equals(docType)){
//                    int n = item.getTotalTimeRated();
//                    double itemScore = item.retrieveValue();
//
//                    kanseiScore += itemScore;
//                    break;
//                }
//            }
//
//            kanseiScore = kanseiScore * UserProfile.MATCH_SCORE_DOCTYPE;
//        }
//
//        return kanseiScore;
//    }

    private static double contextMatching(Material material){
        double matchRate = 0;

        double levelMatchScore = matchOverall(material);
        double learnListMatchScore = matchLearnList(material);

        matchRate = levelMatchScore + learnListMatchScore;

        return matchRate/DatabaseManager.getPreference().getBestMatch();
    }

    private static double matchOverall(Material material){
        String level = profile.returnOverallPreference();
        if (material.getName().contains(level) || material.getTag().contains(level))
            return DatabaseManager.getPreference().getOverallScore();
        else
            return 0;
    }

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
//                    if (userLearnListCount>2)
                        matchMaterialScore += material.getKeywordImportantScore(userLearnType);
//                    else
//                        matchMaterialScore += 1;
                    Log.d("ahihi","");
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
