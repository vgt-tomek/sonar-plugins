package pl.vgtworld.sonar.loggerpreference.checks;

import org.junit.Test;
import org.sonar.java.JavaAstScanner;
import org.sonar.java.model.VisitorsBridge;
import org.sonar.squidbridge.api.CheckMessage;
import org.sonar.squidbridge.api.SourceFile;

import java.io.File;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class AvoidJavaUtilLoggingTest {

	@Test
	public void shouldRaiseAnIssueOnImport() {
		AvoidJavaUtilLogging check = new AvoidJavaUtilLogging();

		File file = new File("src/test/resources/JavaUtilLoggingImport.txt");
		SourceFile sourceFile = JavaAstScanner.scanSingleFile(file, new VisitorsBridge(check));

		Set<CheckMessage> messages = sourceFile.getCheckMessages();
		CheckMessage message = messages.iterator().next();

		assertThat(messages).hasSize(1);
		assertThat(message.getLine()).isEqualTo(4);
		assertThat(message.getDefaultMessage()).isEqualTo(AvoidJavaUtilLogging.MESSAGE);
	}

	@Test
	public void shouldRaiseAnIssueOnStarImport() {
		AvoidJavaUtilLogging check = new AvoidJavaUtilLogging();

		File file = new File("src/test/resources/JavaUtilLoggingStarImport.txt");
		SourceFile sourceFile = JavaAstScanner.scanSingleFile(file, new VisitorsBridge(check));

		Set<CheckMessage> messages = sourceFile.getCheckMessages();
		CheckMessage message = messages.iterator().next();

		assertThat(messages).hasSize(1);
		assertThat(message.getLine()).isEqualTo(4);
		assertThat(message.getDefaultMessage()).isEqualTo(AvoidJavaUtilLogging.MESSAGE);
	}

}
