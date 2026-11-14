
package com.sahil.resume_checker.dto;

public class RecruiterCandidateResult {

    private String fileName;
    private String status;
    private String message;
    private ResumeResponse resumeResponse;
    private Integer rank;

    public RecruiterCandidateResult(String fileName,
                                    ResumeResponse resumeResponse) {
        this.fileName = fileName;
        this.resumeResponse = resumeResponse;
        this.status = "ACCEPTED";
        this.message = "Resume screened successfully";
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank ;
    }

    public RecruiterCandidateResult(String fileName,
                                    String status,
                                    String message) {
        this.fileName = fileName;
        this.status = status;
        this.message = message;
        this.resumeResponse = null;
    }

    public String getFileName() {
        return fileName;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ResumeResponse getResumeResponse() {
        return resumeResponse;
    }
}
