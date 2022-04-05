import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=hc0CfwoM6Kc7gc22dyHv0lWqvFF1DANBIB7CX6r1");

        CloseableHttpResponse response = httpClient.execute(request);

        Nasa nassa = mapper.readValue(response.getEntity().getContent(), new TypeReference<Nasa>() {
        });

        //posts.forEach(System.out::println);

        String url = nassa.getUrl();
        System.out.println(url);

        HttpGet request2 = new HttpGet(url);
        CloseableHttpResponse response2 = httpClient.execute(request2);

        String[] urlArray = url.split("/");
        System.out.println(Arrays.toString(urlArray));
        String namePicters = urlArray[6];
        HttpEntity entity = response2.getEntity();

        if (entity != null) {

            try (FileOutputStream fos = new FileOutputStream(namePicters)) {

                entity.writeTo(fos);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
