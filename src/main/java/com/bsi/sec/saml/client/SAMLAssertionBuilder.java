package com.bsi.sec.saml.client;

import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.opensaml.core.xml.XMLObjectBuilder;
import org.opensaml.core.xml.XMLObjectBuilderFactory;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.core.xml.schema.XSString;
import org.opensaml.saml.common.SAMLObjectBuilder;
import org.opensaml.saml.common.SAMLVersion;

import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.Attribute;
import org.opensaml.saml.saml2.core.AttributeStatement;
import org.opensaml.saml.saml2.core.AttributeValue;
import org.opensaml.saml.saml2.core.AuthnContext;
import org.opensaml.saml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml.saml2.core.AuthnStatement;
import org.opensaml.saml.saml2.core.Condition;
import org.opensaml.saml.saml2.core.Conditions;
import org.opensaml.saml.saml2.core.Issuer;
import org.opensaml.saml.saml2.core.NameID;
import org.opensaml.saml.saml2.core.OneTimeUse;
import org.opensaml.saml.saml2.core.Response;
import org.opensaml.saml.saml2.core.Subject;
import org.opensaml.saml.saml2.core.SubjectConfirmation;
import org.opensaml.saml.saml2.core.SubjectConfirmationData;
import org.opensaml.saml.saml2.core.impl.ResponseBuilder;



@SuppressWarnings({"unchecked","rawtypes"})
public class SAMLAssertionBuilder {
	
	 
		public XMLObjectBuilderFactory getSAMLBuilderFactory() {                      
		    return XMLObjectProviderRegistrySupport.getBuilderFactory();
		}
		
		//TODO implement in the future as necessary
		public void signAssertion(Assertion assertion)
		{
			
		}
			 
		
		public Issuer getIssuer(SAMLAssertionInput input,XMLObjectBuilderFactory factory)
		{
			SAMLObjectBuilder issuerBuilder = (SAMLObjectBuilder) factory.getBuilder(Issuer.DEFAULT_ELEMENT_NAME);
	        Issuer issuer = (Issuer) issuerBuilder.buildObject();
	        issuer.setValue(input.getStrIssuer());
	        return issuer;
		}
		
		public Response buildResponse(Assertion assertion, Issuer issuer, DateTime now, XMLObjectBuilderFactory builderFactory)
		{
			ResponseBuilder builder = (ResponseBuilder) builderFactory.getBuilder(Response.DEFAULT_ELEMENT_NAME);
			Response response = builder.buildObject();
			response.setIssuer(issuer);
			response.setIssueInstant(now);
			response.setDestination("https://bsi.com/");
			//
			response.getAssertions().add(assertion);
			
			return response;
			
		}
	 
		/**
		 * Builds a SAML Attribute of type String
		 * @param name
		 * @param object
		 * @param builderFactory
		 * @return
		 * @throws ConfigurationException
		 */
		public Attribute buildStringAttribute(String name, Object object, XMLObjectBuilderFactory builderFactory) 
		{
			
			SAMLObjectBuilder attrBuilder = (SAMLObjectBuilder) builderFactory.getBuilder(Attribute.DEFAULT_ELEMENT_NAME);
			 Attribute attrFirstName = (Attribute) attrBuilder.buildObject();
			 attrFirstName.setName(name);
	 
			 // Set custom Attributes
			 XMLObjectBuilder stringBuilder = builderFactory.getBuilder(XSString.TYPE_NAME);
			 XSString attrValueFirstName = (XSString) stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
			 attrValueFirstName.setValue((String) object);
	 
			 attrFirstName.getAttributeValues().add(attrValueFirstName);
			return attrFirstName;
		}
	 
