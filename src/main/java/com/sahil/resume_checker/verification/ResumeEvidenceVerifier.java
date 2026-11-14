package com.sahil.resume_checker.verification;

import org.springframework.stereotype.Component;
import java.util.Locale;

@Component
public class ResumeEvidenceVerifier {

    public boolean containsEvidence(String resumeText, String evidence) {

        if (resumeText == null || evidence == null) {
            return false;
        }

        if (resumeText.isBlank() || evidence.isBlank()) {
            return false;
        }

        String normalizedResume = normalize(resumeText);
        String normalizedEvidence = normalize(evidence);

        return normalizedResume.contains(normalizedEvidence);
    }

    private String normalize(String text) {
        return text.toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ")
                .trim();
    }

    public boolean isEvidenceVerified(String resumeText , String evidence) {
        return containsEvidence(resumeText, evidence) ;
    }
}
