package com.example.clientservice.config;

import com.example.clientservice.exception.TimeoutExceptionHandler;
import com.example.clientservice.repository.ClientRepository;
import com.example.clientservice.repository.TokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Configuración para el cliente Retrofit.
 */
@Configuration
@Slf4j
public class RetrofitConfig {

    @Value("${external.api.base-url}")
    private String baseUrl;

    @Value("${external.api.connect-timeout:10}")
    private long connectTimeout;

    @Value("${external.api.read-timeout:30}")
    private long readTimeout;

    @Value("${external.api.write-timeout:30}")
    private long writeTimeout;

    @Bean
    public OkHttpClient okHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message ->
                log.debug("HTTP Request/Response: {}", message)
        );
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .addInterceptor(timeoutInterceptor())
                .addInterceptor(headerInterceptor())
                .addInterceptor(loggingInterceptor)
                .build();  // Eliminado el .eventListener()
    }

    private Interceptor timeoutInterceptor() {
        return chain -> {
            try {
                return chain.proceed(chain.request());
            } catch (IOException e) {
                throw new TimeoutExceptionHandler("API call timed out", e);
            }
        };
    }

    private Interceptor headerInterceptor() {
        return chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("X-App-Version", "1.0.0");

            return chain.proceed(requestBuilder.build());
        };
    }

    @Bean
    public Retrofit retrofitClient(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }
}