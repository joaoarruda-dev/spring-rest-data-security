package br.edu.fatecsjc.lgnspringapi.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource(properties = "server.port=8000")
public class RootResourceTest {

    @InjectMocks
    private RootResource rootResource;

    @Value("${server.port}")
    private String port;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidateRestResource() {
        String expectedMessage = new StringBuilder()
                .append("Bem-vindo, APIs operacionais. ")
                .append(String.format("Acesse: <a href=\"http://localhost:%s/swagger-ui/index.html\">", port))
                .append(String.format("http://localhost:%s/swagger-ui/index.html</a>", port))
                .toString();

        ResponseEntity<String> response = rootResource.validateRestResource();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
    }
}