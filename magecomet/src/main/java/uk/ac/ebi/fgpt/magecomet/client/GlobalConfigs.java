package uk.ac.ebi.fgpt.magecomet.client;

import java.util.LinkedHashMap;

public class GlobalConfigs {
	public static boolean shouldExclude(String attribute){
		if(attribute.contains("Protocol REF") ||
		   attribute.contains("Extract Name") ||
		   attribute.contains("Labeled Extract Name")||
		   attribute.contains("Label")||
		   attribute.contains("Array Design REF")||
		   attribute.contains("ArrayExpress") ||
		   attribute.contains("URI")||
		   attribute.contains("FTP")||
		   attribute.contains("Matrix")
		){
			return true;
		}else
		return false;
	}
	public static LinkedHashMap<String, String> getCommonFactors(){
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("age", "age");
		valueMap.put("age at diagnosis", "age at diagnosis");
		valueMap.put("behavior", "behavior");
		valueMap.put("biosource provider", "biosource provider");
		valueMap.put("bmi", "bmi");
		valueMap.put("cell line", "cell line");
		valueMap.put("cell type", "cell type");
		valueMap.put("chip antibody", "chip antibody");
		valueMap.put("clinical history", "clinical history");
		valueMap.put("clinical information", "clinical information");
		valueMap.put("compound", "compound");
		valueMap.put("developmental stage", "developmental stage");
		valueMap.put("disease stage", "disease stage");
		valueMap.put("dose", "dose");
		valueMap.put("environmental history", "environmental history");
		valueMap.put("ethnicity", "ethnicity");
		valueMap.put("family history", "familiy history");
		valueMap.put("genetic modification", "genetic modification");
		valueMap.put("genotype", "genotype");
		valueMap.put("growth condition", "growth condition");
		valueMap.put("histology", "histology");
		valueMap.put("host", "host");
		valueMap.put("infection", "infectoin");
		valueMap.put("injury", "injury");
		valueMap.put("irradiate", "irradiate");
		valueMap.put("organism part", "organism part");
		valueMap.put("passage", "passage");
		valueMap.put("phenotype", "phenotype");
		valueMap.put("population", "population");
		valueMap.put("sex", "sex");
		valueMap.put("stimulus", "stimulus");
		valueMap.put("strain or line", "strain or line");
		valueMap.put("survival", "survival");
		valueMap.put("temperature", "temperature");
		valueMap.put("time", "time");
		
		return valueMap;
	}
	public static LinkedHashMap<String, String> getCommonCharacteristics(){
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("age", "age");
		valueMap.put("age at diagnosis", "age at diagnosis");
		valueMap.put("behavior", "behavior");
		valueMap.put("biosource provider", "biosource provider");
		valueMap.put("bmi", "bmi");
		valueMap.put("cell line", "cell line");
		valueMap.put("cell type", "cell type");
		valueMap.put("clinical history", "clinical history");
		valueMap.put("clinical information", "clinical information");
		valueMap.put("developmental stage", "developmental stage");
		valueMap.put("disease stage", "disease stage");
		valueMap.put("environmental history", "environmental history");
		valueMap.put("ethnicity", "ethnicity");
		valueMap.put("family history", "familiy history");
		valueMap.put("genetic modification", "genetic modification");
		valueMap.put("genotype", "genotype");
		valueMap.put("histology", "histology");
		valueMap.put("host", "host");
		valueMap.put("infection", "infectoin");
		valueMap.put("organism part", "organism part");
		valueMap.put("passage", "passage");
		valueMap.put("phenotype", "phenotype");
		valueMap.put("population", "population");
		valueMap.put("sex", "sex");
		valueMap.put("strain or line", "strain or line");
		valueMap.put("survival", "survival");
		return valueMap;
	}
	public static LinkedHashMap<String, String> getCommonFactorValueTypes(){
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("AGE", "AGE");
		valueMap.put("AGE_AT_DIAGNOSIS", "AGE_AT_DIAGNOSIS");
		
		return valueMap;
	}
	
}
