package org.edla.xtext.tptp.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.impl.ParserRuleImpl;
import org.eclipse.xtext.impl.RuleCallImpl;
import org.eclipse.xtext.impl.TerminalRuleImpl;
import org.eclipse.xtext.nodemodel.BidiTreeIterator;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.impl.AbstractNode;
import org.eclipse.xtext.nodemodel.impl.CompositeNode;
import org.eclipse.xtext.nodemodel.impl.CompositeNodeWithSemanticElement;
import org.eclipse.xtext.nodemodel.impl.HiddenLeafNode;
import org.eclipse.xtext.nodemodel.impl.LeafNode;
import org.eclipse.xtext.parser.IParseResult;
import org.eclipse.xtext.parser.IParser;
import org.edla.xtext.tptp.TptpRuntimeModule;
import org.edla.xtext.tptp.TptpStandaloneSetup;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class ASTRecover {

	@Inject
	private IParser parser;

	Injector injector = Guice.createInjector(new TptpRuntimeModule());

	public ASTRecover() {
		setupParser();
	}

	private void setupParser() {
		Injector injector = new TptpStandaloneSetup().createInjectorAndDoEMFRegistration();
		injector.injectMembers(this);
	}

	public IParseResult parse(String path) throws IOException {
		// TODO Auto-generated method stub
		File file = new File(path);
		Reader reader = new BufferedReader(new FileReader(file));

		IParseResult result = parser.parse(reader);

		if (result.hasSyntaxErrors()) {
			for (INode e : result.getSyntaxErrors()) {
				System.out.println("(" + path + ":" + e.getStartLine() + "): " + e.getText());
			}
			// throw new
			// ParseException("Provided input contains syntax errors.");
		}
		return result;
	}
	
	public String getRule(INode node) {

		if (node instanceof AbstractNode) {
			AbstractNode abstractNode = (AbstractNode) node;

			if (!(abstractNode instanceof CompositeNode)
					&& !(abstractNode instanceof CompositeNodeWithSemanticElement)
					&& !(abstractNode instanceof LeafNode)
					&& !(abstractNode instanceof HiddenLeafNode)) {

				if (abstractNode.getGrammarElement() instanceof ParserRuleImpl)
					return ((ParserRuleImpl) abstractNode.getGrammarElement())
							.getName();

				if (abstractNode.getGrammarElement() instanceof RuleCallImpl)
					return ((RuleCallImpl) abstractNode.getGrammarElement())
							.getRule().getName();

				if (abstractNode.getGrammarElement() instanceof TerminalRuleImpl)
					return ((TerminalRuleImpl) abstractNode.getGrammarElement())
							.getName();
			}
		}
		return "Nothing";
}

	public static void main(String[] args) {
		ASTRecover ast = new ASTRecover();
		String path = "/Users/hack/NOBACKUP/TPTP-v7.2.0/Problems/PUZ/PUZ063-2-test.p";
		try {
			IParseResult iParseResult = ast.parse(path);
			System.out.println(iParseResult.getRootNode().getText());
			INode root = iParseResult.getRootNode();
			BidiTreeIterator<INode> it = root.getAsTreeIterable().iterator();
			while (it.hasNext()) {
				System.out.println("=============================");
				INode node = it.next();
				System.out.println("node"+node.toString());
				System.out.println("INFO"+ast.getRule(node));
				System.out.println(node.getText());
				System.out.println(node.getGrammarElement());
				if (node instanceof HiddenLeafNode && node.getGrammarElement() instanceof TerminalRuleImpl) {
					System.out.println("TerminalRuleImpl");
				}
				if (node.getGrammarElement() != null && node.getGrammarElement() instanceof Keyword) {
					System.out.println("Keyword");
				}
				System.out.println("=============================");
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}
