package com.apu.studyinfoprotection;

import com.apu.studyinfoprotection.logging.LoggingOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication 
@ComponentScan
public class Application 
{
    private static final Logger logger = LogManager.getLogger(Application.class);

	public static void main(String[] args) throws UnsupportedEncodingException {
	    System.setOut(new PrintStream(new LoggingOutputStream(LogManager.getLogger("sysoutLog"), Level.ALL), true, StandardCharsets.UTF_8.toString()));
		SpringApplication.run(Application.class, args);
	}
}
