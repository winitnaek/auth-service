package com.bsi.sec.saml.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import com.bsi.sec.saml.exception.ConfigurationException;

public class ConfigurationManager {
	
	//public static final String CONFIG_FILE = "saml-config.xml";

	/**
	 * Read and validate configuration file.
	 * 
	 * @return
	 * @throws ConfigurationException 
	 * @throws Exception 
	 */
	public static boolean getValidConfigurations(String configPath, Map<String, BSIConfiguration> configMap) throws ConfigurationException 
	{
		//Map<String, Configuration> configMap = null;
		try {					
			Configurations configs = readConfigurationFile(configPath);
			if(!configs.hasConfigurations())
				throw new ConfigurationException("No configuration entries found, bad configuration!!!!");
			//configMap = new HashMap<String, Configuration>();
			for(BSIConfiguration c : configs.getConfigs())
			{
				if(validateConfiguration(c))
					configMap.put(c.getId()+"", c);
				else
					throw new ConfigurationException("Bad configuration!!!! Check configuration, id: " + c.getId());
			}
			return configs.isEnabled();
			
		} catch (Exception e) {
			throw new ConfigurationException("Unable to read config file! " + configPath, e);
		}				
	}
	
	/**
	 * 
	 *  Read configuration file.
	 *  
	 * @param configPath
	 * @return
	 * @throws Exception
	 */
	public static Configurations readConfigurationFile(String configPath) throws Exception
	{		
		FileInputStream fis = new FileInputStream(new File(configPath));
		JAXBContext ctx = JAXBContext.newInstance(Configurations.class);		
		Unmarshaller um = ctx.createUnmarshaller();			
		Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(ConfigurationManager.class.getResource("/saml-config.xsd"));
		um.setSchema(schema);		
		um.setEventHandler(new ValidationEventHandler(){
			
			public boolean handleEvent(ValidationEvent event) {
				 ValidationEventLocator locator = event.getLocator();		            
		            throw new RuntimeException("XML Validation Exception:  " +
		                event.getMessage() + " at row: " + locator.getLineNumber() +
		                " column: " + locator.getColumnNumber());
		        
			}});
		return (Configurations) um.unmarshal(fis);		
	}
	
	/**
	 * Validate the configuration file. Does the following...
	 * 1) Has valid required entries.
	 * 2) Has valid dependent entries.
	 * @param config
	 * @return
	 * @throws ConfigurationException 
	 */
	private static boolean validateConfiguration(BSIConfiguration config) throws ConfigurationException
	{
		//get cert stores and certificates and pre-validate credentials.
		if(!isUnique(config)) throw new ConfigurationException("Id and IdpIssuer elements cannot be re-used. Remove duplicates.");
		
		return true;		
	}
	
	private static boolean isUnique(BSIConfiguration config)
	{
		//TODO implement this in the future if necessary.
		return true;
	}
	
	/**
	 * Check if keystore and cert configurations are valid.
	 * 
	 * @param config
	 * @return
	 */
	public static boolean isCertConfigurationValid(BSIConfiguration config)
	{
		//TODO Certificate validation. Defensive check. Use if needed.
		return true;
	}

}
