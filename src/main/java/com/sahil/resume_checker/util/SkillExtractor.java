package com.sahil.resume_checker.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@Component
public class SkillExtractor {

    private static final List<String> KNOWN_SKILLS = List.of(
            "Java",
            "Spring Boot",
            "MySQL",
            "MongoDB",
            "Python",
            "JavaScript",
            "HTML",
            "CSS",
            "React",
            "Node.js",
            "REST API",
            "Git",
            "Docker"
    ) ;

    public List<String> extractSkills(String resumeText){

        List<String> extractSkills = new ArrayList<>() ;
        if (resumeText == null || resumeText.isBlank()){
            return extractSkills ;
        }

        String text = resumeText.toLowerCase(Locale.ROOT);

        for (String skill : KNOWN_SKILLS) {
            String pattern = "\\b" + java.util.regex.Pattern.quote(
                    skill.toLowerCase(Locale.ROOT)
            ) + "\\b";

            if (java.util.regex.Pattern.compile(pattern).matcher(text).find()) {
                extractSkills.add(skill);
            }
        }
        return extractSkills ;






    }
}
