package es.advillalba.apigateway.example.get;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Handler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    static final Logger logger = LogManager.getLogger(Handler.class);

    /**
     * Handles a Lambda Function request
     *
     * @param input   The Lambda Function input
     * @param context The Lambda execution environment context object.
     * @return The Lambda Function output
     */
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        Response response = generateResponse(input);
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(response.getHttpStatus())
                .withHeaders(response.getHeaders())
                .withBody(response.getBody());
    }

    Response generateResponse(APIGatewayProxyRequestEvent input) {
        Response response;
        Result output = new Result();
        if (input.getPathParameters() != null) {
            output.setPathParam(input.getPathParameters().getOrDefault("id", "No se ha informado pathParam"));
        }
        output.setQueryParams(input.getQueryStringParameters());
        String jsonOutput;

        try {
            jsonOutput = OBJECT_MAPPER.writeValueAsString(output);
            response = new Response().success().withPayload(jsonOutput);
        } catch (JsonProcessingException e) {
            response = new Response().error();
            logger.error("Error al deserializar, {}", e);
        }
        return response;
    }
}
