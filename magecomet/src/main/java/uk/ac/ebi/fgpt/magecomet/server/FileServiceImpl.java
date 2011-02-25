package uk.ac.ebi.fgpt.magecomet.server;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import uk.ac.ebi.fgpt.magecomet.client.FileService;


public class FileServiceImpl extends RemoteServiceServlet implements FileService{
	@Override
	public String getURL(String experimentAccession,String tableAsAString) {
//		File file = new File(/tmp/);
		return "http:google.com";
	}
}
