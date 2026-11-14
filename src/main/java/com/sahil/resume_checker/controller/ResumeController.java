//@RestController --> tells spring that this class handles web resquest and its erturn values should be written directly yo http response body


package com.sahil.resume_checker.controller;

import com.sahil.resume_checker.analyzer.PdfFormattingAnalyzer;
import com.sahil.resume_checker.dto.ResumeRequest;
import com.sahil.resume_checker.service.ResumeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sahil.resume_checker.dto.ResumeResponse;
import com.sahil.resume_checker.util.PdfTextExtractor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    private final ResumeService resumeService;
    private final PdfTextExtractor pdfTextExtractor ;
    private final PdfFormattingAnalyzer pdfFormattingAnalyzer;

    public ResumeController(ResumeService resumeService , PdfTextExtractor pdfTextExtractor ,
                            PdfFormattingAnalyzer pdfFormattingAnalyzer) {
        this.resumeService = resumeService;
        this.pdfTextExtractor = pdfTextExtractor ;
        this.pdfFormattingAnalyzer = pdfFormattingAnalyzer ;
    }

    @PostMapping("/check")
    public ResumeResponse checkResume(@RequestBody ResumeRequest request) {
        return resumeService.checkResume(request);
    }

    @PostMapping(
            value = "/upload" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResumeResponse uploadResume(@RequestParam("file") MultipartFile file ,
            @RequestParam("requiredSkills") String requiredSkills)
            throws IOException{

       if (file.isEmpty()) {
           throw new IllegalArgumentException("Please upload a PDF file.");
       }

       byte[] pdfBytes  = file.getBytes() ;

       String resumeText = pdfTextExtractor.extractText(new ByteArrayInputStream(pdfBytes));

       ResumeRequest request = new ResumeRequest() ;
       request.setResumeText(resumeText);
       request.setRequiredSkills(requiredSkills);

       ResumeResponse response = resumeService.checkResume(request) ;

       return response ;


    }


}
