package pl.vgtworld.sonar.loggerpreference.checks;

import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.ImportTree;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.VariableTree;
import pl.vgtworld.sonar.loggerpreference.Definition;

import java.util.Deque;
import java.util.LinkedList;

@Rule(
		key = AvoidJavaUtilLogging.KEY,
		name = AvoidJavaUtilLogging.MESSAGE,
		description = "Raise an issue, when Logger from java.util.logging is used in a class.",
		tags = {"logger"}
)
public class AvoidJavaUtilLogging extends BaseTreeVisitor implements JavaFileScanner {

	public static final String CLASS_NAME = "Logger";

	public static final String FORBIDDEN_IMPORT = "java.util.logging.Logger";

	public static final String FORBIDDEN_STAR_IMPORT = "java.util.logging.*";

	public static final String MESSAGE = "Avoid logger from java.util.logging package";

	public static final String KEY = "AvoidJavaUtilLogging";

	private static final RuleKey RULE_KEY = RuleKey.of(Definition.REPOSITORY_KEY, KEY);

	private JavaFileScannerContext context;

	private Tree forbiddenImportTree;

	private Tree forbiddenStarImportTree;

	private Tree loggerProperty;

	private Tree loggerPropertyFullName;

	@Override
	public void scanFile(JavaFileScannerContext javaFileScannerContext) {
		context = javaFileScannerContext;
		clearResults();
		scan(context.getTree());
		validateRule();
	}

	@Override
	public void visitVariable(VariableTree tree) {
		String parsedClassName = getFullClassNameAsString((ExpressionTree) tree.type());
		if (parsedClassName.equals(CLASS_NAME)) {
			loggerProperty = tree;
		}
		if (parsedClassName.equals(FORBIDDEN_IMPORT)) {
			loggerPropertyFullName = tree;
		}
		super.visitVariable(tree);
	}

	@Override
	public void visitImport(ImportTree tree) {
		String parsedImport = getFullClassNameAsString((ExpressionTree) tree.qualifiedIdentifier());

		if (parsedImport.equals(FORBIDDEN_IMPORT)) {
			forbiddenImportTree = tree;
		}
		if (parsedImport.equals(FORBIDDEN_STAR_IMPORT)) {
			forbiddenStarImportTree = tree;
		}

		super.visitImport(tree);
	}

	private void clearResults() {
		forbiddenImportTree = null;
		forbiddenStarImportTree = null;
		loggerProperty = null;
		loggerPropertyFullName = null;
	}

	private void validateRule() {
		if (forbiddenImportTree != null) {
			context.addIssue(forbiddenImportTree, RULE_KEY, MESSAGE);
		} else if (forbiddenStarImportTree != null && loggerProperty != null) {
			context.addIssue(forbiddenStarImportTree, RULE_KEY, MESSAGE);
		} else if (loggerPropertyFullName != null) {
			context.addIssue(loggerPropertyFullName, RULE_KEY, MESSAGE);
		}
	}

	private static String getFullClassNameAsString(ExpressionTree tree) {
		Deque<String> pieces = new LinkedList<>();

		ExpressionTree element = tree;
		while (element.is(Tree.Kind.MEMBER_SELECT)) {
			MemberSelectExpressionTree member = (MemberSelectExpressionTree)element;
			pieces.push(member.identifier().name());
			pieces.push(".");
			element = member.expression();
		}
		if (element.is(Tree.Kind.IDENTIFIER)) {
			IdentifierTree identifier = (IdentifierTree)element;
			pieces.push(identifier.name());
		}

		StringBuilder builder = new StringBuilder();
		for (String piece : pieces) {
			builder.append(piece);
		}

		return builder.toString();
	}

}
