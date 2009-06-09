package lombok.eclipse;

import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ConstructorDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Initializer;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.parser.Parser;

/**
 * Entry point for the Eclipse Parser patch that lets lombok modify the Abstract Syntax Tree as generated by
 * eclipse's parser implementations. This class is injected into the appropriate OSGi ClassLoader and can thus
 * use any classes that belong to org.eclipse.jdt.(apt.)core.
 * 
 * Note that, for any Method body, if Bit24 is set, the eclipse parser has been patched to never attempt to
 * (re)parse it. You should set Bit24 on any MethodDeclaration object you inject into the AST:
 * 
 * <code>methodDeclaration.bits |= 0x80000;</code>
 * 
 * @author rzwitserloot
 * @author rspilker
 */
public class TransformEclipseAST {
	/**
	 * This method is called immediately after eclipse finishes building a CompilationUnitDeclaration, which is
	 * the top-level AST node when eclipse parses a source file. The signature is 'magic' - you should not
	 * change it!
	 * 
	 * Eclipse's parsers often operate in diet mode, which means many parts of the AST have been left blank.
	 * Be ready to deal with just about anything being null, such as the Statement[] arrays of the Method AST nodes.
	 * 
	 * @param parser The eclipse parser object that generated the AST.
	 * @param ast The AST node belonging to the compilation unit (java speak for a single source file).
	 */
	public static void transform(Parser parser, CompilationUnitDeclaration ast) {
		if ( ast.types != null ) for ( TypeDeclaration type : ast.types ) {
			if ( type.fields != null ) for ( FieldDeclaration field : type.fields ) {
				if ( field.annotations != null ) for ( Annotation annotation : field.annotations ) {
					if ( annotation.type.toString().equals("Getter") ) {
						new HandleGetter_ecj().apply(annotation, type, field);
					}
				}
			}
		}
	}
	
	public static void transform(Parser parser, MethodDeclaration ast) {
		
	}
	
	public static void transform(Parser parser, ConstructorDeclaration ast) {
		
	}
	
	public static void transform(Parser parser, Initializer ast) {
		
	}
}
