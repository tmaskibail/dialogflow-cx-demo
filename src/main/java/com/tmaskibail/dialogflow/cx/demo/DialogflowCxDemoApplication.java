package com.tmaskibail.dialogflow.cx.demo;

import com.google.cloud.dialogflow.cx.v3beta1.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DialogflowCxDemoApplication implements CommandLineRunner {

    public static final Logger LOG = LoggerFactory.getLogger(DialogflowCxDemoApplication.class);

    private static final String LOCATION_ID = "europe-west2";
    private static final String PROJECT_ID = "xxxx";
    private static final String AGENT_ID = "xxxx";
    private static final String SESSION_ID = "1";
    private static final String LANGUAGE_CODE = "en-GB";

    public static void main(String[] args) {
        SpringApplication.run(DialogflowCxDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        SessionsSettings.Builder sessionsSettingsBuilder = SessionsSettings.newBuilder();
        sessionsSettingsBuilder.setEndpoint(LOCATION_ID + "-dialogflow.googleapis.com:443");
        SessionsSettings sessionsSettings = sessionsSettingsBuilder.build();

        TextInput.Builder textInput = TextInput.newBuilder().setText("Hello");

        try (var sessionsClient = SessionsClient.create(sessionsSettings)) {
            var session = SessionName.of(PROJECT_ID, LOCATION_ID, AGENT_ID, SESSION_ID);
            LOG.info("Session Path: {}", session.toString());

            var queryInput = QueryInput
                    .newBuilder()
                    .setText(textInput)
                    .setLanguageCode(LANGUAGE_CODE)
                    .build();

            DetectIntentRequest request = DetectIntentRequest.newBuilder()
                    .setSession(session.toString())
                    .setQueryInput(queryInput)
                    .build();

            DetectIntentResponse response = sessionsClient.detectIntent(request);
            // Display the query result.
            var queryResult = response.getQueryResult();
            LOG.info("Query Text : {}", queryResult.getText());
            LOG.info("Query Response : {}", queryResult.getResponseMessages(0));
            LOG.info("Detected Intent: {}", queryResult.getIntent().getDisplayName());
            LOG.info("confidence: {}", queryResult.getIntentDetectionConfidence());
        }
    }
}
