package pl.vgtworld.sonar.loggerpreference;

import org.sonar.api.BatchExtension;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionAnnotationLoader;

public class Definition implements RulesDefinition, BatchExtension {

	public static final String REPOSITORY_KEY = "vgt-rules";

	@Override
	public void define(Context context) {
		NewRepository repo = context.createRepository(REPOSITORY_KEY, "java");
		repo.setName("VGT rules");

		RulesDefinitionAnnotationLoader annotationLoader = new RulesDefinitionAnnotationLoader();
		annotationLoader.load(repo, FileScannersFactory.checkClasses());
		repo.done();
	}

}
