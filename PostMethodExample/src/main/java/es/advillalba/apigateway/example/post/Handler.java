package es.advillalba.apigateway.example.post;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.StorageClass;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


public class Handler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    static final Logger logger = LogManager.getLogger(Handler.class);
    private static final String BUCKET = "advillalba-apigateway-example";
    private static final String REGION = "eu-west-1";
    AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
            .withRegion(REGION)
            .build();



    /**
     * Handles a Lambda Function request
     *
     * @param input   The Lambda Function input
     * @param context The Lambda execution environment context object.
     * @return The Lambda Function output
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        Response response = storePayload(input.getBody(), context.getAwsRequestId());
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(response.getHttpStatus())
                .withHeaders(response.getHeaders())
                .withBody(response.getBody());
    }

    private Response storePayload(String payload, String requestId) {
        Response response;
        String fileName = requestId + ".json";
        if (StringUtils.isBlank(payload)) {
            return new Response().error();
        }
        try {
            InputStream fileContent = new ByteArrayInputStream(payload.getBytes());
            PutObjectRequest s3Request = new PutObjectRequest(BUCKET, fileName, fileContent, null);
            s3Request.setStorageClass(StorageClass.ReducedRedundancy);
            s3Client.putObject(s3Request);

            response = new Response().success().body(s3Client.getUrl(BUCKET, fileName).toString());
        } catch (SdkClientException ex) {
            logger.error("Se ha producido un error {}", ex);
            response = new Response().error();
        }
        return response;
    }
}
