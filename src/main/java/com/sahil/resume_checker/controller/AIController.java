package com.sahil.resume_checker.controller;


import com.sahil.resume_checker.analyzer.PdfFormattingAnalyzer;
import com.sahil.resume_checker.dto.AIAnalysisResponse;
import com.sahil.resume_checker.dto.FullResumeAnalysisResponse;
import com.sahil.resume_checker.dto.ResumeResponse;
import com.sahil.resume_checker.entity.User;
import com.sahil.resume_checker.repository.UserRepository;
import com.sahil.resume_checker.service.AIResumeAnalysisService;
import com.sahil.resume_checker.service.OllamaService;
import com.sahil.resume_checker.service.ResumeService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.sahil.resume_checker.util.PdfTextExtractor ;
import com.sahil.resume_checker.dto.ResumeRequest ;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")

public class AIController {
    private final AIResumeAnalysisService analysisService;
    private final PdfTextExtractor pdfTextExtractor ;
    private final ResumeService resumeService ;
    private final PdfFormattingAnalyzer pdfFormattingAnalyzer ;
    private final UserRepository userRepository;

    public AIController(
                        AIResumeAnalysisService analysisService , PdfTextExtractor pdfTextExtractor,
                        ResumeService resumeService , PdfFormattingAnalyzer pdfFormattingAnalyzer,
                        UserRepository userRepository) {
        this.analysisService = analysisService ;
        this.pdfTextExtractor = pdfTextExtractor ;
        this.resumeService = resumeService ;
        this.pdfFormattingAnalyzer = pdfFormattingAnalyzer ;
        this.userRepository = userRepository ;
    }

    @PostMapping(
            value = "/analyze-complete",
            consumes = "multipart/form-data"
    )
    public FullResumeAnalysisResponse analyzeComplete(
            @RequestParam("file") MultipartFile file,
            @RequestParam("jobDescription") String jobDescription,
            @RequestParam("requiredSkills") String requiredSkills ,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws IOException {

        if (file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Please upload a PDF resume."
            );
        }

        if (jobDescription == null || jobDescription.isBlank()) {
            throw new IllegalArgumentException(
                    "Job description cannot be empty."
            );
        }

        if (requiredSkills == null || requiredSkills.isBlank()) {
            throw new IllegalArgumentException(
                    "Required skills cannot be empty."
            );
        }

        byte[] pdfBytes = file.getBytes();

        String resumeText = pdfTextExtractor.extractText(
                new ByteArrayInputStream(pdfBytes)
        );


        if (resumeText == null || resumeText.isBlank()) {
            throw new IllegalArgumentException(
                    "No readable text found in the PDF."
            );
        }

        ResumeRequest request = new ResumeRequest();
        request.setResumeText(resumeText);
        request.setRequiredSkills(requiredSkills);

        ResumeResponse skillAnalysis =
                resumeService.checkResume(request);

        PdfFormattingAnalyzer.FormattingReport formattingReport =
                pdfFormattingAnalyzer.analyze(
                        new ByteArrayInputStream(pdfBytes)
                );

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long userId = user.getId();

        AIAnalysisResponse aiAnalysis = analysisService.analyzeResume(
                resumeText ,
                jobDescription,
                userId
        );

        return new FullResumeAnalysisResponse(
                skillAnalysis,
                formattingReport,
                aiAnalysis
        );
    }

    @GetMapping("/test")
    public String test() {
        return "AI resume Screening System is running" ;
    }

    @GetMapping("/history")
    public List<Map<String , Object>> getMyAnalyses(
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("USer not found"));

        return  analysisService.getAnalysesByUserId(user.getId());
    }

}
