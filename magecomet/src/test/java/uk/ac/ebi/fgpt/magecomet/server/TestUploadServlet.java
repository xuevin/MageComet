package uk.ac.ebi.fgpt.magecomet.server;



//public class TestUploadServlet {
//	
//	@Test
//	public void testValidator(){
//		try {
//
//			File idf = new File(this.getClass().getClassLoader().getResource("E-GEOD-18781.idf.txt").getFile());
//			
//			if(idf.exists()){
//				System.out.println("File Exists");
//			}else{
//				fail("File can't be found");
//			}
//
//			// make a new parser, in read only mode
//			MAGETABParser parser = new MAGETABParser();
//
//			
//			// register error item listener
//			final List<ErrorItem> errorList = new ArrayList<ErrorItem>();
//
//			parser.addErrorItemListener(new ErrorItemListener() {
//				public void errorOccurred(ErrorItem item) {
//					errorList.add(item);
//				}
//			});
//			
//			// Create validataor 
//			Validator<MAGETABInvestigation> validator = new SemanticValidator(idf.getAbsolutePath());
//
//			// set validator on the parser
//			parser.setValidator(validator);
//
//			// do parse
//			System.out.println("Parsing " + idf.getAbsolutePath() + "...");
//
//			// need to get the url of this file, as the parser only takes urls
//
//			parser.parse(idf.toURI().toURL());
//			
//			
//			JSONArray errorArray= new JSONArray();
//			
//			int i =0;
//			for(ErrorItem error:errorList){
//				JSONObject errorItem = new JSONObject();
//				errorItem.put("code",error.getErrorCode()+"");
//				errorItem.put("type",error.getErrorType());
//				errorItem.put("message",error.getMesg());
//				errorItem.put("line",error.getLine()+"");
//				errorItem.put("column",error.getCol()+"");
//				errorArray.put(i,errorItem);
//				i++;
//			}
//			System.out.println(errorArray.toString());
//
//		} catch (ParseException e) {
//			// This happens if parsing failed.
//			// Any errors here will also have been reported by the listener
//			e.printStackTrace();
//			fail();
//		} catch (MalformedURLException e) {
//			// This is if the url from the file is bad
//			e.printStackTrace();
//			fail();
//		} catch (Exception e){
//			e.printStackTrace();
//			fail();
//		}
//	}
//}
