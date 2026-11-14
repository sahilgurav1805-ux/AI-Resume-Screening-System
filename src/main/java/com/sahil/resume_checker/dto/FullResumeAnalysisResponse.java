package com.sahil.resume_checker.dto;

import com.sahil.resume_checker.analyzer.PdfFormattingAnalyzer.FormattingReport;

public class FullResumeAnalysisResponse {
    private ResumeResponse skillAnalysis ;
    private  FormattingReport formattingReport ;
    private AIAnalysisResponse aiAnalysis ;
    public FullResumeAnalysisResponse(
            ResumeResponse skillAnalysis ,
            FormattingReport formattingReport ,
            AIAnalysisResponse aiAnalysis ) {

        this.skillAnalysis = skillAnalysis ;
        this.formattingReport = formattingReport ;
        this.aiAnalysis = aiAnalysis ;

    }

    public ResumeResponse getSkillAnalysis() {
        return skillAnalysis ;
    }

    public FormattingReport getFormattingReport() {
        return formattingReport ;
    }

    public AIAnalysisResponse getAiAnalysis() {
        return aiAnalysis ;
    }



}
