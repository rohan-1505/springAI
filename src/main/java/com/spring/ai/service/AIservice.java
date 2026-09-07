package com.spring.ai.service;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import com.spring.ai.dto.Joke;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AIservice {

    private final ChatClient chatClient;
    private final org.springframework.ai.embedding.EmbeddingModel em;;	
    
    public float[] getEmbeddings(String text) {
		return em.embed(text);
	}

    public Joke getJoke(String topic) {

        String systemPrompt = """
                You are a sarcastic and funny AI.

                You will respond to the user's request for a joke about {topic}.

                Your responses should be humorous, witty, and sarcastic,
                while still being appropriate and respectful.

                Avoid using offensive language or making jokes that could
                be considered insensitive.

                Your goal is to make the user laugh while maintaining
                a lighthearted and playful tone.
                """;

        PromptTemplate promptTemplate = new PromptTemplate(systemPrompt);

        String renderedText = promptTemplate.render(
                Map.of("topic", topic)
        );

        Joke response = chatClient.prompt()
                .system(renderedText)
                .advisors(new SimpleLoggerAdvisor())
                .call()
                .entity(Joke.class);

        return response;
    }
}