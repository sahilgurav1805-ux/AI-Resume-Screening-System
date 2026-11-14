package com.sahil.resume_checker.analyzer;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.ArrayList;
import java.util.List ;

import org.springframework.stereotype.Component ;

@Component
public class PdfFormattingAnalyzer {

    public FormattingReport analyze(InputStream inputStream) throws IOException {


        List<String> formattingWarnings = new ArrayList<>() ;

        try(PDDocument document = Loader.loadPDF(inputStream.readAllBytes())){

            int pageCount = document.getNumberOfPages();

            Set<Float> fontSizes = new TreeSet<>() ;
            Set<String> fontNames = new HashSet<>() ;

            PDFTextStripper stripper = new PDFTextStripper() {

                @Override
                protected void processTextPosition(TextPosition text){

                    fontSizes.add(
                            Math.round(text.getFontSizeInPt() * 10) / 10.0f
                    );

                    fontNames.add(text.getFont().getName());

                    super.processTextPosition(text);
                }
            };

            String extractedText = stripper.getText(document);
            if (extractedText.trim().length() < 50) {
                formattingWarnings.add(
                        "Very little text could be extracted from this PDF. "
                        + "It may be scanned or contain unreadable text."
                );
            }

            for (Float fontSize : fontSizes) {
                if (fontSize < 10.f) {
                    formattingWarnings.add(
                            "Some text uses a font size below 10 pt. "
                            + "Check whether it is comfortable to read."
                    );
                    break;
                }
            }

            Set<String > fontFamilies = new HashSet<>();

            for (String fontName : fontNames){

                String fontFamily = fontName.split(",")[0].trim();

                fontFamilies.add(fontFamily);
            }

            if (fontFamilies.size() > 3) {
                formattingWarnings.add(
                        "The resume uses more than 3 font families. "
                        + "Consider using fewer fonts for a consistent appearance."
                ) ;
            }

            String pageMessage;

            if (pageCount == 1) {
                pageMessage = "The resume contains 1 page.";
            } else if (pageCount == 2) {
                pageMessage = "The resume contains 2 pages.";

            } else {
                pageMessage ="The resume contains " +
                        pageCount +" pages. Review whether this length "
                        + "is appropriate for your experience.";
            }

            return new FormattingReport(
                    pageCount,
                    fontSizes,
                    fontNames ,
                    pageMessage,
                    formattingWarnings
            );
        }
    }

    public record FormattingReport (
            int pageCount,
            Set<Float> fontSizes,
            Set<String> fontNames ,
            String pageMessage,
            List<String> formattingWarnings
    ) {

    }



}
