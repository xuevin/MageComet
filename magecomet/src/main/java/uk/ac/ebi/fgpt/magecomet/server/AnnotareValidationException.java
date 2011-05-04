package uk.ac.ebi.fgpt.magecomet.server;

public class AnnotareValidationException extends Exception {
	public AnnotareValidationException(){
		super();
	}
	public AnnotareValidationException(String message){
		super(message);
	}
	public AnnotareValidationException(Throwable throwable){
		super(throwable);
	}
	public AnnotareValidationException(String message, Throwable throwable){
		super(message, throwable);
	}
	
}
