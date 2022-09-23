package com.ameritas.viafilehandler.rabbitmq.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
class ViaFileHandlerProducerAppTests {

/*	@BeforeEach
	void setup(){
		MockitoAnnotations.initMocks(this);
	}*/

	String patt="^\\w+_[0-9]{8,}\\.[a-z]{3}$";
	String source="./src/test/resources/";
	String fileProcessed=null;
	Integer maxTime=5;
	@Test
	void testRabbitMq() throws Exception {
		Path dir = Paths.get(source);
		if (Files.isDirectory(dir)) {
			Optional<Path> opPath = Files.list(dir)
					.filter(p ->
							!Files.isDirectory(p))
					.min((p1, p2) ->
							Long.compare(p2.toFile().lastModified(), p1.toFile().lastModified()));

			if (opPath.isPresent()) {
				dir = opPath.get();
				boolean match = Pattern.matches(patt, dir.getFileName().toString());
				log.info("Value of pattern matching {} for {}", match, dir.getFileName().toString());

				if (!match)
					throw new InvalidFileNameException(dir.getFileName().toString(), "File name " + dir + " doesnt match expected pattern" + patt);
				if (fileProcessed != null && fileProcessed.equalsIgnoreCase(dir.getFileName().toString())) {
					BasicFileAttributes attr =
							Files.readAttributes(dir, BasicFileAttributes.class);

					log.info("lastModifiedTime: " + attr.lastModifiedTime());
					long fileTime = attr.lastModifiedTime().toInstant().atZone(ZoneOffset.UTC).toEpochSecond();
					long now = LocalDateTime.now(ZoneId.of("UTC")).toEpochSecond(ZoneOffset.UTC);
					long deltaMin = (now - fileTime) / 60;
					log.info("Last modified {} minutes back", deltaMin);

					if (deltaMin > maxTime) {
						throw new Exception("File " + dir.getFileName().getFileName() + " still exists at " + dir.getParent() + " after mins => " + maxTime);
					}
				}
				fileProcessed = dir.getFileName().toString();
				log.info("New file reading for processing {}",fileProcessed);
			}
		}
	}

}
