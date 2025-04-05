package com.example.clientservice.config;

import com.example.clientservice.repository.ClientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
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
    public Retrofit retrofitClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS);

        // En producción, el nivel de logging debe ser menor
        if (log.isDebugEnabled()) {
            httpClient.addInterceptor(logging);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient.build())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    /**
     * Bean que crea la implementación de ClientRepository utilizando Retrofit.
     *
     * @param retrofit La instancia de Retrofit configurada
     * @return La implementación de ClientRepository
     */
    @Bean
    public ClientRepository clientRepository(Retrofit retrofit) {
        return retrofit.create(ClientRepository.class);
    }
}