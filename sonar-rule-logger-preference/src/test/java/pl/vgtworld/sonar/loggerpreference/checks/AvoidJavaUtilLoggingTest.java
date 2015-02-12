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
		assertThat(message.getLine()).isEqualTo(9);
		assertThat(message.getDefaultMessage()).isEqualTo(AvoidJavaUtilLogging.MESSAGE);
	}

	@Test
	public void shouldNotRaiseAnIssueOnStarImportWhenLoggerIsNotUsed() {
		AvoidJavaUtilLogging check = new AvoidJavaUtilLogging();

		File file = new File("src/test/resources/JavaUtilLoggingStarImportWithoutLogger.txt");
		SourceFile sourceFile = JavaAstScanner.scanSingleFile(file, new VisitorsBridge(check));

		Set<CheckMessage> messages = sourceFile.getCheckMessages();

		assertThat(messages).hasSize(0);
	}

	@Test
	public void shouldRaiseAnIssueWhenLoggerWithFullPackageNameIsUsedInsteadOfImport() {
		AvoidJavaUtilLogging check = new AvoidJavaUtilLogging();

		File file = new File("src/test/resources/JavaUtilLoggingNoImport.txt");
		SourceFile sourceFile = JavaAstScanner.scanSingleFile(file, new VisitorsBridge(check));

		Set<CheckMessage> messages = sourceFile.getCheckMessages();
		CheckMessage message = messages.iterator().next();

		assertThat(messages).hasSize(1);
		assertThat(message.getLine()).isEqualTo(8);
		assertThat(message.getDefaultMessage()).isEqualTo(AvoidJavaUtilLogging.MESSAGE);
	}

	@Test
	public void shouldCleanResultsBeforeScanning() {
		AvoidJavaUtilLogging check = new AvoidJavaUtilLogging();

		File file1 = new File("src/test/resources/JavaUtilLoggingImport.txt");
		File file2 = new File("src/test/resources/JavaUtilLoggingStarImportWithoutLogger.txt");
		JavaAstScanner.scanSingleFile(file1, new VisitorsBridge(check));
		SourceFile sourceFile = JavaAstScanner.scanSingleFile(file2, new VisitorsBridge(check));

		Set<CheckMessage> messages = sourceFile.getCheckMessages();

		assertThat(messages).hasSize(0);
	}

	@Test
	public void shouldNotFailOnParsingCatchSection() {
		AvoidJavaUtilLogging check = new AvoidJavaUtilLogging();

		File file = new File("src/test/resources/JavaUtilLoggingCatchSection.txt");
		SourceFile sourceFile = JavaAstScanner.scanSingleFile(file, new VisitorsBridge(check));

		Set<CheckMessage> messages = sourceFile.getCheckMessages();

		assertThat(messages).hasSize(0);
	}

}