		/**
		 * Helper method which includes some basic SAML fields which are part of almost every SAML Assertion.
		 *
		 * @param input
		 * @param issuer 
		 * @return
		 * @throws Exception 
		 */
		public Assertion buildDefaultAssertion(SAMLAssertionInput input, Issuer issuer, DateTime now ,XMLObjectBuilderFactory factory) throws Exception
		{
			try
			{
		         // Create the NameIdentifier
		         SAMLObjectBuilder nameIdBuilder = (SAMLObjectBuilder) factory.getBuilder(NameID.DEFAULT_ELEMENT_NAME);
		         NameID nameId = (NameID) nameIdBuilder.buildObject();
		         nameId.setValue(input.getStrNameID());
		         nameId.setNameQualifier(input.getStrNameQualifier());
		         nameId.setFormat(NameID.UNSPECIFIED);
	 
		         // Create the SubjectConfirmation
	 
		         SAMLObjectBuilder confirmationMethodBuilder = (SAMLObjectBuilder) factory.getBuilder(SubjectConfirmationData.DEFAULT_ELEMENT_NAME);
		         SubjectConfirmationData confirmationMethod = (SubjectConfirmationData) confirmationMethodBuilder.buildObject();
		         
		         DateTime nowPlus20 = now.plusMinutes(20);
		         confirmationMethod.setNotBefore(now);
		         //confirmationMethod.setNotOnOrAfter(nowPlus20);
	 
		         SAMLObjectBuilder subjectConfirmationBuilder = (SAMLObjectBuilder) factory.getBuilder(SubjectConfirmation.DEFAULT_ELEMENT_NAME);
		         SubjectConfirmation subjectConfirmation = (SubjectConfirmation) subjectConfirmationBuilder.buildObject();
		         subjectConfirmation.setSubjectConfirmationData(confirmationMethod);
	 
		         // Create the Subject
		         SAMLObjectBuilder subjectBuilder = (SAMLObjectBuilder) factory.getBuilder(Subject.DEFAULT_ELEMENT_NAME);
		         Subject subject = (Subject) subjectBuilder.buildObject();
	 
		         subject.setNameID(nameId);
		         subject.getSubjectConfirmations().add(subjectConfirmation);
	 
		         // Create Authentication Statement
		         SAMLObjectBuilder authStatementBuilder = (SAMLObjectBuilder) factory.getBuilder(AuthnStatement.DEFAULT_ELEMENT_NAME);
		         AuthnStatement authnStatement = (AuthnStatement) authStatementBuilder.buildObject();
		        
		         DateTime now2 = new DateTime();
		         authnStatement.setAuthnInstant(now2);
		         authnStatement.setSessionIndex(input.getSessionId());
		         //authnStatement.setSessionNotOnOrAfter(now2.plus(input.getMaxSessionTimeoutInMinutes()));
	 
		         SAMLObjectBuilder authContextBuilder = (SAMLObjectBuilder) factory.getBuilder(AuthnContext.DEFAULT_ELEMENT_NAME);
		         AuthnContext authnContext = (AuthnContext) authContextBuilder.buildObject();
		        
		         SAMLObjectBuilder authContextClassRefBuilder = (SAMLObjectBuilder) factory.getBuilder(AuthnContextClassRef.DEFAULT_ELEMENT_NAME);
		         AuthnContextClassRef authnContextClassRef = (AuthnContextClassRef) authContextClassRefBuilder.buildObject();
		         authnContextClassRef.setAuthnContextClassRef("urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport"); //not signing currently.
		         
				authnContext.setAuthnContextClassRef(authnContextClassRef);
		        authnStatement.setAuthnContext(authnContext);
	 
		        // Builder Attributes
		         SAMLObjectBuilder attrStatementBuilder = (SAMLObjectBuilder) factory.getBuilder(AttributeStatement.DEFAULT_ELEMENT_NAME);
		         AttributeStatement attrStatement = (AttributeStatement) attrStatementBuilder.buildObject();
	 
		      // Create the attribute statement
		         Map attributes = input.getAttributes();
		         if(attributes != null){
		        	Set<String> keySet =  attributes.keySet();
		        	 for (String key : keySet)
					{
		        		 Attribute attrFirstName = buildStringAttribute(key, attributes.get(key), factory);
		        		 attrStatement.getAttributes().add(attrFirstName);
					}
		         }
	 
		         // Create the do-not-cache condition
		         SAMLObjectBuilder doNotCacheConditionBuilder = (SAMLObjectBuilder) factory.getBuilder(OneTimeUse.DEFAULT_ELEMENT_NAME);
		         Condition condition = (Condition) doNotCacheConditionBuilder.buildObject();
		        
		         SAMLObjectBuilder conditionsBuilder = (SAMLObjectBuilder) factory.getBuilder(Conditions.DEFAULT_ELEMENT_NAME);
		         Conditions conditions = (Conditions) conditionsBuilder.buildObject();
		         conditions.setNotBefore(now);
		         //conditions.setNotOnOrAfter(nowPlus20); //default		         
		         conditions.getConditions().add(condition);	 
		   		      
		         // Create the assertion
		         SAMLObjectBuilder assertionBuilder = (SAMLObjectBuilder) factory.getBuilder(Assertion.DEFAULT_ELEMENT_NAME);
		         Assertion assertion = (Assertion) assertionBuilder.buildObject();
		         assertion.setIssuer(issuer);
		         assertion.setIssueInstant(now);
		         assertion.setVersion(SAMLVersion.VERSION_20);
	 
		         assertion.getAuthnStatements().add(authnStatement);
		         assertion.setSubject(subject);
		         assertion.getAttributeStatements().add(attrStatement);
		         assertion.setConditions(conditions);
	 
				return assertion;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw e;
			}			
		}
	 
