/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.saml;


import static com.bsi.sec.saml.Constants.RSA;
import static com.bsi.sec.saml.Constants.X_509;
import com.bsi.sec.saml.config.BSIConfiguration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.AudienceRestriction;
import org.opensaml.saml.saml2.core.AuthnRequest;
import org.opensaml.saml.saml2.core.Condition;
import org.opensaml.saml.saml2.core.Conditions;
import org.opensaml.saml.saml2.core.Issuer;
import org.opensaml.saml.saml2.core.LogoutResponse;
import org.opensaml.saml.saml2.core.OneTimeUse;
import org.opensaml.saml.saml2.core.RequestAbstractType;
import org.opensaml.saml.saml2.core.Response;
import org.opensaml.saml.saml2.core.StatusResponseType;
import org.opensaml.saml.security.impl.SAMLSignatureProfileValidator;

import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.XMLObjectBuilderFactory;
import org.opensaml.core.xml.io.Marshaller;
import org.opensaml.core.xml.io.MarshallingException;
import org.opensaml.core.xml.io.Unmarshaller;
import org.opensaml.core.xml.io.UnmarshallerFactory;
//import org.opensaml.core.xml.security.SecurityHelper;
import org.opensaml.security.credential.Credential;
import org.opensaml.security.x509.BasicX509Credential;
import org.opensaml.xmlsec.signature.KeyInfo;
import org.opensaml.xmlsec.signature.Signature;
import org.opensaml.xmlsec.signature.support.SignatureValidator;
import org.opensaml.xmlsec.signature.X509Certificate;
import org.opensaml.xmlsec.signature.X509Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

//import com.bsi.sec.saml.config.BSIConfiguration;
import com.bsi.sec.saml.exception.InvalidIssuerException;
import com.bsi.sec.saml.exception.InvalidResponseException;
import com.bsi.sec.saml.exception.InvalidSignatureException;
import com.bsi.sec.saml.exception.SamlDecodeException;
import java.security.cert.Certificate;
import java.util.Base64;
import javax.xml.transform.TransformerConfigurationException;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;

import org.opensaml.xmlsec.signature.support.SignatureException;

/**
 * Helper class for performing java, opensaml crypto 
 * operations and other helper functions.
 * 
 * @author SudhirP
 *
 */
final public class SAMLHelper
{
   private final static Logger log = LoggerFactory.getLogger(SAMLHelper.class);  
   
   
   /**
	 * Validate signature of a response. This should be done to a response and is configuration driven.
	 * @param response
	 * @return
	 * @throws InvalidSignatureException 
	 */
	public static boolean hasValidResponseSignature(StatusResponseType response, Certificate configuredCert) throws InvalidSignatureException {		
		Signature signature = null;
		if(response.isSigned())
			signature = response.getSignature();
		if(signature == null && (response instanceof Response)) //get signature from assertions if not found in the response;
		{
			Response res = (Response) response;
			Assertion a = res.getAssertions().get(0);
			if(a != null && a.isSigned())
				signature = a.getSignature();	
		}
		if(signature == null)
			throw new InvalidSignatureException("No signature found!!!");
		//validate against profile
		SAMLSignatureProfileValidator profileValidator = new SAMLSignatureProfileValidator();
		try {
			profileValidator.validate(signature);			
		} catch (SignatureException e) {
			log.warn(e.getMessage(), e);
		}
		//validate signature
		Credential verificationCredential = getVerificationCredential(response, signature, configuredCert);
		if(verificationCredential == null) return false;
		try {
			SignatureValidator.validate(signature,verificationCredential);
			return true;
		} catch (SignatureException e) {		
			log.warn(e.getMessage(), e);
		}
		return false;		
	}
   /**
	 * Get credential from certificate in the signature section. (x509 - RSA)
 * @param response 
	 * @param signature
	 * @return
	 */
   public static  Credential getVerificationCredential(StatusResponseType response, Signature signature, Certificate configuredCert) {
		KeyInfo keyInfo = signature.getKeyInfo();
		X509Certificate cert = null;
		List<X509Data> x509List = keyInfo.getX509Datas();
		GOT_CERT:for(X509Data data : x509List)
		{
			for(X509Certificate tmpCert : data.getX509Certificates())
			{
				cert = tmpCert;
				break GOT_CERT;
			}
		}
		if(cert != null)
		{
			CertificateFactory certificateFactory;
			java.security.cert.X509Certificate certificate = null;
			try {				
				certificateFactory = CertificateFactory.getInstance(X_509);
				byte[] decodedCert = Base64.getDecoder().decode(cert.getValue());
				certificate = (java.security.cert.X509Certificate) certificateFactory
						.generateCertificate(new ByteArrayInputStream(decodedCert));				
			} catch (Exception e) {
				log.error("Unable to create certifcate object from file");
				return null;
			}
			try {
				if(!isTrustedCert(certificate, configuredCert))
				{
					log.error("Certificate not found in the keystore!" + certificate);
					return null;
				}
				
			} catch (Exception e1) {
				log.error("Unable to establish trust!! Cant find certificate!! " + e1);
				return null;
			} 
			// pull out the public key part of the certificate into a KeySpec
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(certificate.getPublicKey().getEncoded());
			// get KeyFactory object that creates key objects, specifying RSA
			KeyFactory keyFactory;
			try {
				keyFactory = KeyFactory.getInstance(RSA);
			} catch (NoSuchAlgorithmException e) {
				log.error("RSA Algorithm not found");
				return null;
			}
			// generate public key to validate signatures
			PublicKey publicKey;
			try {
				publicKey = keyFactory.generatePublic(publicKeySpec);
			} catch (InvalidKeySpecException e) {
				log.error("Invalid public key");
				return null;
			}
			//create credential 
			BasicX509Credential publicCredential = new BasicX509Credential(certificate);
			publicCredential.setEntityCertificate(certificate);
			publicCredential.setPublicKey(publicKey);
			return publicCredential;
			
		}		
		return null;
	}
   
   
   
