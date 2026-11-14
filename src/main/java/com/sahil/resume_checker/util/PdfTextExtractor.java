package com.sahil.resume_checker.util;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class PdfTextExtractor {

    public String extractText(InputStream inputStream) throws IOException {
        try(PDDocument document = Loader.loadPDF(inputStream.readAllBytes())){
            PDFTextStripper textStripper = new PDFTextStripper() ;
            return  textStripper.getText(document) ;
        }
    }
}
