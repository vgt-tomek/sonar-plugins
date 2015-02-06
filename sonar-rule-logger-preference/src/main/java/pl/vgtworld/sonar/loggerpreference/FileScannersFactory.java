package pl.vgtworld.sonar.loggerpreference;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannersFactory;
import pl.vgtworld.sonar.loggerpreference.checks.AvoidJavaUtilLogging;

public class FileScannersFactory implements JavaFileScannersFactory, BatchExtension {

	private final CheckFactory checkFactory;

	public FileScannersFactory(CheckFactory checkFactory) {
		this.checkFactory = checkFactory;
	}

	@Override
	public Iterable<JavaFileScanner> createJavaFileScanners() {
		Checks<JavaFileScanner> checks = checkFactory.create(Definition.REPOSITORY_KEY);
		checks.addAnnotatedChecks(checkClasses());
		return checks.all();
	}

	public static Class[] checkClasses() {
		return new Class[] {
				AvoidJavaUtilLogging.class
		};
	}

}
