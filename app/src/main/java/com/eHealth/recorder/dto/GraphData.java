package com.eHealth.recorder.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by electrorobo on 4/29/16.
 */
public class GraphData implements Parcelable {

    private String recordCreationDate;
    private String readingDate;
    private String SBPvalue;
    private String DBPvalue;
    private String diseaseTypeForGraph;
    private String graphDuration;
    private String graphDay;
    private String graphMonth;
    private String graphYear;
    private String regularExpression;
    private HashMap<String, String> hashMap_data;

    public String getRecordCreationDate() {
        return recordCreationDate;
    }

    public void setRecordCreationDate(String recordCreationDate) {
        this.recordCreationDate = recordCreationDate;
    }

    public String getReadingDate() {
        return readingDate;
    }

    public void setReadingDate(String readingDate) {
        this.readingDate = readingDate;
    }

    public String getSBPvalue() {
        return SBPvalue;
    }

    public void setSBPvalue(String SBPvalue) {
        this.SBPvalue = SBPvalue;
    }

    public String getDBPvalue() {
        return DBPvalue;
    }

    public void setDBPvalue(String DBPvalue) {
        this.DBPvalue = DBPvalue;
    }

    public String getDiseaseTypeForGraph() {
        return diseaseTypeForGraph;
    }

    public void setDiseaseTypeForGraph(String diseaseTypeForGraph) {
        this.diseaseTypeForGraph = diseaseTypeForGraph;
    }

    public String getGraphDuration() {
        return graphDuration;
    }

    public void setGraphDuration(String graphDuration) {
        this.graphDuration = graphDuration;
    }

    public String getGraphDay() {
        return graphDay;
    }

    public void setGraphDay(String graphDay) {
        this.graphDay = graphDay;
    }

    public String getGraphMonth() {
        return graphMonth;
    }

    public void setGraphMonth(String graphMonth) {
        this.graphMonth = graphMonth;
    }

    public String getGraphYear() {
        return graphYear;
    }

    public void setGraphYear(String graphYear) {
        this.graphYear = graphYear;
    }

    public String getRegularExpression() {
        return regularExpression;
    }

    public void setRegularExpression(String regularExpression) {
        this.regularExpression = regularExpression;
    }

    public HashMap<String, String> getHashMap_data() {
        return hashMap_data;
    }

    public void setHashMap_data(HashMap<String, String> hashMap_data) {
        this.hashMap_data = hashMap_data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
