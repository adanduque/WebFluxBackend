package com.adam.projectwebflux.config;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

@Configuration
public class ResourceWebPropertiesConfig {
    @Bean
    public WebProperties.Resources resources(){
        return  new WebProperties.Resources();
    }
   /* @Bean
    public ErrorAttributes errorAttributes(){
        return  new ErrorAttributes() {
            @Override
            public Throwable getError(ServerRequest request) {
                return null;
            }

            @Override
            public void storeErrorInformation(Throwable error, ServerWebExchange exchange) {

            }
        };
    }

    @Bean
    public ServerCodecConfigurer serverCodecConfigurer(){
        return  new ServerCodecConfigurer() {
            @Override
            public ServerDefaultCodecs defaultCodecs() {
                return null;
            }

            @Override
            public ServerCodecConfigurer clone() {
                return null;
            }

            @Override
            public CustomCodecs customCodecs() {
                return null;
            }

            @Override
            public void registerDefaults(boolean registerDefaults) {

            }

            @Override
            public List<HttpMessageReader<?>> getReaders() {
                return null;
            }

            @Override
            public List<HttpMessageWriter<?>> getWriters() {
                return null;
            }
        };
    }

*/
}
