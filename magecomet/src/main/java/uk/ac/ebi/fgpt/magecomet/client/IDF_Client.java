package uk.ac.ebi.fgpt.magecomet.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

public class IDF_Client implements Serializable{
	private String dateOfExperiment;
	private String experimentDescription;
	private String investigationTitle;
	private String magetabVersion;
	private String publicReleaseDate;
	private List<String> experimentalDesign;
	private List<String> experimentalDesignTermAccession;
	private List<String> experimentalDesignTermSourceREF;
	private List<String> experimentalFactorName;
	private List<String> experimentalFactorTermAccession;
	private List<String> experimentalFactorTermSourceREF;
	private List<String> experimentalFactorType;
	private List<String> normalizationTermAccession;
	private List<String> normalizationTermSourceREF;
	private List<String> normalizationType;
	private List<String> personAddress;
	private List<String> personAffiliation;
	private List<String> personEmail;
	private List<String> personFax;
	private List<String> personFirstName;
	private List<String> personLastName;
	private List<String> personMidInitials;
	private List<String> personPhone;
	private List<String> personRoles;
	private List<String> personRolesTermAccession;
	private List<String> personRolesTermSourceREF;
	private List<String> protocolContact;
	private List<String> protocolDescription;
	private List<String> protocolHardware;
	private List<String> protocolName;
	private List<String> protocolParameters;
	private List<String> protocolSoftware;
	private List<String> protocolTermAccession;
	private List<String> protocolTermSourceREF;
	private List<String> protocolType;
	private List<String> publicationAuthorList;
	private List<String> publicationDOI;
	private List<String> publicationStatus;
	private List<String> publicationStatusTermAccession;
	private List<String> publicationStatusTermSourceREF;
	private List<String> publicationTitle;
	private List<String> pubMedId;
	private List<String> qualityControlTermAccession;
	private List<String> qualityControlTermSourceREF;
	private List<String> qualityControlType;
	private List<String> replicateTermAccession;
	private List<String> replicateTermSourceREF;
	private List<String> replicateType;
	private List<String> sdrfFile;
	private List<String> termSourceFile;
	private List<String> termSourceName;
	private List<String> termSourceVersion;
	private Map<String,Set<String>> comments;
	
	
	public IDF_Client(JSONObject jsonObject) {
		setDateOfExperiment(jsonObject.get("dateOfExperiment").isString().stringValue());
		setExperimentDescription(jsonObject.get("experimentDescription").isString().stringValue());
		setInvestigationTitle(jsonObject.get("investigationTitle").isString().stringValue());
		setMagetabVersion(jsonObject.get("magetabVersion").isString().stringValue());
		setPublicReleaseDate(jsonObject.get("publicReleaseDate").isString().stringValue());
		setExperimentalDesign(jsonObject.get("experimentalDesign").isArray());
		setExperimentalDesignTermAccession(jsonObject.get("experimentalDesignTermAccession").isArray());
		setExperimentalDesignTermSourceREF(jsonObject.get("experimentalDesignTermSourceREF").isArray());
		setExperimentalFactorName(jsonObject.get("experimentalFactorName").isArray());
		setExperimentalFactorTermAccession(jsonObject.get("experimentalFactorTermAccession").isArray());
		setExperimentalFactorTermSourceREF(jsonObject.get("experimentalFactorTermSourceREF").isArray());
		setExperimentalFactorType(jsonObject.get("experimentalFactorType").isArray());
		setNormalizationTermAccession(jsonObject.get("normalizationTermAccession").isArray());
		setNormalizationTermSourceREF(jsonObject.get("normalizationTermSourceREF").isArray());
		setNormalizationType(jsonObject.get("normalizationType").isArray());
		setPersonAddress(jsonObject.get("personAddress").isArray());
		setPersonAffiliation(jsonObject.get("personAffiliation").isArray());
		setPersonEmail(jsonObject.get("personEmail").isArray());
		setPersonFax(jsonObject.get("personFax").isArray());
		setPersonFirstName(jsonObject.get("personFirstName").isArray());
		setPersonLastName(jsonObject.get("personLastName").isArray());
		setPersonMidInitials(jsonObject.get("personMidInitials").isArray());
		setPersonPhone(jsonObject.get("personPhone").isArray());
		setPersonRoles(jsonObject.get("personRoles").isArray());
		setPersonRolesTermAccession(jsonObject.get("personRolesTermAccession").isArray());
		setPersonRolesTermSourceREF(jsonObject.get("personRolesTermSourceREF").isArray());
		setProtocolContact(jsonObject.get("protocolContact").isArray());
		setProtocolDescription(jsonObject.get("protocolDescription").isArray());
		setProtocolHardware(jsonObject.get("protocolHardware").isArray());
		setProtocolName(jsonObject.get("protocolName").isArray());
		setProtocolParameters(jsonObject.get("protocolParameters").isArray());
		setProtocolSoftware(jsonObject.get("protocolSoftware").isArray());
		setProtocolTermAccession(jsonObject.get("protocolTermAccession").isArray());
		setProtocolTermSourceREF(jsonObject.get("protocolTermSourceREF").isArray());
		setProtocolType(jsonObject.get("protocolType").isArray());
		setPublicationAuthorList(jsonObject.get("publicationAuthorList").isArray());
		setPublicationDOI(jsonObject.get("publicationDOI").isArray());
		setPublicationStatus(jsonObject.get("publicationStatus").isArray());
		setPublicationStatusTermAccession(jsonObject.get("publicationStatusTermAccession").isArray());
		setPublicationStatusTermSourceREF(jsonObject.get("publicationStatusTermSourceREF").isArray());
		setPublicationTitle(jsonObject.get("publicationTitle").isArray());
		setPubMedId(jsonObject.get("pubMedId").isArray());
		setQualityControlTermAccession(jsonObject.get("qualityControlTermAccession").isArray());
		setQualityControlTermSourceREF(jsonObject.get("qualityControlTermSourceREF").isArray());
		setQualityControlType(jsonObject.get("qualityControlType").isArray());
		setReplicateTermAccession(jsonObject.get("replicateTermAccession").isArray());
		setReplicateTermSourceREF(jsonObject.get("replicateTermSourceREF").isArray());
		setReplicateType(jsonObject.get("replicateType").isArray());
		setSdrfFile(jsonObject.get("sdrfFile").isArray());
		setTermSourceFile(jsonObject.get("termSourceFile").isArray());
		setTermSourceName(jsonObject.get("termSourceName").isArray());
		setTermSourceVersion(jsonObject.get("termSourceVersion").isArray());
		setComments(jsonObject.get("commentsMap").isArray());
		
		
	}
	private void setComments(JSONArray array) {
		comments = new HashMap<String, Set<String>>();
		System.out.println(array.toString());
		for(int row = 0; row < array.size(); row++){
			JSONArray rowArray = array.get(row).isArray();
			Set<String> columnSet = new HashSet<String>();
			for(int column = 1;column<rowArray.size();column++){		
				String value =rowArray.get(column).isString().stringValue(); 
				columnSet.add(value);
			}
			
			String fieldName = rowArray.get(0).isString().stringValue();
			fieldName = ("Comment [" + fieldName + "]");
			comments.put(fieldName,columnSet);
		}
	}
	private void setTermSourceVersion(JSONArray array) {
		termSourceVersion = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			termSourceVersion.add(array.get(i).isString().stringValue());
		}		
	}
	private void setTermSourceName(JSONArray array) {
		termSourceName = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			termSourceName.add(array.get(i).isString().stringValue());
		}		
	}
	private void setTermSourceFile(JSONArray array) {
		termSourceFile = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			termSourceFile.add(array.get(i).isString().stringValue());
		}				
	}
	private void setSdrfFile(JSONArray array) {
		sdrfFile = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			sdrfFile.add(array.get(i).isString().stringValue());
		}		
	}
	private void setReplicateType(JSONArray array) {
		replicateType = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			replicateType.add(array.get(i).isString().stringValue());
		}				
	}
	private void setReplicateTermSourceREF(JSONArray array) {
		replicateTermSourceREF = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			replicateTermSourceREF.add(array.get(i).isString().stringValue());
		}				
	}
	private void setReplicateTermAccession(JSONArray array) {
		replicateTermAccession = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			replicateTermAccession.add(array.get(i).isString().stringValue());
		}		
	}
	private void setQualityControlType(JSONArray array) {
		qualityControlType = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			qualityControlType.add(array.get(i).isString().stringValue());
		}			
	}
	private void setQualityControlTermSourceREF(JSONArray array) {
		qualityControlTermSourceREF = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			qualityControlTermSourceREF.add(array.get(i).isString().stringValue());
		}				
	}
	private void setQualityControlTermAccession(JSONArray array) {
		qualityControlTermAccession = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			qualityControlTermAccession.add(array.get(i).isString().stringValue());
		}			
	}
	private void setPubMedId(JSONArray array) {
		pubMedId = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			pubMedId.add(array.get(i).isString().stringValue());
		}				
	}
	private void setPublicationTitle(JSONArray array) {
		publicationTitle = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			publicationTitle.add(array.get(i).isString().stringValue());
		}				
	}
	private void setPublicationStatusTermSourceREF(JSONArray array) {
		publicationStatusTermSourceREF = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			publicationStatusTermSourceREF.add(array.get(i).isString().stringValue());
		}				
	}
	private void setPublicationStatusTermAccession(JSONArray array) {
		publicationStatusTermAccession = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			publicationStatusTermAccession.add(array.get(i).isString().stringValue());
		}					
	}
	private void setPublicationStatus(JSONArray array) {
		publicationStatus = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			publicationStatus.add(array.get(i).isString().stringValue());
		}			
	}
	private void setPublicationDOI(JSONArray array) {
		publicationDOI = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			publicationDOI.add(array.get(i).isString().stringValue());
		}			
	}
	private void setPublicationAuthorList(JSONArray array) {
		publicationAuthorList = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			publicationAuthorList.add(array.get(i).isString().stringValue());
		}			
	}
	private void setProtocolType(JSONArray array) {
		protocolType = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			protocolType.add(array.get(i).isString().stringValue());
		}			
	}
	private void setProtocolTermSourceREF(JSONArray array) {
		protocolTermSourceREF = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			protocolTermSourceREF.add(array.get(i).isString().stringValue());
		}				
	}
	private void setProtocolTermAccession(JSONArray array) {
		protocolTermAccession = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			protocolTermAccession.add(array.get(i).isString().stringValue());
		}			
	}
	private void setProtocolSoftware(JSONArray array) {
		protocolSoftware = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			protocolSoftware.add(array.get(i).isString().stringValue());
		}				
	}
	private void setProtocolParameters(JSONArray array) {
		protocolParameters = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			protocolParameters.add(array.get(i).isString().stringValue());
		}		
	}
	private void setProtocolName(JSONArray array) {
		protocolName = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			protocolName.add(array.get(i).isString().stringValue());
		}				
	}
	private void setProtocolHardware(JSONArray array) {
		protocolHardware = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			protocolHardware.add(array.get(i).isString().stringValue());
		}				
	}
	private void setProtocolDescription(JSONArray array) {
		protocolDescription = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			protocolDescription.add(array.get(i).isString().stringValue());
		}			
	}
	private void setProtocolContact(JSONArray array) {
		protocolContact = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			protocolContact.add(array.get(i).isString().stringValue());
		}			
	}
	private void setPersonRolesTermSourceREF(JSONArray array) {
		personRolesTermSourceREF = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			personRolesTermSourceREF.add(array.get(i).isString().stringValue());
		}		
	}
	private void setPersonRolesTermAccession(JSONArray array) {
		personRolesTermAccession = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			personRolesTermAccession.add(array.get(i).isString().stringValue());
		}		
	}
	private void setPersonRoles(JSONArray array) {
		personRoles = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			personRoles.add(array.get(i).isString().stringValue());
		}			
	}
	private void setPersonPhone(JSONArray array) {
		personPhone = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			personPhone.add(array.get(i).isString().stringValue());
		}				
	}
	private void setPersonMidInitials(JSONArray array) {
		personMidInitials = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			personMidInitials.add(array.get(i).isString().stringValue());
		}		
	}
	private void setPersonLastName(JSONArray array) {
		personLastName = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			personLastName.add(array.get(i).isString().stringValue());
		}	
	}
	private void setPersonFirstName(JSONArray array) {
		personFirstName = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			personFirstName.add(array.get(i).isString().stringValue());
		}		
	}
	private void setPersonFax(JSONArray array) {
		personFax = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			personFax.add(array.get(i).isString().stringValue());
		}			
	}
	private void setPersonEmail(JSONArray array) {
		personEmail = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			personEmail.add(array.get(i).isString().stringValue());
		}	
		
	}
	private void setPersonAffiliation(JSONArray array) {
		personAffiliation = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			personAffiliation.add(array.get(i).isString().stringValue());
		}			
	}
	private void setPersonAddress(JSONArray array) {
		personAddress = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			personAddress.add(array.get(i).isString().stringValue());
		}			
	}
	private void setNormalizationType(JSONArray array) {
		normalizationType = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			normalizationType.add(array.get(i).isString().stringValue());
		}			
	}
	private void setNormalizationTermSourceREF(JSONArray array) {
		normalizationTermSourceREF = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			normalizationTermSourceREF.add(array.get(i).isString().stringValue());
		}		
	}
	private void setNormalizationTermAccession(JSONArray array) {
		normalizationTermAccession = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			normalizationTermAccession.add(array.get(i).isString().stringValue());
		}
		
	}
	private void setExperimentalFactorType(JSONArray array) {
		experimentalFactorType = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			experimentalFactorType.add(array.get(i).isString().stringValue());
		}	
		
	}
	private void setExperimentalFactorTermSourceREF(JSONArray array) {
		experimentalFactorTermSourceREF = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			experimentalFactorTermSourceREF.add(array.get(i).isString().stringValue());
		}	
	}
	private void setExperimentalFactorTermAccession(JSONArray array) {
		experimentalFactorTermAccession = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			experimentalFactorTermAccession.add(array.get(i).isString().stringValue());
		}		
	}
	private void setExperimentalFactorName(JSONArray array) {
		experimentalFactorName = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			experimentalFactorName.add(array.get(i).isString().stringValue());
		}		
	}
	private void setExperimentalDesignTermSourceREF(JSONArray array) {
		experimentalDesignTermSourceREF = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			experimentalDesignTermSourceREF.add(array.get(i).isString().stringValue());
		}
		
	}
	private void setExperimentalDesignTermAccession(JSONArray array) {
		experimentalDesignTermAccession = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			experimentalDesignTermAccession.add(array.get(i).isString().stringValue());
		}
	}
	private void setExperimentalDesign(JSONArray array) {
		experimentalDesign = new ArrayList<String>();
		for(int i =0;i<array.size();i++){
			experimentalDesign.add(array.get(i).isString().stringValue());
		}
	}
	public String getDateOfExperiment() {
		return dateOfExperiment;
	}

	public void setDateOfExperiment(String dateOfExperiment) {
		this.dateOfExperiment = dateOfExperiment;
	}

	public String getExperimentDescription() {
		return experimentDescription;
	}

	public void setExperimentDescription(String experimentDescription) {
		this.experimentDescription = experimentDescription;
	}

	public String getInvestigationTitle() {
		return investigationTitle;
	}

	public void setInvestigationTitle(String investigationTitle) {
		this.investigationTitle = investigationTitle;
	}

	public String getMagetabVersion() {
		return magetabVersion;
	}

	public void setMagetabVersion(String magetabVersion) {
		this.magetabVersion = magetabVersion;
	}

	public String getPublicReleaseDate() {
		return publicReleaseDate;
	}

	public void setPublicReleaseDate(String publicReleaseDate) {
		this.publicReleaseDate = publicReleaseDate;
	}

	public List<String> getExperimentalDesign() {
		return experimentalDesign;
	}

	public void setExperimentalDesign(List<String> experimentalDesign) {
		this.experimentalDesign = experimentalDesign;
	}

	public List<String> getExperimentalDesignTermAccession() {
		return experimentalDesignTermAccession;
	}

	public void setExperimentalDesignTermAccession(
			List<String> experimentalDesignTermAccession) {
		this.experimentalDesignTermAccession = experimentalDesignTermAccession;
	}

	public List<String> getExperimentalDesignTermSourceREF() {
		return experimentalDesignTermSourceREF;
	}

	public void setExperimentalDesignTermSourceREF(
			List<String> experimentalDesignTermSourceREF) {
		this.experimentalDesignTermSourceREF = experimentalDesignTermSourceREF;
	}

	public List<String> getExperimentalFactorName() {
		return experimentalFactorName;
	}

	public void setExperimentalFactorName(List<String> experimentalFactorName) {
		this.experimentalFactorName = experimentalFactorName;
	}

	public List<String> getExperimentalFactorTermAccession() {
		return experimentalFactorTermAccession;
	}

	public void setExperimentalFactorTermAccession(
			List<String> experimentalFactorTermAccession) {
		this.experimentalFactorTermAccession = experimentalFactorTermAccession;
	}

	public List<String> getExperimentalFactorTermSourceREF() {
		return experimentalFactorTermSourceREF;
	}

	public void setExperimentalFactorTermSourceREF(
			List<String> experimentalFactorTermSourceREF) {
		this.experimentalFactorTermSourceREF = experimentalFactorTermSourceREF;
	}

	public List<String> getExperimentalFactorType() {
		return experimentalFactorType;
	}

	public void setExperimentalFactorType(List<String> experimentalFactorType) {
		this.experimentalFactorType = experimentalFactorType;
	}

	public List<String> getNormalizationTermAccession() {
		return normalizationTermAccession;
	}

	public void setNormalizationTermAccession(
			List<String> normalizationTermAccession) {
		this.normalizationTermAccession = normalizationTermAccession;
	}

	public List<String> getNormalizationTermSourceREF() {
		return normalizationTermSourceREF;
	}

	public void setNormalizationTermSourceREF(
			List<String> normalizationTermSourceREF) {
		this.normalizationTermSourceREF = normalizationTermSourceREF;
	}

	public List<String> getNormalizationType() {
		return normalizationType;
	}

	public void setNormalizationType(List<String> normalizationType) {
		this.normalizationType = normalizationType;
	}

	public List<String> getPersonAddress() {
		return personAddress;
	}

	public void setPersonAddress(List<String> personAddress) {
		this.personAddress = personAddress;
	}

	public List<String> getPersonAffiliation() {
		return personAffiliation;
	}

	public void setPersonAffiliation(List<String> personAffiliation) {
		this.personAffiliation = personAffiliation;
	}

	public List<String> getPersonEmail() {
		return personEmail;
	}

	public void setPersonEmail(List<String> personEmail) {
		this.personEmail = personEmail;
	}

	public List<String> getPersonFax() {
		return personFax;
	}

	public void setPersonFax(List<String> personFax) {
		this.personFax = personFax;
	}

	public List<String> getPersonFirstName() {
		return personFirstName;
	}

	public void setPersonFirstName(List<String> personFirstName) {
		this.personFirstName = personFirstName;
	}

	public List<String> getPersonLastName() {
		return personLastName;
	}

	public void setPersonLastName(List<String> personLastName) {
		this.personLastName = personLastName;
	}

	public List<String> getPersonMidInitials() {
		return personMidInitials;
	}

	public void setPersonMidInitials(List<String> personMidInitials) {
		this.personMidInitials = personMidInitials;
	}

	public List<String> getPersonPhone() {
		return personPhone;
	}

	public void setPersonPhone(List<String> personPhone) {
		this.personPhone = personPhone;
	}

	public List<String> getPersonRoles() {
		return personRoles;
	}

	public void setPersonRoles(List<String> personRoles) {
		this.personRoles = personRoles;
	}

	public List<String> getPersonRolesTermAccession() {
		return personRolesTermAccession;
	}

	public void setPersonRolesTermAccession(List<String> personRolesTermAccession) {
		this.personRolesTermAccession = personRolesTermAccession;
	}

	public List<String> getPersonRolesTermSourceREF() {
		return personRolesTermSourceREF;
	}

	public void setPersonRolesTermSourceREF(List<String> personRolesTermSourceREF) {
		this.personRolesTermSourceREF = personRolesTermSourceREF;
	}

	public List<String> getProtocolContact() {
		return protocolContact;
	}

	public void setProtocolContact(List<String> protocolContact) {
		this.protocolContact = protocolContact;
	}

	public List<String> getProtocolDescription() {
		return protocolDescription;
	}

	public void setProtocolDescription(List<String> protocolDescription) {
		this.protocolDescription = protocolDescription;
	}

	public List<String> getProtocolHardware() {
		return protocolHardware;
	}

	public void setProtocolHardware(List<String> protocolHardware) {
		this.protocolHardware = protocolHardware;
	}

	public List<String> getProtocolName() {
		return protocolName;
	}

	public void setProtocolName(List<String> protocolName) {
		this.protocolName = protocolName;
	}

	public List<String> getProtocolParameters() {
		return protocolParameters;
	}

	public void setProtocolParameters(List<String> protocolParameters) {
		this.protocolParameters = protocolParameters;
	}

	public List<String> getProtocolSoftware() {
		return protocolSoftware;
	}

	public void setProtocolSoftware(List<String> protocolSoftware) {
		this.protocolSoftware = protocolSoftware;
	}

	public List<String> getProtocolTermAccession() {
		return protocolTermAccession;
	}

	public void setProtocolTermAccession(List<String> protocolTermAccession) {
		this.protocolTermAccession = protocolTermAccession;
	}

	public List<String> getProtocolTermSourceREF() {
		return protocolTermSourceREF;
	}

	public void setProtocolTermSourceREF(List<String> protocolTermSourceREF) {
		this.protocolTermSourceREF = protocolTermSourceREF;
	}

	public List<String> getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(List<String> protocolType) {
		this.protocolType = protocolType;
	}

	public List<String> getPublicationAuthorList() {
		return publicationAuthorList;
	}

	public void setPublicationAuthorList(List<String> publicationAuthorList) {
		this.publicationAuthorList = publicationAuthorList;
	}

	public List<String> getPublicationDOI() {
		return publicationDOI;
	}

	public void setPublicationDOI(List<String> publicationDOI) {
		this.publicationDOI = publicationDOI;
	}

	public List<String> getPublicationStatus() {
		return publicationStatus;
	}

	public void setPublicationStatus(List<String> publicationStatus) {
		this.publicationStatus = publicationStatus;
	}

	public List<String> getPublicationStatusTermAccession() {
		return publicationStatusTermAccession;
	}

	public void setPublicationStatusTermAccession(
			List<String> publicationStatusTermAccession) {
		this.publicationStatusTermAccession = publicationStatusTermAccession;
	}

	public List<String> getPublicationStatusTermSourceREF() {
		return publicationStatusTermSourceREF;
	}

	public void setPublicationStatusTermSourceREF(
			List<String> publicationStatusTermSourceREF) {
		this.publicationStatusTermSourceREF = publicationStatusTermSourceREF;
	}

	public List<String> getPublicationTitle() {
		return publicationTitle;
	}

	public void setPublicationTitle(List<String> publicationTitle) {
		this.publicationTitle = publicationTitle;
	}

	public List<String> getPubMedId() {
		return pubMedId;
	}

	public void setPubMedId(List<String> pubMedId) {
		this.pubMedId = pubMedId;
	}

	public List<String> getQualityControlTermAccession() {
		return qualityControlTermAccession;
	}

	public void setQualityControlTermAccession(
			List<String> qualityControlTermAccession) {
		this.qualityControlTermAccession = qualityControlTermAccession;
	}

	public List<String> getQualityControlTermSourceREF() {
		return qualityControlTermSourceREF;
	}

	public void setQualityControlTermSourceREF(
			List<String> qualityControlTermSourceREF) {
		this.qualityControlTermSourceREF = qualityControlTermSourceREF;
	}

	public List<String> getQualityControlType() {
		return qualityControlType;
	}

	public void setQualityControlType(List<String> qualityControlType) {
		this.qualityControlType = qualityControlType;
	}

	public List<String> getReplicateTermAccession() {
		return replicateTermAccession;
	}

	public void setReplicateTermAccession(List<String> replicateTermAccession) {
		this.replicateTermAccession = replicateTermAccession;
	}

	public List<String> getReplicateTermSourceREF() {
		return replicateTermSourceREF;
	}

	public void setReplicateTermSourceREF(List<String> replicateTermSourceREF) {
		this.replicateTermSourceREF = replicateTermSourceREF;
	}

	public List<String> getReplicateType() {
		return replicateType;
	}

	public void setReplicateType(List<String> replicateType) {
		this.replicateType = replicateType;
	}

	public List<String> getSdrfFile() {
		return sdrfFile;
	}

	public void setSdrfFile(List<String> sdrfFile) {
		this.sdrfFile = sdrfFile;
	}

	public List<String> getTermSourceFile() {
		return termSourceFile;
	}

	public void setTermSourceFile(List<String> termSourceFile) {
		this.termSourceFile = termSourceFile;
	}

	public List<String> getTermSourceName() {
		return termSourceName;
	}

	public void setTermSourceName(List<String> termSourceName) {
		this.termSourceName = termSourceName;
	}

	public List<String> getTermSourceVersion() {
		return termSourceVersion;
	}

	public void setTermSourceVersion(List<String> termSourceVersion) {
		this.termSourceVersion = termSourceVersion;
	}
	public Map<String, String> getAllSingleMaps() {
		
		HashMap<String, String> temp= new HashMap<String, String>();
		temp.put("Date Of Experiment", dateOfExperiment);
		temp.put("Experiment Description", experimentDescription);
		temp.put("Investigation Title", investigationTitle);
		temp.put("Magetab Version",magetabVersion);
		temp.put("Public Release Date",publicReleaseDate);
		
		return temp;
	}
	public Map<String, List<String>> getAllMultiMaps() {
		HashMap<String,List<String>> temp = new HashMap<String, List<String>>();
		
		temp.put("Experimental Design Term Accession", experimentalDesignTermAccession);
		temp.put("Experimental Design Term Source REF",experimentalDesignTermSourceREF);
		temp.put("Experimental Factor Name",experimentalFactorName);
		temp.put("Experimental Factor Term Accession",experimentalFactorTermAccession);
		temp.put("Experimental Factor Term Source REF",experimentalFactorTermSourceREF);
		temp.put("Experimental Factor Type",experimentalFactorType);
		temp.put("Normalization Term Accession",normalizationTermAccession);
		temp.put("Normalization Term Source REF",normalizationTermSourceREF);
		temp.put("Normalization Type",normalizationType);
		temp.put("Person Address",personAddress);
		temp.put("Person Affiliation",personAffiliation);
		temp.put("Person Email",personEmail);
		temp.put("Person Fax",personFax);
		temp.put("Person First Name",personFirstName);
		temp.put("Person Last Name",personLastName);
		temp.put("PersonMidInitials",personMidInitials);
		temp.put("Person Phone",personPhone);
		temp.put("Person Roles",personRoles);
		temp.put("Person Roles Term Accession",personRolesTermAccession);
		temp.put("Person Roles Term Source REF",personRolesTermSourceREF);
		temp.put("Protocol Contact",protocolContact);
		temp.put("Protocol Description",protocolDescription);
		temp.put("Protocol Hardware",protocolHardware);
		temp.put("Protocol Name", protocolName);
		temp.put("Protocol Parameters",protocolParameters);
		temp.put("Protocol Software",protocolSoftware);
		temp.put("Protocol Term Accession",protocolTermAccession);
		temp.put("Protocol Term Source REF",protocolTermSourceREF);
		temp.put("Protocol Type",protocolType);
		temp.put("Publication Author List",publicationAuthorList);
		temp.put("Publication DOI",publicationDOI);
		temp.put("Publication Status",publicationStatus);
		temp.put("Publication Status Term Accession",publicationStatusTermAccession);
		temp.put("Publication Status TermSource REF",publicationStatusTermSourceREF);
		temp.put("PublicationTitle",publicationTitle);
		temp.put("pubMedId",pubMedId);
		temp.put("Quality Control Term Accession",qualityControlTermAccession);
		temp.put("Quality Control Term Source REF",qualityControlTermSourceREF);
		temp.put("Quality Control Type",qualityControlType);
		temp.put("Replicate Term Accession",replicateTermAccession);
		temp.put("ReplicateTermSourceREF",replicateTermSourceREF);
		temp.put("Replicate Type",replicateType);
		temp.put("SDRF File",sdrfFile);
		temp.put("Term Source File",termSourceFile);
		temp.put("Term Source Name",termSourceName);
		temp.put("Term Source Version",termSourceVersion);
		Iterator<String> itr = comments.keySet().iterator();
		while(itr.hasNext()){
			String fieldName = itr.next();
			Set<String> values = comments.get(fieldName);
			List<String> newList = new ArrayList<String>(values);
			temp.put(fieldName, newList);
		}
		return temp;
	}
	public void setComments(Map<String,Set<String>> comments) {
		this.comments = comments;
	}
	public Map<String,Set<String>> getComments() {
		return comments;
	}
}
