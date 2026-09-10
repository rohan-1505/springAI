package com.spring.ai.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest

public class RagTestPDF {
	
	@Autowired
	private RagPdf ragPdf;

	@Test
	public void testIngestPDFToVectorStore() {
		ragPdf.ingestPdfToVectorStore();

		System.out.println("PDF ingested into the vector store.");
	}
	
	 @Test
	    public void askAI() {
	    	var joke= ragPdf.askAI("how to connect to discord account");
	    	System.out.println(joke);

	        
	    }

}
