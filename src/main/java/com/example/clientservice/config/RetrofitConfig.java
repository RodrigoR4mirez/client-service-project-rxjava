package com.example.clientservice.config;

import com.example.clientservice.repository.ClientRepository;
import com.example.clientservice.repository.TokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * Configuración para el cliente Retrofit.
 */
@Configuration
@Slf4j
public class RetrofitConfig {

    @Value("${external.api.base-url}")
    private String baseUrl;

    @Value("${external.api.timeout:30}")
    private long timeout;

    @Bean
    public OkHttpClient okHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    // Headers estáticos que se aplicarán a todas las peticiones
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Content-Type", "application/json")
                            .header("Accept", "application/json")
                            .header("X-App-Version", "1.0.0");

                    return chain.proceed(requestBuilder.build());
                });

        if (log.isDebugEnabled()) {
            httpClient.addInterceptor(logging);
        }

        return httpClient.build();
    }

    @Bean
    public Retrofit retrofitClient(OkHttpClient okHttpClient) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    @Bean
    public ClientRepository clientRepository(Retrofit retrofit) {
        return retrofit.create(ClientRepository.class);
    }

    @Bean
    public TokenRepository tokenRepository(Retrofit retrofit) {
        return retrofit.create(TokenRepository.class);
    }
}