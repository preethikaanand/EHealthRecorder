package com.eHealth.recorder.dto;

/**
 * Created by electrorobo on 4/20/16.
 */
public class MedicalPrescription {

    private String prescribedBy;
    private String prescribedTo;
    private String diseaseName;
    private String medicalTests;
    private String precautions;
    private String prescriptionEndDate;
    private String morningMedicine;
    private String noonMedicine;
    private String eveMedicine;

    public String getPrescribedBy() {
        return prescribedBy;
    }

    public void setPrescribedBy(String prescribedBy) {
        this.prescribedBy = prescribedBy;
    }

    public String getPrescribedTo() {
        return prescribedTo;
    }

    public void setPrescribedTo(String prescribedTo) {
        this.prescribedTo = prescribedTo;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getMedicalTests() {
        return medicalTests;
    }

    public void setMedicalTests(String medicalTests) {
        this.medicalTests = medicalTests;
    }

    public String getPrecautions() {
        return precautions;
    }

    public void setPrecautions(String precautions) {
        this.precautions = precautions;
    }

    public String getPrescriptionEndDate() {
        return prescriptionEndDate;
    }

    public void setPrescriptionEndDate(String prescriptionEndDate) {
        this.prescriptionEndDate = prescriptionEndDate;
    }

    public String getMorningMedicine() {
        return morningMedicine;
    }

    public void setMorningMedicine(String morningMedicine) {
        this.morningMedicine = morningMedicine;
    }

    public String getNoonMedicine() {
        return noonMedicine;
    }

    public void setNoonMedicine(String noonMedicine) {
        this.noonMedicine = noonMedicine;
    }

    public String getEveMedicine() {
        return eveMedicine;
    }

    public void setEveMedicine(String eveMedicine) {
        this.eveMedicine = eveMedicine;
    }
}
