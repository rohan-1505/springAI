package com.spring.ai.service;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import com.spring.ai.dto.Joke;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AIservice {

    private final ChatClient chatClient;
    private final org.springframework.ai.embedding.EmbeddingModel em;
    private final VectorStore vectorStore;
    
    public float[] getEmbeddings(String text) {
		return em.embed(text);
	}
    
    
    public void ingestMovieData() {

        Document movie1 = new Document(
            "Interstellar is a science fiction movie about astronauts "
            + "who travel through a wormhole to find a new home for humanity.",
            Map.of(
                "title", "Interstellar",
                "genre", "Science Fiction",
                "director", "Christopher Nolan",
                "year", 2014
            )
        );

        Document movie2 = new Document(
            "Inception is a science fiction thriller about a skilled thief "
            + "who enters people's dreams to steal and plant information.",
            Map.of(
                "title", "Inception",
                "genre", "Science Fiction",
                "director", "Christopher Nolan",
                "year", 2010
            )
        );

        Document movie3 = new Document(
            "The Dark Knight is a superhero crime movie where Batman "
            + "faces the Joker, a criminal who wants to create chaos in Gotham City.",
            Map.of(
                "title", "The Dark Knight",
                "genre", "Superhero",
                "director", "Christopher Nolan",
                "year", 2008
            )
        );

        vectorStore.add(List.of(movie1, movie2, movie3));
    }
    
    public List<Document> similaritySearch(String text) {
		return vectorStore.similaritySearch(SearchRequest.builder()
				.query(text)
				.topK(2)
				.build());
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