package com.spring.ai.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spring.ai.dto.Joke;
@SpringBootTest
public class ragtest {
	@Autowired
    private RagAi ragai;

    @Test
    public void askAI() {
    	var joke= ragai.askAI("What are vector stores in Spring AI?");
    	System.out.println(joke);

        
    }
    @Test
    public void testIngestDataToVectorStore() {
		
		ragai.ingestData();
		
		System.out.println("Document ingested into the vector store.");
	}


}
