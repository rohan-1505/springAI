package com.spring.ai.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spring.ai.dto.Joke;

@SpringBootTest
public class AIServiceTests {

    @Autowired
    private AIservice aiService;
    

    @Test
    void testGetJoke() {

        Joke joke = aiService.getJoke("Java");

        System.out.println("AI Joke:");
        System.out.println(joke.getJoke());
    }
    @Test
    void checkKey() {

        String key = System.getenv("GROQ_API_KEY");

        System.out.println("Key exists: " + (key != null));
        System.out.println("Key length: " + 
            (key == null ? 0 : key.length()));
    }
    @Test
    void testEmbeddings() {

        String text = "Hello, world!";

        var embeddings = aiService.getEmbeddings(text);

        System.out.println("Embedding size: " + embeddings.length);

        for (float value : embeddings) {
            System.out.print(value + " ");
        }

        System.out.println();
    }
    
    @Test
    public void testIngestDataToVectorStore() {
		
		aiService.ingestMovieData();
		
		System.out.println("Document ingested into the vector store.");
	}
    
    @Test
    public void testSimilaritySearch() {
    			String query = "A movie about a thief who enters dreams.";
    			var response =aiService.similaritySearch(query);
    			System.out.println(response);
    }
}