   /**
    * 
    * 
    * @param config
    * @param request
    */
	public static void addOneTimeUseCondition(AuthnRequest request)
	{
            
		XMLObjectBuilderFactory factory = XMLObjectProviderRegistrySupport.getBuilderFactory();
		Conditions conditions = request.getConditions();		
		if(conditions == null)			
			conditions = (Conditions) factory.getBuilder(Conditions.DEFAULT_ELEMENT_NAME).buildObject(Conditions.DEFAULT_ELEMENT_NAME);		
		Condition condition = (Condition) factory.getBuilder(OneTimeUse.DEFAULT_ELEMENT_NAME).buildObject(OneTimeUse.DEFAULT_ELEMENT_NAME);		
		conditions.getConditions().add(condition);
		request.setConditions(conditions);
	}
	
	/**
	 * Add expiration conditions.
	 * @param authRequest
	 */
	public static void addExpiryCondition(AuthnRequest request, DateTime issueInstant, DateTime expireAt) {
		XMLObjectBuilderFactory factory = XMLObjectProviderRegistrySupport.getBuilderFactory();
		Conditions conditions = request.getConditions();		
		if(conditions == null)			
		{
			conditions = (Conditions) factory.getBuilder(Conditions.DEFAULT_ELEMENT_NAME).buildObject(Conditions.DEFAULT_ELEMENT_NAME);
			request.setConditions(conditions);
		}
		conditions.setNotBefore(issueInstant);
		conditions.setNotOnOrAfter(expireAt);		
		
	}
	
	/**
	    * Checks for before, on-or-after conditions.
	    * All times are expected to be in gmt.
	    * @param config
	    * @param request
	    */
		public static boolean checkConditions(Assertion assertion)
		{
			DateTimeZone gmt = DateTimeZone.forID("Etc/GMT");
			DateTime now = new DateTime(gmt);
			boolean valid = true;			
			if(assertion.getConditions() == null)return valid;
			//
			DateTime notBefore = assertion.getConditions().getNotBefore();
			if(notBefore != null)
				notBefore = notBefore.toDateTime(gmt);
			DateTime notOnOrAfter = assertion.getConditions().getNotOnOrAfter();
			if(notOnOrAfter != null)
				notOnOrAfter = notOnOrAfter.toDateTime(gmt);
			if(notBefore != null && now.isBefore(notBefore))
				return !valid;
			if(notOnOrAfter != null && !now.isBefore(notOnOrAfter))
				return !valid;
			//
			return valid;
		}
		
		/**
		 *  Get destincation of the response
		 * @param configurations 
		 * 
		 * @param response
		 * @return
		 * @throws InvalidIssuerException 
		 */
		public static String getResponseDestination(Map<String, BSIConfiguration> configurations, StatusResponseType response) throws InvalidIssuerException {		
			String destination = response.getDestination();
			if(StringUtils.isNotBlank(destination))
				return destination;
			else
			{
				//get it from assertion.
				Assertion assertion = ((Response) response).getAssertions().get(0);
				AudienceRestriction ar = assertion.getConditions().getAudienceRestrictions().get(0);
				ar.getAudiences().get(0);
				return "";
			}
		}
		
