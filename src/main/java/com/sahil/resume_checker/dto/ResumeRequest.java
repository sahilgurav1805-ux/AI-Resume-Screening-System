package com.sahil.resume_checker.dto;

public class ResumeRequest {
    private String resumeText ;
    private String requiredSkills ;

    public String getResumeText() {

        return resumeText ;
    }

    public void setResumeText(String resumeSkills){
        this.resumeText = resumeSkills ;
    }

    public String getRequiredSkills(){

        return requiredSkills ;
    }

    public void setRequiredSkills(String requiredSkills) {

        this.requiredSkills = requiredSkills ;
    }
}
