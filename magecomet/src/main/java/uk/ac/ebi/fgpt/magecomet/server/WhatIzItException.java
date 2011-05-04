package uk.ac.ebi.fgpt.magecomet.server;

public class WhatIzItException extends Exception {
	public WhatIzItException(){
		super();
	}
	public WhatIzItException(String message){
		super(message);
	}
	public WhatIzItException(Throwable throwable){
		super(throwable);
	}
	public WhatIzItException(String message, Throwable throwable){
		super(message, throwable);
	}
	
}