		public static class SAMLAssertionInput
		{
	 
			private String strIssuer;
			private String strNameID;
			private String strNameQualifier;
			private String sessionId;
			private int maxSessionTimeoutInMinutes = 15; // default is 15 minutes
	 
			private Map attributes;
	 
			/**
			 * Returns the strIssuer.
			 *
			 * @return the strIssuer
			 */
			public String getStrIssuer()
			{
				return strIssuer;
			}
	 
			/**
			 * Sets the strIssuer.
			 *
			 * @param strIssuer
			 *            the strIssuer to set
			 */
			public void setStrIssuer(String strIssuer)
			{
				this.strIssuer = strIssuer;
			}
	 
			/**
			 * Returns the strNameID.
			 *
			 * @return the strNameID
			 */
			public String getStrNameID()
			{
				return strNameID;
			}
	 
			/**
			 * Sets the strNameID.
			 *
			 * @param strNameID
			 *            the strNameID to set
			 */
			public void setStrNameID(String strNameID)
			{
				this.strNameID = strNameID;
			}
	 
			/**
			 * Returns the strNameQualifier.
			 *
			 * @return the strNameQualifier
			 */
			public String getStrNameQualifier()
			{
				return strNameQualifier;
			}
	 
			/**
			 * Sets the strNameQualifier.
			 *
			 * @param strNameQualifier
			 *            the strNameQualifier to set
			 */
			public void setStrNameQualifier(String strNameQualifier)
			{
				this.strNameQualifier = strNameQualifier;
			}
	 
			/**
			 * Sets the attributes.
			 *
			 * @param attributes
			 *            the attributes to set
			 */
			public void setAttributes(Map attributes)
			{
				this.attributes = attributes;
			}
	 
			/**
			 * Returns the attributes.
			 *
			 * @return the attributes
			 */
			public Map getAttributes()
			{
				return attributes;
			}
	 
			/**
			 * Sets the sessionId.
			 * @param sessionId the sessionId to set
			 */
			public void setSessionId(String sessionId)
			{
				this.sessionId = sessionId;
			}
	 
			/**
			 * Returns the sessionId.
			 * @return the sessionId
			 */
			public String getSessionId()
			{
				return sessionId;
			}
	 
			/**
			 * Sets the maxSessionTimeoutInMinutes.
			 * @param maxSessionTimeoutInMinutes the maxSessionTimeoutInMinutes to set
			 */
			public void setMaxSessionTimeoutInMinutes(int maxSessionTimeoutInMinutes)
			{
				this.maxSessionTimeoutInMinutes = maxSessionTimeoutInMinutes;
			}
	 
			/**
			 * Returns the maxSessionTimeoutInMinutes.
			 * @return the maxSessionTimeoutInMinutes
			 */
			public int getMaxSessionTimeoutInMinutes()
			{
				return maxSessionTimeoutInMinutes;
			}
	 
		}
	 
	
}
