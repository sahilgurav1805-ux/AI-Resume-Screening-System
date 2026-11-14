
package com.sahil.resume_checker.controller;

import com.sahil.resume_checker.dto.RecruiterCandidateResult;
import com.sahil.resume_checker.service.RecruiterScreeningService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/recruiter")
public class RecruiterScreeningController {

    private final RecruiterScreeningService screeningService;

    public RecruiterScreeningController(
            RecruiterScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    @PostMapping(
            value = "/screen-resumes",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public List<RecruiterCandidateResult> screenResumes(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("requiredSkills") String requiredSkills)
            throws IOException {

        return screeningService.screenResumes(
                files,
                requiredSkills
        );
    }


}
