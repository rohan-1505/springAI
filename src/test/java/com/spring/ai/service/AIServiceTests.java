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
    void testEmbeddings() {

        String text = "Hello, world!";

        var embeddings = aiService.getEmbeddings(text);

        System.out.println("Embedding size: " + embeddings.length);

        for (float value : embeddings) {
            System.out.print(value + " ");
        }

        System.out.println();
    }
}