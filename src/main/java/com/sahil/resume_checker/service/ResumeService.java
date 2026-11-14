package com.sahil.resume_checker.service;

import com.sahil.resume_checker.dto.ResumeRequest;
import com.sahil.resume_checker.dto.ResumeResponse;
import com.sahil.resume_checker.util.SkillExtractor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class ResumeService {



    private final SkillExtractor skillExtractor;

    public ResumeService(SkillExtractor skillExtractor) {
        this.skillExtractor = skillExtractor;
    }

    public ResumeResponse checkResume(ResumeRequest request) {

        if (request == null
                || request.getResumeText() == null
                || request.getRequiredSkills() == null) {

            throw new IllegalArgumentException(
                    "Resume text and required skills are required."
            );
        }

             if (request.getResumeText().isBlank() ||
            request.getRequiredSkills().isBlank()) {
                throw new IllegalArgumentException(
                        "Resume text and required skills cannot be blank."
                );

        }


        List<String> resumeSkills =
                skillExtractor.extractSkills(request.getResumeText());

        List<String> requiredSkills = Arrays.stream(
                        request.getRequiredSkills().split(","))
                .map(String::trim)
                .filter(skill -> !skill.isEmpty())
                .map(skill -> skill.toLowerCase(Locale.ROOT))
                .distinct()
                .collect(Collectors.toList());


        if (resumeSkills.isEmpty() || requiredSkills.isEmpty()) {
            throw new IllegalArgumentException(
                    "The resume must contain known skills, and required skills cannot be empty."
            );
        }

        List<String> normalizedResumeSkills = resumeSkills.stream()
                .map(skill -> skill.toLowerCase(Locale.ROOT))
                .collect(Collectors.toList());

        List<String> matchedSkills = requiredSkills.stream()
                .filter(normalizedResumeSkills::contains)
                .collect(Collectors.toList());

        List<String> missingSkills = requiredSkills.stream()
                .filter(skill -> !normalizedResumeSkills.contains(skill))
                .collect(Collectors.toList());


        double matchPercentage =
                (matchedSkills.size() * 100.0) / requiredSkills.size();


        return new ResumeResponse(
                matchedSkills,
                missingSkills,
                matchPercentage
        );
       }


}