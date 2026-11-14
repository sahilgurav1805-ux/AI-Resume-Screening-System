package com.sahil.resume_checker.util;

public class SkillExtractorTest {
    public static void main(String[] args) {

        SkillExtractor extractor = new SkillExtractor() ;;

       // String resumeText = "I know JavaScript but have not learned Java.";
        String resumeText = "I have experience with JavaScript.";

        System.out.println(extractor.extractSkills(resumeText));
    }
}
