package com.example.pacttestconsumer;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Oleg Bomko on 12/06/2018.
 */
public class ContactTest {

    @Rule
    public PactProviderRuleMk2 provider = new PactProviderRuleMk2("test_provider", "localhost", 8083, this);

    @Pact(provider = "test_provider", consumer = "test_consumer")
    public RequestResponsePact createPact(PactDslWithProvider pactDslWithProvider) {
        return pactDslWithProvider.given("Test Hello Provider Service")
                .uponReceiving("Request to provider")
                .path("/getHello")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body("Hello from pact test provider service")
                .toPact();
    }

    @Test
    @PactVerification
    public void runTest() {
        RestTemplate rest = new RestTemplate();
        String result = rest.getForObject(provider.getUrl() + "/getHello", String.class);
        assertThat(result, is("Hello from pact test provider service"));
    }

}
