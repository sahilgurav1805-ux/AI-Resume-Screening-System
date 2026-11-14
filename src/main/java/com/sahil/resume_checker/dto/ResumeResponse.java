package com.sahil.resume_checker.dto;


import java.util.List;
public class ResumeResponse {


    private List<String> matchedSkills;
    private List<String> missingSkills;
    private double matchPercentage;

    public ResumeResponse(
            List<String> matchedSkills,
            List<String> missingSkills,
            double matchPercentage) {

        this.matchedSkills = matchedSkills;
        this.missingSkills = missingSkills;
        this.matchPercentage = matchPercentage;
    }

    public List<String> getMatchedSkills() {
        return matchedSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public double getMatchPercentage() {
        return matchPercentage;
    }


}
