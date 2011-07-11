package uk.ac.ebi.fgpt.magecomet.client.model;

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
	public static LinkedHashMap<String, String> getCommonFactorsOrChars(){
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
		valueMap.put("disease state", "disease state");
		valueMap.put("dose", "dose");
		valueMap.put("environmental history", "environmental history");
		valueMap.put("ethnicity", "ethnicity");
		valueMap.put("family history", "familiy history");
		valueMap.put("genetic modification", "genetic modification");
		valueMap.put("genotype", "genotype");
		valueMap.put("growth condition", "growth condition");
		valueMap.put("histology", "histology");
		valueMap.put("host", "host");
		valueMap.put("individual", "individual");
		valueMap.put("infection", "infection");
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
		valueMap.put("unit", "unit");
		
		return valueMap;
	}
	public static LinkedHashMap<String, String> getCommonFactorValueTypes(){
    LinkedHashMap<String,String> valueMap = new LinkedHashMap<String,String>();
    valueMap.put("AGE", "AGE");
    valueMap.put("AGEATDIAGNOSIS", "AGEATDIAGNOSIS");
    valueMap.put("BEHAVIOR", "BEHAVIOR");
    valueMap.put("BIOSOURCEPROVIDER", "BIOSOURCEPROVIDER");
    valueMap.put("BMI", "BMI");
    valueMap.put("CELLLINE", "CELLLINE");
    valueMap.put("CELLTYPE", "CELLTYPE");
    valueMap.put("CHIPANTIBODY", "CHIPANTIBODY");
    valueMap.put("CLINICALHISTORY", "CLINICALHISTORY");
    valueMap.put("CLINICALINFORMATION", "CLINICALINFORMATION");
    valueMap.put("COMPOUND", "COMPOUND");
    valueMap.put("DEVELOPMENTALSTAGE", "DEVELOPMENTALSTAGE");
    valueMap.put("DISEASESTAGE", "DISEASESTAGE");
    valueMap.put("DISEASESTATE", "DISEASESTATE");
    valueMap.put("DOSE", "DOSE");
    valueMap.put("ENVIRONMENTALHISTORY", "ENVIRONMENTALHISTORY");
    valueMap.put("ETHNICITY", "ETHNICITY");
    valueMap.put("FAMILYHISTORY", "FAMILIYHISTORY");
    valueMap.put("GENETICMODIFICATION", "GENETICMODIFICATION");
    valueMap.put("GENOTYPE", "GENOTYPE");
    valueMap.put("GROWTHCONDITION", "GROWTHCONDITION");
    valueMap.put("HISTOLOGY", "HISTOLOGY");
    valueMap.put("HOST", "HOST");
    valueMap.put("INDIVIDUAL", "INDIVIDUAL");
    valueMap.put("INFECTION", "INFECTION");
    valueMap.put("INJURY", "INJURY");
    valueMap.put("IRRADIATE", "IRRADIATE");
    valueMap.put("ORGANISMPART", "ORGANISMPART");
    valueMap.put("PASSAGE", "PASSAGE");
    valueMap.put("PHENOTYPE", "PHENOTYPE");
    valueMap.put("POPULATION", "POPULATION");
    valueMap.put("SEX", "SEX");
    valueMap.put("STIMULUS", "STIMULUS");
    valueMap.put("STRAINORLINE", "STRAINORLINE");
    valueMap.put("SURVIVAL", "SURVIVAL");
    valueMap.put("TEMPERATURE", "TEMPERATURE");
    valueMap.put("TIME", "TIME");
    valueMap.put("UNIT", "UNIT");
		
		return valueMap;
	}
	public static LinkedHashMap<String, String> getUnits() {
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("time", "time");
		valueMap.put("mass", "mass");
		valueMap.put("area", "area");
		valueMap.put("concentration", "concentration");
		valueMap.put("temperature", "temperature");
		valueMap.put("radiation", "radiation");
		valueMap.put("volume", "volume");		
		return valueMap;
	}
	
}
