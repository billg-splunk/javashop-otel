package com.splunk.otel.annotator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

/*
 *  Authored by Derek Desjardins
 *  ddesjardins@splunk.com
 *  December 2021
 */

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;



public class OpenTelemetryAnnotator {
	
	private static final String FILE_PATH = "../shop/";
	
	  // list all files from this path
    public static ArrayList<Path> listFiles(URI path) throws IOException {
    	
    	
    	ArrayList<Path> result = new ArrayList<Path> () ;
    	try (Stream<Path> paths = Files.walk(Paths.get(path))) {
    	    paths
    	    .filter(Files::isReadable)
    	    .map(Path::toFile)
    	    .filter(file->file.getName().endsWith(".java"))
    	    .forEach( file-> {
    	    	result.add(file.toPath());
    	    	System.out.println(file);
    	    });       
    	}catch (IOException ex){
    	    ex.printStackTrace();
    	}
    	return result;

    }
	
	public static void annotateCodebase(File projectDir) throws Exception {
		ArrayList<Path> files = listFiles(projectDir.toURI());
		
		// Iterating over collection 'c' using iterator
		   for (java.util.Iterator<Path> iter = files.iterator(); iter.hasNext(); ) {
			   annotateFile(iter.next().toAbsolutePath().toString());
		   }
		
	}
	
	public static void annotateFile(String sFileName) throws Exception {
		 	CompilationUnit cu = StaticJavaParser.parse(new FileInputStream(sFileName));
	        
	        LexicalPreservingPrinter.setup(cu);
	        
	        System.out.println(LexicalPreservingPrinter.print(cu));
	       
	        VoidVisitor<Void> otelAnnotationVisitor = new OtelAnnotationVisitor();
	        otelAnnotationVisitor.visit(cu, null);
	        
	        
	        // Add OpenTelemetry @WithSpan and @SpanAttribute Support via package import to file.
	        cu.addImport(new ImportDeclaration("io.opentelemetry.instrumentation.annotations.WithSpan",false, false));
	        cu.addImport(new ImportDeclaration("io.opentelemetry.instrumentation.annotations.SpanAttribute",false, false));
	        
	        System.out.println("  ");
	        System.out.println("------------------");
	        System.out.println("  ");
	        
	        String sResult = LexicalPreservingPrinter.print(cu);
	        System.out.println(sResult);
	        File file = new File(sFileName);
	        File rename = new File(sFileName + "OLD");
	        
	    
	        boolean flag = file.renameTo(rename);
	  
	    
	        if (!flag) {
	            throw new IOException("Creation of  .OLD file copy failed...");
	        }
	        
	        PrintWriter out = new PrintWriter(sFileName);
	        out.println(sResult);
	}
	
    public static void main(String[] args) throws Exception {
    	  File projectDir = new File(FILE_PATH);
    	  //listFiles(projectDir.toURI());
	     annotateCodebase(projectDir);
       
    }

    
    private static class OtelAnnotationVisitor extends VoidVisitorAdapter<Void> {

        @Override 
        public void visit(  MethodDeclaration  md, Void arg) {
        	super.visit(md, arg);
        	
        	if ( md.getBody().isPresent() ) {
        		md.addAnnotation("WithSpan");
        		NodeList<Parameter> params = md.getParameters();
        		params.forEach((param) -> {
                    param.addSingleMemberAnnotation("SpanAttribute", param.getNameAsString());
                });
        		
        	}
        }
    }
}
