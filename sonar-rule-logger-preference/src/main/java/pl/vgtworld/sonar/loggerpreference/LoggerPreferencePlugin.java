package pl.vgtworld.sonar.loggerpreference;

import org.sonar.api.SonarPlugin;

import java.util.Arrays;
import java.util.List;

public class LoggerPreferencePlugin extends SonarPlugin {

	@Override
	public List getExtensions() {
		return Arrays.asList(
				Definition.class,
				FileScannersFactory.class
		);
	}

}
