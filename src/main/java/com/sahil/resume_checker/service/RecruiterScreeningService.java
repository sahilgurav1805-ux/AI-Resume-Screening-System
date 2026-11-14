package com.sahil.resume_checker.service;


import com.sahil.resume_checker.dto.RecruiterCandidateResult;
import com.sahil.resume_checker.dto.ResumeRequest;
import com.sahil.resume_checker.dto.ResumeResponse;
import com.sahil.resume_checker.util.PdfTextExtractor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;


@Service
public class RecruiterScreeningService {

    private final ResumeService resumeService ;
    private final PdfTextExtractor pdfTextExtractor ;

    public RecruiterScreeningService(
            ResumeService resumeService ,
            PdfTextExtractor pdfTextExtractor
    ) {
        this.resumeService = resumeService ;
        this.pdfTextExtractor = pdfTextExtractor ;
    }


    public List<RecruiterCandidateResult> screenResumes(
            List<MultipartFile> files ,
            String requiredSkills
    )  {


        List<RecruiterCandidateResult> results = new ArrayList<>();


        for (MultipartFile file : files) {


            if (file.isEmpty()) {
                results.add(new RecruiterCandidateResult(
                        file.getOriginalFilename(),
                        "REJECTED",
                        "Uploaded file is empty."
                ));

                continue;
            }

            String fileName = file.getOriginalFilename();

            if (fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {

                results.add(new RecruiterCandidateResult(
                        fileName,
                        "REJECTED",
                        "Only PDF files are supported."
                ));
                continue;
            }

            try {


                String resumeText = pdfTextExtractor.extractText(
                        new ByteArrayInputStream(file.getBytes())

                );


                if (!isLikelyResume(resumeText)) {
                    results.add(new RecruiterCandidateResult(
                            file.getOriginalFilename(),
                            "REJECTED",
                            "This document does not appear to be a resume."
                    ));

                    continue;
                }

                ResumeRequest request = new ResumeRequest();
                request.setResumeText(resumeText);
                request.setRequiredSkills(requiredSkills);

                ResumeResponse response = resumeService.checkResume(request);

                results.add(new RecruiterCandidateResult(
                        file.getOriginalFilename(),
                        response
                ));
            }
            catch (IOException e) {
                results.add(new RecruiterCandidateResult(
                        file.getOriginalFilename(),
                        "REJECTED",
                        "Unable to read PDF. The file may be corrupted or invalid."
                ));
            }
        }





        results.sort(
                Comparator.comparing(
                        (RecruiterCandidateResult candidate) ->
                                candidate.getResumeResponse() == null
                ).thenComparing(
                        Comparator.comparingDouble(
                                (RecruiterCandidateResult candidate) ->
                                        candidate.getResumeResponse() == null
                                                ? 0.0
                                                : candidate.getResumeResponse()
                                                .getMatchPercentage()
                        ).reversed()
                )
        );

        int rank = 1;
        for (RecruiterCandidateResult candidate : results) {
            if (candidate.getResumeResponse() != null) {
                candidate.setRank(rank++);
            }
        }

        return results;
    }


    private boolean isLikelyResume(String text) {

        if (text == null || text.isBlank()) {
            return false;
        }

        String lowerText = text.toLowerCase();

        boolean hasEmail = Pattern.compile(
                "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}",
                Pattern.CASE_INSENSITIVE
        ).matcher(text).find();

        boolean hasPhone = Pattern.compile(
                "(?:\\+?\\d{1,3}[-.\\s]?)?(?:\\d[-.\\s]?){10}"
        ).matcher(text).find();

        boolean hasEducation =
                lowerText.contains("education")
                        || lowerText.contains("university")
                        || lowerText.contains("college");

        boolean hasProjects =
                lowerText.contains("projects")
                        || lowerText.contains("project");

        boolean hasSkills =
                lowerText.contains("skills")
                        || lowerText.contains("technical skills");

        boolean hasExperience =
                lowerText.contains("experience")
                        || lowerText.contains("internship");

        boolean hasObjective =
                lowerText.contains("objective")
                        || lowerText.contains("professional summary");

        int resumeSections = 0;

        if (hasEducation) resumeSections++;
        if (hasProjects) resumeSections++;
        if (hasSkills) resumeSections++;
        if (hasExperience) resumeSections++;
        if (hasObjective) resumeSections++;

        return (hasEmail || hasPhone) && resumeSections >= 1;
    }
}