		/**
		 *  Get issuer of the response
		 * 
		 * @param response
		 * @return
		 * @throws InvalidIssuerException 
		 */
		public static String getResponseIssuer(StatusResponseType response) throws InvalidIssuerException {		
			Issuer issuer = response.getIssuer();			
			if(issuer != null)
				return issuer.getValue();
			else if(response instanceof Response)
			{
				Assertion assertion = ((Response) response).getAssertions().get(0);
				issuer = assertion.getIssuer();
				if(issuer != null)
					return issuer.getValue();
			}	
						
			if(issuer == null)
				throw new InvalidIssuerException("Cannot locate issuer in response! \n" + SAMLHelper.xmlObjectToString(response));
			return null;	
		}
		
		/**
		 * Is a SAML Login Response?
		 * @param response
		 * @return
		 * @throws InvalidResponseException
		 * @throws SamlDecodeException
		 */
		public static boolean isLoginResponse(StatusResponseType response) throws InvalidResponseException, SamlDecodeException
		{
			if(response instanceof Response)
				return true;
			else if(response instanceof LogoutResponse)		
				return false;
			else
				throw new InvalidResponseException("Unsupported response type.\n" + xmlObjectToString(response));
			
				
		}
		
		/**
		 * Encode the request.
		 * 
		 * @param requestMessage
		 * @return
		 * @throws MarshallingException
		 * @throws IOException
		 */
		public static String encodeRequest(RequestAbstractType requestMessage)
				throws MarshallingException, IOException {	
                    
                    
			Marshaller marshaller = XMLObjectProviderRegistrySupport.getMarshallerFactory().getMarshaller(requestMessage);
			Element dom = marshaller.marshall(requestMessage);	
			StringWriter responseWriter = new StringWriter();
                        try {
                            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(dom), new StreamResult(responseWriter));
                        } catch (TransformerConfigurationException ex) {
                            log.warn(ex.getMessage(), ex);
                        } catch (TransformerException ex) {
                            log.warn(ex.getMessage(), ex);
                        }
			if(log.isDebugEnabled())
				log.debug("Generated request : " + responseWriter.toString());
			Deflater deflater = new Deflater(8, true);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream, deflater);
			deflaterOutputStream.write(responseWriter.toString().getBytes());
			deflaterOutputStream.close();
			String encodedRequestMessage = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
			return URLEncoder.encode(encodedRequestMessage, "UTF-8").trim();
		}
		
		/**
		 * Un-marshall and convert to an XML object.
		 * 
		 * @param response
		 * @return
		 * @throws SamlDecodeException
		 */
		public static XMLObject decodeResponse(String response) throws SamlDecodeException
		{
			try {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				documentBuilderFactory.setNamespaceAware(true);
				DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();									
				byte[] respBytes = null;
				try {
					respBytes = Base64.getDecoder().decode(response);
				} catch (Exception e) {
					log.debug("Response was not B64 encoded." , e);
				}
				if(respBytes == null) //not Base64 encoded.
					respBytes = response.getBytes();	
				ByteArrayInputStream is = new ByteArrayInputStream(respBytes);		
				Document document = docBuilder.parse(is);
				Element element = document.getDocumentElement();
				UnmarshallerFactory unmarshallerFactory =  XMLObjectProviderRegistrySupport.getUnmarshallerFactory();
				Unmarshaller unmarshaller = unmarshallerFactory
						.getUnmarshaller(element);
				XMLObject decodeResponse =  unmarshaller.unmarshall(element);
				if(log.isDebugEnabled())
					log.debug("Decode Response: " + xmlObjectToString(decodeResponse));
				return decodeResponse;
			} catch (Exception e) {
				throw new SamlDecodeException("Unable to parse response! " + e.getMessage(), e);
			}
		}
		
		
		/**
		 * get string representation of the xml object
		 * @param obj
		 * @return
		 * @throws SamlDecodeException
		 */
		public static String xmlObjectToString(XMLObject obj)
		{
			try
			{
			  // Set up the output transformer
			  TransformerFactory transfac = TransformerFactory.newInstance();
			  Transformer trans = transfac.newTransformer();
			  trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			  trans.setOutputProperty(OutputKeys.INDENT, "yes");
			  // Print the DOM node
			  StringWriter sw = new StringWriter();
			  StreamResult result = new StreamResult(sw);
			  DOMSource source = new DOMSource(obj.getDOM());
			  trans.transform(source, result);
			  return sw.toString();
			}
			catch (TransformerException e)
			{			
				log.warn("Unable to get String representation of SAML DOM", e);
			}		
			return "";
		}
		
		/**
		 * 
		 * @param response
		 * @return
		 * @throws InvalidIssuerException 
		 * @throws KeyStoreException 
		 */
		public static boolean isTrustedCert(Certificate samlCert, Certificate configuredCert) throws InvalidIssuerException, KeyStoreException {
                    return samlCert.equals(configuredCert);
		}

		
   
}
