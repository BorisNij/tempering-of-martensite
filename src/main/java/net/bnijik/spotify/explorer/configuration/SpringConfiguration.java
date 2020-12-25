package net.bnijik.spotify.explorer.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.net.http.HttpClient;

@Configuration
@ComponentScan("net.bnijik.spotify.explorer")
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:application.yml")
public class SpringConfiguration {

    @Bean
    public HttpClient.Builder getHttpClientBuilder() {
        return HttpClient.newBuilder();
    }
}
