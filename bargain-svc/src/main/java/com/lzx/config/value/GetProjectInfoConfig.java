package com.lzx.config.value;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GetProjectInfoConfig {
    public static String AUTHOR;

    @Value("${author}")
    public void setAuthor(String author) {
        AUTHOR = author;
    }

}
