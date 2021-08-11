package io.github.usalko.tus.test;

import me.desair.tus.server.TusFileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;

@SpringBootApplication
public class TusServerTestApplication implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOG = LoggerFactory.getLogger(TusServerTestApplication.class);

	@Value("${spring.profiles.active}")
	protected String springProfilesActive;

	@Value("${tus.server.data.directory}")
	protected String tusDataPath;

	@Value("#{servletContext.contextPath}") // https://stackoverflow.com/questions/57511424/difference-between-and
	private String servletContextPath;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		LOG.info("=======================================");
		LOG.info("App running with active profiles: {}", springProfilesActive);
		LOG.info("=======================================");
	}

	@Bean
	public TusFileUploadService tusFileUploadService() {
		return new TusFileUploadService()
				.withStoragePath(tusDataPath)
				.withDownloadFeature()
				.withUploadURI(servletContextPath + "/videos/0[xX][0-9a-fA-F]+/upload")
				.withThreadLocalCache(true);
	}

	public static void main(String[] args) {
		SpringApplication.run(TusServerTestApplication.class, args);
	}

}
