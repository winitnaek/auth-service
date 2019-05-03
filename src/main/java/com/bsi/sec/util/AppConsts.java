package com.bsi.sec.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public interface AppConsts
{
	public static final String APP_SETTINGS = "BSI_APP_SETTINGS";
	public static final String ON_BEHALF_PARAMS = "ON_BEHALF_PARAMS";
	public static final String ON_BEHALF_LOGIN = "ON_BEHALF_LOGIN";
	public static final String ON_BEHALF_ADMIN_LOGIN = "ON_BEHALF_ADMIN_LOGIN";
	public static final String EVT_LOGIN_IDS = "EVT_LOGIN";
	public static final String ER_ONBEHALF_EMP="ER_ONBEHALF_EMP";
	public static final String ER_EEDETAILS_PAGE="ER_EEDETAILS_PAGE";
	public static final String LOGIN_TYPE="LOGIN_TYPE";
	
	public static final String EMPLOYEE = "EMPLOYEE";
	public static final String REQUESTED_THEME = "REQ_THEME";
	public static final String MY_TEMP_THEME = "MY_TEMP_THEME";
	public static final String EMP_THEME = "EMP_THEME";
	
	// Service Objects - Address Verification LICENSE Key
	public static final String ADDR_LIC_KEY_SO = "WS36-LTV2-YDS1";

	//Schedule Import Availability global setting.
	public static final String WORKDAY_SCHEDULED_IMPORT_AVAILABLE="WORKDAY_SCHEDULED_IMPORT_AVAILABLE";
	public static final String SAP_SCHEDULED_IMPORT_AVAILABLE="SAP_SCHEDULED_IMPORT_AVAILABLE";
	public static final String SAP_CLOUD_SCHEDULED_IMPORT_AVAILABLE="SAP_CLOUD_SCHEDULED_IMPORT_AVAILABLE";
	public static final String NONE_SCHEDULED_IMPORT_AVAILABLE="NONE_SCHEDULED_IMPORT_AVAILABLE";
	public static final String LAWSON_SCHEDULED_IMPORT_AVAILABLE="LAWSON_SCHEDULED_IMPORT_AVAILABLE";
	// System info
	public static final String XML_SERVER = "XMLServer";
	public static final String XML_PORTNUM = "XMLPortNumber";
	public static final String XML_TIMEOUT = "XMLTimeOut";
	public static final String XML_SRVR_USRID = "eeFormsXmlSrvrUserId";
	
	public static final String XML_SRVR_DSET = "eeFormsXmlSrvrDataset";
	public static final String BUILD_RES = "buildnumber.properties";
	public static final String DATA_SOURCE_PREFIX = "jdbc/bsiobxDS";
	public static final String INIT_CONTEXT = "java:/comp/env";
	public static final String SMTP_SERVER = "smtp.server";
	public static final String MAIL_RESOURCE = "mail/session";
	public static final String PROD_REL_NOTES = "eeFormsReleaseNotesLoc";
	public static final String MAPPING_HOST = "eeFormsMappingHost";
	public static final String USE_RELEASE_NUMBER = "useReleaseNumber";
	public static final String TF_DATA_SOURCE_NAME = "TFDataSource";
	
	//public static final String CONFIG_PROP_PATH = "obx/config.properties";

	// Alerts constants
	public static final int TOTAL_ALLOW_EE_ALRT_ID = 1;
	public static final int TOTAL_ALLOW_ER_ALRT_ID = 2;
	public static final int LOCK_MARI_EE_ALRT_ID = 3;
	public static final int LOCK_MARI_ER_ALRT_ID = 4;
	public static final int LOCK_ALLOW_EE_ALRT_ID = 5;
	public static final int LOCK_ALLOW_ER_ALRT_ID = 6;
	public static final int EXEMPT_EE_ALRT_ID = 7;
	public static final int EXEMPT_ER_ALRT_ID = 8;
	public static final int DOC_ALRT_ID = 9999;
	public static final int ALRT_IND_EE = 0;
	public static final int ALRT_IND_ER = 1;
	public static final int ALRT_TYPE_FORM = 1;
	public static final int ALRT_TYPE_FIELD = 2;
	public static final int DOCU_REQUIRED = 1;
	public static final int TOTAL_ALLOW_EE_RULE_TYPE = 1;
	public static final int TOTAL_ALLOW_ER_RULE_TYPE = 2;
	public static final int EXEMPT_EE_RULE_TYPE = 7;
	public static final int EXEMPT_ER_RULE_TYPE = 8;
	public static final int ALRT_ID = 1;

	// Employee Profile Items
	public static final String ITEM_SSN = "SOCIAL SECURITY NUMBER";
	public static final String ITEM_FIRST_NAME = "FIRST NAME";
	public static final String ITEM_MIDDLE_NAME = "MIDDLE NAME";
	public static final String ITEM_LAST_NAME = "LAST NAME";
	public static final String ITEM_ADDR_LINE1 = "ADDRESS LINE 1";
	public static final String ITEM_ADDR_LINE2 = "ADDRESS LINE 2";
	public static final String ITEM_CITY = "CITY";
	public static final String ITEM_STATE = "STATE";
	public static final String ITEM_POSTAL_ZIP = "POSTAL ZIP (5 DIGIT)";
	public static final String ITEM_POSTAL_CODE = "POSTAL CODE (4 DIGIT)";
	public static final String ITEM_COUNTY = "COUNTY";
	public static final String ITEM_COUNTRY = "COUNTRY";
	public static final String ITEM_EMAIL_ADDR = "E-MAIL ADDRESS";
	public static final String ITEM_EIC_STATUS = "EIC STATUS";
	public static final String ITEM_ANNUAL_GROSS_WAGE = "ANNUAL GROSS WAGE";
	public static final String ITEM_PENSION_TAX = "PENSION TAX";
	public static final String ITEM_LOCK_MARI= "LOCK-IN MARITAL STATUS";
	public static final String ITEM_LOCK_ALLOW = "LOCK-IN ALLOWANCE";
	public static final String ITEM_ER_ACSESS_EE_TAXES_EMPID = "SFID";
	public static final String ITEM_ER_ACSESS_EE_TAXES_EMPINPUTFORMS = "SFIF";
	

	public static final String ITEM_WKSITE_ADDR_LINE1 = "WORK SITE: ADDRESS LINE 1";
	public static final String ITEM_WKSITE_ADDR_LINE2 = "WORK SITE: ADDRESS LINE 2";
	public static final String ITEM_WKSITE_CITY = "WORK SITE: CITY";
	public static final String ITEM_WKSITE_STATE = "WORK SITE: STATE";
	public static final String ITEM_WKSITE_POSTAL_ZIP = "WORK SITE: POSTAL ZIP (5 DIGIT)";
	public static final String ITEM_WKSITE_POSTAL_CODE = "WORK SITE: POSTAL CODE (4 DIGIT)";
	public static final String ITEM_WKSITE_COUNTY = "WORK SITE: COUNTY";
	public static final String ITEM_WKSITE_COUNTRY = "WORK SITE: COUNTRY";
	public static final String ITEM_WKSITE_EIC_STATUS = "WORK SITE: EIC STATUS";
	public static final String ITEM_WKSITE_RES_CODE = "WORK SITE: RESIDENT CODE";
	public static final String ITEM_WKSITE_ADDR_TYPE = "WORK SITE: ADDRESS TYPE";
	
	// Configuration
	public static final int DFLT_MAX_ALLOWANCES = 20;
	public static final int DFLT_MAX_EXEMPT_AMT = 30000;
	public static final int DFLT_TIME_ZONE = 0;
	public static final int DFLT_ROWS_PER_PAGE = 50;
	public static final String TIME_ZONE_HOLDER = "timeZoneHolder";
	public static final int WIZ_DFLT_MAX_ALLOWANCES = 99;
	public static final int WIZ_DFLT_MAX_EXEMPT_AMT = 99999;
	
	//general configuration
	public static final String ITEM_MAX_ALLOWANCES = "MAX_ALLOWANCES";
	public static final String ITEM_MAX_EXEMPT_AMT = "MAX_EXEMPTION_AMT";
	public static final String ITEM_EE_PROFILE_EDIT_BY_EE = "EE_PROFILE_EDIT_BY_EMPLOYEE";
	public static final String ITEM_LOCATE_TAXES_ON_EE_IMPORT = "LOCATE_TAXES_ON_EMPLOYEE_IMPORT";
	public static final String ITEM_EE_EDIT_OF_WKSTE_RESTRICTED = "EE_EDIT_OF_WKSTE_RESTRICTED";
	public static final String ITEM_ER_REQ_TO_APPRV_FMRS = "ER_REQ_TO_APPRV_FMRS";
	public static final String ITEM_APPROVE_EMPLOYEE_TAXES ="APPROVE_EMPLOYEE_TAXES";
	public static final String ITEM_EDIT_TAXES_IN_UI ="EDIT_TAXES_IN_UI";
	public static final String ITEM_SAML_OPTION = "SAML_OPTION";
	//public static final String ITEM_ER_REQ_TO_APPRV_CUST_FMRS = "ER_REQ_TO_APPRV_CUST_FMRS";
	public static final String ITEM_ER_AUTO_APPRV_FMRS_WITH_ALERTS = "ER_AUTO_APPRV_FMRS_WITH_ALERTS";
	public static final String ITEM_ER_AUTO_APPRV_FMRS_WITHOUT_APPRVL = "ER_AUTO_APPRV_FMRS_WITHOUT_APPRVL";
	public static final String ITEM_EE_DISP_CUST_FORMS = "EE_DISP_CUST_FORMS";
	public static final String ITEM_ER_REQ_TO_APPR_CUST_FORMS = "ER_REQ_TO_APPR_CUST_FORMS";
	public static final String ITEM_GROUP_FORMS = "GROUP_FORMS";
	public static final String ITEM_NUM_YRS_TO_PRSRV_FMRS = "NUM_YRS_TO_PRSRV_FMRS";
	public static final String ITEM_ER_ALERT_DELV_METH = "ER_ALERT_DELV_METH";
	public static final String ITEM_EE_ALERT_DELV_METH = "EE_ALERT_DELV_METH";
	public static final String ITEM_NUM_RWS_BFR_FLTRS_DSLPD = "NUM_RWS_BFR_FLTRS_DSLPD";
	public static final String ITEM_AUTO_ADJT_DAYLGT_SAV_TIME = "AUTO_ADJT_DAYLGT_SAV_TIME";
	public static final String ITEM_TIME_ZONE_DISPLAY = "TIME_ZONE_DISPLAY";
	public static final String ITEM_FILING_INSTRCT_DISPLAY = "FILING_INSTRCT_DISPLAY";
	public static final String ITEM_EDITED_FORM_DISPLAY = "EDITED_FORM_DISPLAY";
	public static final String ITEM_REJECTED_FORM_DISPLAY = "REJECTED_FORM_DISPLAY";
	public static final String ITEM_PENDING_FORM_DISPLAY = "PENDING_FORM_DISPLAY";
	public static final String ITEM_COMPLETED_FORM_DISPLAY = "COMPLETED_FORM_DISPLAY";
	public static final String ITEM_CUST_FORMS_AFTER_APPROVAL = "CUST_FORMS_AFTER_APPROVAL";
	public static final String ITEM_ENCRYPT_DOWNLOAD_XML= "ENCRYPT_DOWNLOAD_XML";
	public static final String ITEM_EXP_EMP_TAXES_WITH_BASE_FORMS= "EXP_EMP_TAXES_WITH_BASE_FORMS";
	public static final String ITEM_SEC_MASTER = "SEC_MASTER";
	public static final String ITEM_WEBSERVICE_CONFIG = "WEBSERVICE_CONFIG";
	public static final String ITEM_WEBSERVICE_CONFIG_EXT = "WSEXT_CONFIG";
	public static final String ITEM_SEND_EMAILS = "SEND_EMAILS";
	public static final String SSO_ACCESS_CONFIG = "SSO_ACCESS_CONFIG";
	public static final String HAS_SSO_ACCESS = "HAS_SSO_ACCESS";
	public static final String ITEM_TAX_EFFECTIVE_DATE = "TAX_EFFECTIVE_DATE";
	public static final String ITEM_RETROACTIVE_FORM_CORRECTIONS = "RETROACTIVE_FORM_CORRECTIONS";
	public static final String ITEM_ER_IMPORT_SUCCESS_EML = "ER_IMPORT_SUCCESS_EML";
	public static final String ITEM_ER_IMPORT_FAILURE_EML = "ER_IMPORT_FAILURE_EML";
	public static final String ITEM_INTEGRATION_TYPE = "INTG_TYPE";
	public static final String ITEM_STACK_IMPORT_REQ = "STACK_IMPORT_REQ";
	public static final String ITEM_ENABLED = "ENABLED";
	public static final String STATUS_ENABLED = "Enabled";
	public static final String STATUS_DISABLED = "Disabled";
	public static final String ITEM_FORMS_IN_PDF = "FORMS_IN_PDF";
	public static final String ITEM_FORMS_IN_HTML = "FORMS_IN_HTML";
	public static final String ITEM_RETURN_FEDERAL_FOR_SAME_LIVEWORK_TERRITORY = "RETURN_FEDERAL_FOR_SAME_LIVEWORK_TERRITORY";
	public static final String ITEM_VIEW_EE_PAYROLL_WITHHOLDING_INFO = "VIEW_EE_PAYROLL_WITHHOLDING_INFO";
	public static final String ITEM_VIEW_EE_MY_TAX_PROFILE = "VIEW_EE_MY_TAX_PROFILE";
	public static final String ITEM_VIEW_MY_PERSONAL_INFO = "VIEW_MY_PERSONAL_INFO";
	public static final String ITEM_COPY_WKSTE_OVR_ADDR_ON_IMPORT = "COPY_WKSTE_OVR_ADDR_ON_IMPORT";
	
	
	public static final String ITEM_IMPORT_SUCCESS = "ER_IMPORT_SUCCESS_EML";
	public static final String ITEM_IMPORT_FAILURE = "ER_IMPORT_FAILURE_EML";
	public static final String ITEM_EXPORT_SUCCESS = "ER_EXPORT_SUCCESS_EML";
	public static final String ITEM_EXPORT_FAILURE = "ER_EXPORT_FAILURE_EML";
	public static final String ITEM_EMP_EXPORT_LOG = "EMP_EXPORT_LOG";
	
	public static final String ITEM_COMPANY_TYPE = "COMPANY_TYPE";
	public static final String ITEM_CUSTOMER_COMPANY = "Customer";
	public static final String ITEM_INTERNAL_COMPANY = "Internal";
	public static final String ITEM_EMAIL_ADDRESS_TYPE = "PRIMARY_EMAIL_ADDRESS_TYPE";
	public static final String ITEM_UNMAPPED_TAXCODE_TYPE = "UNMAPPED_TAX_LOG";
	
	//config valid values;
	public static final String COLAPSED = "C";
	public static final String EXPANDED = "E";
	public static final String WHEN_PRESENT = "P";
	public static final String ALWAYS = "A";
	
	// SAP Config Data
	public static final String SAP_SYSNR = "SYSNR";
	public static final String SAP_CLIENTNR = "CLIENTNR";
	public static final String SAP_LANGUAGE = "SAP_LANGUAGE";
	public static final String SAP_INTG_TYPE_JCO = "JCO";
	public static final String SAP_INTG_TYPE_WS = "WS";
	public static final String SAP_PORTNR = "PORTNR";
	
		
	//Lawson Config Data
	public static final String ITEM_PRODUCT_LINE = "PRODUCT_LINE";
	public static final String ITEM_AGS = "AGS";
	public static final String ITEM_DME = "DME";
	public static final String ITEM_YEAR_END_PARAS_CONFIURED = "YEAR_END_PARAS_CONFIURED"; //Year End Parameter Configured
	
	//Workday Config Data
	public static final String  ITEM_WEBSERVICE_URL ="WEBSERVICE_URL";
	public static final String  ITEM_TENANT ="TENANT";
	public static final String  ITEM_VERSION ="VERSION";
	public static final String  ITEM_COMPANY_ID_TYPE ="COMPANYIDTYPE";
	public static final String  ITEM_CO_REFERENCE_ID ="CO_REFERENCE_ID";
	
	// Peoplesoft Config Data
	public static final String ITEM_PORTNUM = "PORTNUM";
	public static final String ITEM_USESSL = "USESSL";

	
	//Common Integration Config Data
	public static final String ITEM_USERNAME = "USERNAME";
	public static final String ITEM_PASSWORD = "PASSWORD";
	public static final String ITEM_ERP_NUM_RECORDS = "NRECORDS";
	public static final int DFLT_MAX_RECORDS = 1000;
	
	
	//ADP Config Data 
	public static final String ITEM_FTP_HOSTNAME_DEST = "FTPHOSTNAME";
	public static final String ITEM_FTP_PORTNR_DEST = "FTPPORTNR";
	public static final String ITEM_FTP_USERNAME_DEST = "FTPUSERNAME";
	public static final String ITEM_FTP_PASSWORD_DEST = "FTPPASSWORD";
	public static final String ITEM_PATH_DEST = "FTPPATH";
	public static final String ITEM_FILENAME_DEST = "FTPFILENAME";
	public static final String ITEM_PGP_KEY = "PGPKEY";
	public static final String ITEM_NRECORD_DEST= "NRECORDS_ADP";
	public static final String ITEM_AGGRE_SCHEDULED_TRANSFER = "AGGREGATE_SCHEDULED_TRANSFER";
	
	
	//Email Configuration
	public static final String ITEM_EE_CHNGS_PROFILE_EML = "EE_CHNGS_PROFILE_EML";
	public static final String ITEM_ER_FORM_APRVL_EML = "ER_FORM_APRVL_EML";
	public static final String ITEM_EE_FORM_SUBMIT_EML = "EE_FORM_SUBMIT_EML";
	public static final String ITEM_EE_FORM_REJECT_EML = "EE_FORM_REJECT_EML";
	public static final String ITEM_FORM_EXPORT_EML = "FORM_EXPORT_EML";
	public static final String ITEM_EE_W2_AVAILABLE = "EE_W2_AVAILABLE";
	public static final String ITEM_EE_W2_WITHDRAWN = "EE_W2_WITHDRAWN";
	public static final String ITEM_EE_W2_CONSENT_RECEIVED = "EE_W2_CONSENT_RECEIVED";
	public static final String ITEM_EE_W2_CONSENT_WITHDRAWN = "EE_W2_CONSENT_WITHDRAWN";
	public static final String ITEM_EE_W2_CONSENT_RESET = "EE_W2_CONSENT_RESET";
	public static final String ITEM_ER_EMP_LOGIN_INFO_EML = "ER_EMP_LOGIN_INFO_EML";
	public static final String ITEM_ER_CHANGES_EMP_PASSWORD_EML = "ER_CHANGES_EMP_PASSWORD_EML";
	public static final String ITEM_ER_EMP_PASSWORD_INFO_EML = "ER_EMP_PASSWORD_INFO_EML";
	public static final String ITEM_EE_USER_ACCOUNT = "EE_USER_ACCOUNT";
	
	public static final String ITEM_ER_BACKUP_SUCCESS_EML = "ER_BACKUP_SUCCESS_EML";
	public static final String ITEM_ER_BACKUP_FAILURE_EML = "ER_BACKUP_FAILURE_EML";
	public static final String ITEM_ER_RESTORE_SUCCESS_EML = "ER_RESTORE_SUCCESS_EML";
	public static final String ITEM_ER_RESTORE_FAILURE_EML = "ER_RESTORE_FAILURE_EML";
	
	public static final String ITEM_ER_UNMAPPED_TAX_EML = "ER_UNMAPPED_TAX_EML";
	
	
	//Data Transfer configuration
	public static final String ITEM_DATA_TRANSFER_METHOD = "DATA_TRANSFER_METHOD";
	public static final String ITEM_SCHEDULED_FREQUENCY = "SCHEDULED_FREQUENCY";
	public static final String ITEM_TRANSFER_START_DATE = "TRANSFER_START_DATE";
	public static final String ITEM_TRANSFER_START_TIME = "TRANSFER_START_TIME";
	public static final String ITEM_HOSTNAME = "HOSTNAME";
	
	public static final String ITEM_IMPORT_EXPORT_ROOT_DIR = "IMPORT_EXPORT_ROOT_DIR";
	public static final String ITEM_SCHEDULER_FREQUENCY = "SCHEDULER_FREQUENCY";
	public static final String ITEM_IMPORT_REQUEST_TIME = "IMPORT_REQUEST_TIME";
	public static final String ITEM_EXPORT_REQUEST_TIME = "EXPORT_REQUEST_TIME";
	
	public static final String ITEM_ADOBE_READER_VALID_VERSION = "ADOBE_READER_VALID_VERSION";
	public static final String ADOBE_READER_VALID_VERSION = "10.0";
	public static final String ITEM_W2_REQUEST_TIME = "W2_REQUEST_TIME";
	public static final String ITEM_NUM_W2YRS_TO_DISPLAY = "NUM_W2YRS_TO_DISPLAY";

	public static final String DLVR_MTH_SCREEN = "2";
	public static final String DLVR_MTH_EMAIL = "1";
	public static final String DLVR_MTH_SCREEN_AND_EMAIL = "0";
	
	public static final String DTMETH_MANUAL = "m";
	public static final String DTMETH_REALTIME = "r";
	public static final String DTMETH_SCHEDULED = "s";

	public static final String CONF_SCHED_FREQ_DAILY = "d";
	public static final String CONF_SCHED_FREQ_WKLY = "w";
	public static final String CONF_SCHED_FREQ_BIWKLY = "b";
	public static final String CONF_SCHED_FREQ_SMNTHLY = "s";
	public static final String CONF_SCHED_FREQ_MNTHLY = "m";
	
    public static final String SCHED_FREQ_MONDAY = "SCHED_FREQ_MONDAY";
	public static final String SCHED_FREQ_TUESDAY = "SCHED_FREQ_TUESDAY";
	public static final String SCHED_FREQ_WEDNESDAY = "SCHED_FREQ_WEDNESDAY";
	public static final String SCHED_FREQ_THURSDAY = "SCHED_FREQ_THURSDAY";
	public static final String SCHED_FREQ_FRIDAY = "SCHED_FREQ_FRIDAY";
	public static final String SCHED_FREQ_SATURDAY = "SCHED_FREQ_SATURDAY";
	public static final String SCHED_FREQ_SUNDAY = "SCHED_FREQ_SUNDAY";
	
	public static final String DEFAULT_HR = "12";
	public static final String DEFAULT_MIN= "00";
	public static final String DEFAULT_AMPM = "pm";
	public static final String DEFAULT_SCH_DAY = "1";
	
	public static final String YES = "y";
	public static final String NO = "n";
	public static final String IGNORE = "i";
	public static final String PARTIAL_DONE = "p";
	public static final String EXPORT_ALL_TAXES = "a";
	public static final String ADMIN_ONLY = "a";
	public static final String IN_PROGRESS = "i";
	
	public static final String EXCL_LOCKIN_INT = "2";
	public static final String YES_INT = "1";
	public static final String NO_INT = "0";
	
	public static final String YES_CAPS = "Y";
	public static final String NO_CAPS = "N";

	// Form status constants
	public static final int EDITED_STATUS = 0;
	public static final int SUBMITTED_STATUS = 1;
	public static final int REJECTED_STATUS = 2;
	public static final int APPROVED_STATUS = 3;
	public static final int EXPORTED_STATUS = 4;
	public static final int EDITED_STATUS_WITHAGREE = 5;
	public static final int NOT_POSTED = 6;
	public static final int POSTED = 7;
	public static final int WITHDRAWN = 8;
	public static final int EXPORT_INPROGRESS = 9;
	public static final int EXPORT_FAILED  = 10;
	public static final int SUBMIT_PENDING_STATUS = 11;
	
	// Worksite limit for display purpose - blank option
	public static final int WKSITE_COUNT_LIMIT_DISP_ALL_OPT = 50;
	public static final String WORKSITES = "worksites";
	public static final String WORKSITES_STATE_LIST = "wkSteStateList";
	public static final String WORKSITES_CNTY_LIST = "workCountyList";

	// Locator constants
	public static final int RUN_LOCATOR_DONE = 0;
	public static final int RUN_LOCATOR_NOT_DONE = 1;
	
	public static final String RUN_FILTER_FORMS ="RUNFILTERFRMS";
	public static final int RUN_FILTER_FORMS_DONE = 0;
	public static final int RUN_FILTER_FORMS_NOT_DONE = 1;
	public static final int RUN_FILTER_FORMS_NO_TAXES = 9;
	
	public static final String RCP_AGR_FLAG="RCPAGRFLAG";
	public static final int YES_RCP_AGR = 1;
	public static final int NO_RCP_AGR = 0;

	// Export Status
	public static final int READY_FOR_EXPORT = 1;
	public static final int NOT_READY_FOR_EXPORT = 0;
		
	// Form based alerts
	public static final String ALRT_FORM_BASED_EE = "AlrtFormBasedEE";
	public static final String ALRT_FORM_BASED_ER = "AlrtFormBasedER";
	public static final int ALRT_ID_FORM_BASED_EE = 101;
	public static final int ALRT_ID_FORM_BASED_ER = 102;
	public static final int ALRT_SRC_FORM_BASED_EE = 101;
	public static final int ALRT_SRC_FORM_BASED_ER = 102;
	public static final int ALRT_SRC_INDIC = 0;
	
	public static final String ADDR_TYP_LIVE_ON_JAN1 = "4";
	public static final String ADDR_TYP_WORK_ON_JAN1 = "5";

	// Address Verification
	public static final String ITEM_ADDR_VERIFY_CONFIG = "ADDR_VERIFY_CONFIG";

	public static final String ADDR_VERIFY_SUCCESS_MSG = "<font color=\"blue\"><b>Address has been verified and formatted as follows. "
			+ " Click the address below to accept.</b><br/></font>" + "<a href=\"javascript:dispFormattedAddr()\"><ul><li>" + "[STREET1]" + ", " + "[STREET2]" + ", " + "[CITY]"
			+ " " + "[STATE]" + ", " + "[POSTALZIP]" + "</ul></li></a>" + "<font color=\"blue\"><b>OR just continue filling the other details to keep "
			+ " the address as entered.</b></font>";

	public static final String ADDR_VERIFY_SUCCESS_MSG2 = "<font color=\"blue\"><b>Address has been verified and formatted as follows. "
			+ " Click the address below to accept.</b><br/></font>" + "<a href=\"javascript:dispFormattedAddr()\"><ul><li>" + "[STREET1]" + ", " + "[CITY]" + " " + "[STATE]"
			+ ", " + "[POSTALZIP]" + "</ul></li></a>" + "<font color=\"blue\"><b>OR just continue filling the other details to keep " + " the address as entered.</b></font>";

	public static final String ADDR_VERIFY_SUCCESS_MSG_WK_STE = "<font color=\"blue\"><b>Address has been verified and formatted as follows. "
			+ " Click the address below to accept.</b><br/></font>" + "<a href=\"javascript:dispFormattedAddrWkLoc()\"><ul><li>" + "[STREET1]" + ", " + "[STREET2]" + ", "
			+ "[CITY]" + " " + "[STATE]" + ", " + "[POSTALZIP]" + "</ul></li></a>" + "<font color=\"blue\"><b>OR just continue filling the other details to keep "
			+ " the address as entered.</b></font>";

	public static final String ADDR_VERIFY_SUCCESS_MSG2_WK_STE = "<font color=\"blue\"><b>Address has been verified and formatted as follows. "
			+ " Click the address below to accept.</b><br/></font>" + "<a href=\"javascript:dispFormattedAddrWkLoc()\"><ul><li>" + "[STREET1]" + ", " + "[CITY]" + " " + "[STATE]"
			+ ", " + "[POSTALZIP]" + "</ul></li></a>" + "<font color=\"blue\"><b>OR just continue filling the other details to keep " + " the address as entered.</b></font>";

	public static final String ADDR_VERIFY_ERR_MSG = "<b>Address could not be located/verified.</b><br/>" + "Please make sure the information is accurate and try again.<br/>"
			+ "If you are sure the address entered is correct, locate the nearest verified address by clicking on the map to select the address.\n";

	// Address Type constants
	public static final int LIVE = 0;
	public static final int WORK = 1;
	public static final int LIVE_ON_JAN_1 = 4;
	public static final int WORK_ON_JAN_1 = 5;
	public static final int MAILING = 8;
	public static final int WORK_PRIMARY = 13;

	// Payroll Provider
	public static final String ERPSYSTEM = "ERPSYSTEM";
	public static final String ERPSYTEM_DEST = "ERPSYSTEM_DEST";
	public static final String LAWSON_SYSTEM = "Lawson";
	public static final String LAWSON_DRIVER = "com.bsi.eeForms.Lawson.LawsonDriver";
	public static final String SAP_DRIVER = "com.bsi.eeForms.SAP.SAPDriver";
	public static final String SAP_SYSTEM = "SAP";
	public static final String NO_ERPSYSTEM = "None";
	public static final String PEOPLESOFT_SYSTEM = "PEOPLESOFT";
	public static final String SAP_CLOUD_SYSTEM = "SAP Cloud";
	public static final String WORKDAY_SYSTEM = "Workday";
	public static final String ADP_SYSTEM = "ADP";
	public static final String TESSERACT_SYSTEM = "Tesseract";
	public static final String SAP_ON_PREMESIS_SYSTEM = "SAPOP";
	// Embedded Payroll Provider
	public static final List<String> EMBEDDED_PROVIDERS = Arrays.asList(SAP_CLOUD_SYSTEM,SAP_SYSTEM); 
    // Pagination
    public static final String PAGE_COUNT				= "pageCount";
    public static final String CURRENT_PAGE 			= "currentPage";
    public static final String START_PAGE				= "startPage";
    public static final String END_PAGE					= "endPage";
    public static final String PAGINATION_ACTION		= "paginationAction";
    
    public static final int DEFAULT_NO_OF_ROWS_PER_PAGE	= 19;
    public static final int SHOW_LINKS					= 5;
    
    // General
    public static final String MODE_HOLDER = "modeHolder";
    public static final int NEW_MODE = 1;
    public static final int EDIT_MODE = 2;
    
    // Form type
    public static final int RPTTYPE_BASE = 0;
	public static final int RPTTYPE_CUSTOM = 1;
	public static final int RPTTYPE_W2 = 2;
	public static final int RPTTYPE_W2C = 3;
	
	// CONFIG
	public static final String ITEM_ENCRYPTION_KEY = "ENCRYPTION_KEY";
	public static final String ITEM_CUST_FORMS_CONFIG = "CUST_FORMS_CONFIG";
	public static final String ITEM_ER_SUBMIT_EMP_FORMS = "ER_SUBMIT_EMP_FORMS";
	public static final String ITEM_CUST_BRTE_CONFIG= "BYPASS_EXPORT_CONFIG";
	public static final String ITEM_CUST_JAN1_ADDR_ON_EE_IMPORT= "DEFAULT_JAN1_ADDR_ON_EE_IMPORT";
	public static final String ITEM_SECURE_EMPLOYEE_PII= "SECURE_EMPLOYEE_PII";
	public static final String ITEM_SEARCH_BY_EMPEXTID = "SEARCH_BY_EXTERNAL_EMPLOYEE_ID";
	public static final String ITEM_EMPLOYEE_ACCESS_ALLOWED = "EMPLOYEE_ACCESS_ALLOWED";
	public static final String ITEM_EMP_W2_CONFIG = "EMP_W2_CONFIG";
	public static final String ITEM_EE_ENFORCE_LOCKIN_ON_SUBMIT = "EE_ENFORCE_LOCKIN_ON_SUBMIT";
	public static final String ITEM_DISP_FORMS_AFTER_APPROVAL = "DISP_FORMS_AFTER_APPROVAL";
	public static final String ITEM_EMP_IMPORT_LOG = "EMP_IMPORT_LOG";
	public static final String ITEM_SYSTEM_OF_RECORD = "SYSTEM_OF_RECORD";
	public static final String ITEM_PAYROLL_WITHHOLDING_ELECTION = "PAYROLL_WITHHOLDING_ELECTION";
	public static final String ITEM_IMMU_COMPANY_ID = "IMMUTABLE_COMPANY_UUID";
	public static final String ITEM_USER_SYSTEM_ID = "USERS_SYSTEM_ID";
	public static final String ITEM_CONVERSION_DONE = "CONVERSION_DONE";
	public static final String ITEM_EMPLOYEE_APP_THEME="EMPLOYEE_APP_THEME";
	public static final String ITEM_BYPASS_TAX_APPROVAL_ON_INITIAL_IMPORT = "BYPASS_TAX_APPROVAL_ON_INITIAL_IMPORT";
	public static final String ITEM_DATA_TRANSFER_TYPE="DATA_TRANSFER_TYPE";
	public static final String ITEM_ONBOARDING_STATUS = "ONBOARDING_STATUS";
	public static final String ITEM_ONBOARDING_DONE = "ONBOARDING_DONE";
	
	
	
	
	//W2 Configurations
	public static final String ITEM_W2_FTP_HOST_NAME = "FTPHOSTNAME";
	public static final String ITEM_W2_FTP_USER_NAME = "FTPUSERNAME";
	public static final String ITEM_W2_FTP_PASSWORD = "FTPPASSWORD";
	public static final String ITEM_W2_SUBSCRIPTION_YEAR = "W2_SUBSCRIPTION_YEAR";
	public static final String ITEM_W2_ACCESS_YEAR = "W2_ACCESS_YEAR";
	public static final String ITEM_W2_NUM_OF_YRS_DISP = "W2_NUM_OF_YRS_DISP";
	public static final String ITEM_W2_NUM_OF_YRS_TO_PRESERVE = "W2_NUM_OF_YRS_TO_PRESERVE";
	public static final String ITEM_W2_FILING_YEAR = "W2_FILING_YEAR";
	public static final String ITEM_W2_CONSENT_DATE = "W2_CONSENT_DATE";
	
	// Form Search
    public static final int ITEM_ALL_FORMS = 0;
    public static final int ITEM_APPROVED_FORMS = 1;
    public static final int ITEM_APPRV_FRMS_BY_AUTH = 2;
    public static final int ITEM_EXPORTED_FORMS = 3;
    public static final int ITEM_EXPORT_FRMS_BY_AUTH = 4;
    public static final int ITEM_POSTED_W2 = 5;
    public static final int ITEM_WITHDRAWN_W2 = 6;
    public static final int ITEM_APPRV_CUST_FORMS = 7;
    public static final int ITEM_EXPORTED_CUST_FORMS = 8;
    
    // W-2 Report IDs
    public static final String FED_W2_RPT = "FEDW2";
    public static final String FED_W2C_RPT = "FEDW2C";
    
	// PrimaryFlag constants
	public static final int PWKSTE = 1;
	public static final int NON_PWKSTE = 0;
    
    //Import Export Constants
    public static final String IMPORT = "I";
    public static final String EXPORT = "E";
    public static final String PENDING = "P";
    public static final String W4 = "W4";
    public static final String W2 = "W2";
    public static final String CUSTOM = "CR";
    public static final String OTHER = "O";
    
    // Manual Export Type
    public static final String BASE_EXPORT = "Base";
    public static final String CUSTOM_EXPORT = "Custom";
    public static final String ALL_TAXES_EXPORT = "Taxes";
    
    //General constants
    public static final String DIR_SEPARATOR = "\\";
    public static final String UNDERSCORE = "_";
    public static final String XML_EXTN = ".xml";
    
    //Form Status Report 
    public static final String AUTHORITY_DEFAULT = "ALL";
    public static final String FORM_TYPE_DEFAULT = "0";
    public static final String FORM_TYPE_CUSTOMER = "1";
    public static final String FORM_STATUS_DEFAULT = "1";
    public static final String PACKAGE_FORM_STATUS_DEFAULT = "11";
    //Tax Code Mapping Status
    public static final String MAPPED = "M";
    public static final String UNMAPPED = "U";
    
    public static final int ALRT_HIDE = 0;
    public static final int ALRT_SHOW = 1;
    
    public static final int HOME_PAGE = 0;
    public static final int PROFILE_PAGE = 1;
    
    //Download Status
    public static final int DOWNLOADED = 1;
    public static final int NOT_DOWNLOADED = 0;
    
    //CustomForm flag
    public static final String DEFAULT_CUSTFORMS = "1";
    public static final String NO_CUSTFORMS = "0";
    
    public static final String PROFILE_EFFDATE = "PROFILE_EFFDATE";
    
    public static final String TAX_EFFDATE = "TAX_EFFDATE";

	public static final String SSO_SESSION_TIMEOUT = "ssoSessionTimeout";
	public static final String SSO_KEEP_ALIVE = "ssoKeepAlive";
	
	// Tax Source Type constants
	public static final short TAX_SRCTYPE_BSI_LOCATOR = 0;
	public static final short TAX_SRCTYPE_COMPLTED_FORM_CORRECTION = 1;
	public static final short TAX_SRCTYPE_FORM_APPROVAL = 2;
	public static final short TAX_SRCTYPE_FORM_CONVERSION = 9;
	public static final short TAX_SRCTYPE_HISTORICAL_TAX = 8;

	
	public static final String RETRO_TAX_EFFDATE = "RETRO_TAX_EFFDATE";
	public static final String RETRO_REPORT = "RETRO_REPORT";
	
	public static final String FAILED = "F";
	
	
	public static final String SOR_BSI = "SOR-BSI";
	public static final String SOR_PAY = "SOR-PAY";
	
    // for displaying on reports  
    public static final int CITY_DISPLAY_LENGTH=30;
	
    //for displaying number of messages in event log page
    public static final int NUMBER_MESSAGES_EVEN_LOG= 15;
    public static final String WITHHOLDING_TAX_TYPE= "001";
    
    public static final int MINUS_ONE  = -1;
    
    public static final String SORT_ASC  = "ASC";
	public static final String BIRT_SESS_MGR = "viewingSessionManager";
  
	public static final String CITY = "City";
	public static final String COUNTY = "County";
	public static final String SCHOOL_DISTRICT = "School District";
	public static final String OTHR = "Other";
	//Manage User Login Limit
	public static final int USER_LOGINS_SEARCH_LIMIT_NO= 100;
	public static final int CUSTOMER_EVENTS_LOAD_LIMIT_NO= 1000;
	public static final int REQUEST_LOG_LOAD_LIMIT_NO= 1000;
	public static final String USER_LOGINS_SEARCH_LIMIT= "userLoginsSearchLimit";
	public static final String CUSTOMER_EVENTS_LOAD_LIMIT= "customerEventsLoadLimit";
	public static final String REQUEST_LOG_LOAD_LIMIT= "requestLogLoadLimit";
	public static final String VIEW_INSTRUCTION_PATH_DIR = "viewPDFInstructionPath.dir";
    public static final String VIEW_INSTRUCTION_PATH_VAL = "C\\:\\W4\\REPORT_INSTRUCTION\\";
    
    public static final String CONFIG_PROP_PATH ="obx/config.properties";
	//My Preferences
	public static final String MY_APP_THEME = "MY_APP_THEME";
	public static final String MY_FORM_FORMAT = "MY_FORM_FORMAT";
	public static final String MY_FORM_VERSION = "MY_FORM_VERSION";
	public static final String MY_FORM_NRA = "MY_FORM_NRA";
	public static final String SAVE = "S";
	public static final String RESET = "R";
	public static final String ENGLISH = "ENGLISH";
	public static final String ST_NO = "NO";
	//WebService
	public static final String IMPORT_WSDL = "import.ws.wsdl";
	public static final String WS_USERNAME = "EFWSUSER";
	public static final String WS_PASSWORD = "import.ws.password";
	
	// Categories
		public final static int CAT_WITHHOLDING = 1;
		public final static int CAT_NON_RESIDENT = 2;
		public final static int CAT_NON_RESIDENT_ALIEN = 3;
		public final static int CAT_MIL_SERVICE_MEMBER = 4;
		public final static int CAT_MILITARY_SPOUSE = 5;
		public final static int CAT_NATIVE_AMERICAN = 6;
		public final static int CAT_SPANISH = 7;
		public final static int CAT_EXEMPT = 8;
		public final static int CAT_RECIP_AGR = 9;
		public final static int CAT_WORK_OUTSIDE_OF_WH_STATE = 10;
		public final static int CAT_OTHER = 11;
		

		public final static int FLG_NO = 0;
		public final static int FLG_YES = 1;
		
		// Reciprocal reporting methods
		public final static int MTHD_TAX_RES_ONLY_WHEN_NO_NON_RES = 1;
		public final static int MTHD_SUBTRACT_NON_RES_TAX_FROM_RES = 2;
		public final static int MTHD_TAX_BOTH_RES_AND_NON_RES = 3;
		public final static int MTHD_DO_NOT_TAX_RES_AUTH = 4;
		public final static int MTHD_DO_NOT_TAX_NON_RES_AUTH = 5;
		
		// Form Filter Reasons
		public final static int UNFILTERED = 0;
		public final static int FILTERED_BY_PREFERENCE = 1;
		public final static int FILTERED_BY_RECIPROCAL_MATRIX = 2;
		public final static int FILTERED_BY_USER_RESPONSE = 3;
		public final static int FILTERED_BY_USER_OVERRIDE = 4;
		
		// Form Filter RPTTYPE
		public final static int FEDERAL_TYPE = 0;
		public final static int STATE_TYPE = 1;
		public final static int LOCALITY_TYPE = 2;
	
		
		public final static int[] RM_1_NO_WH_LV_INC_CAT ={CAT_WITHHOLDING,CAT_EXEMPT};
		public final static int[] RM_1_NO_WH_LV_EXC_CAT ={CAT_RECIP_AGR,CAT_WORK_OUTSIDE_OF_WH_STATE};
		public final static int[] RM_1_WH_LV_INC_CAT ={CAT_EXEMPT};
		public final static int[] RM_1_WH_LV_EXC_CAT ={CAT_WITHHOLDING,CAT_RECIP_AGR};
		public final static int[] RM_1_WH_WK_INC_CAT ={CAT_WITHHOLDING,CAT_EXEMPT};
		public final static int[] RM_1_WH_WK_EXC_CAT ={CAT_RECIP_AGR,CAT_WORK_OUTSIDE_OF_WH_STATE};
		
		public final static int[] RM_2_3_LV_INC_CAT ={CAT_WITHHOLDING,CAT_EXEMPT};
		public final static int[] RM_2_3_LV_EXC_CAT ={CAT_NON_RESIDENT,CAT_RECIP_AGR};
		public final static int[] RM_2_3_WK_INC_CAT ={CAT_WITHHOLDING,CAT_EXEMPT,CAT_NON_RESIDENT};
		public final static int[] RM_2_3_WK_EXC_CAT ={CAT_RECIP_AGR,CAT_WORK_OUTSIDE_OF_WH_STATE};
		
		public final static int[] RM_4_LV_INC_CAT ={CAT_EXEMPT};
		public final static int[] RM_4_LV_EXC_CAT ={CAT_WITHHOLDING,CAT_RECIP_AGR};
		public final static int[] RM_4_WK_INC_CAT ={CAT_WITHHOLDING,CAT_EXEMPT};
		public final static int[] RM_4_WK_EXC_CAT ={CAT_WORK_OUTSIDE_OF_WH_STATE,CAT_RECIP_AGR};

		public final static int[] RM_5_RA_LV_INC_CAT ={CAT_WITHHOLDING};
		public final static int[] RM_5_RA_WK_INC_CAT ={CAT_EXEMPT,CAT_RECIP_AGR};
		
		public final static int[] RM_5_NO_RA_LV_INC_CAT ={CAT_EXEMPT};
		public final static int[] RM_5_NO_RA_LV_EXC_CAT ={CAT_NON_RESIDENT};
		public final static int[] RM_5_NO_RA_WK_INC_CAT ={CAT_EXEMPT};
		
		public final static int[] NRA_YES_INC_CAT ={CAT_NON_RESIDENT_ALIEN};
		public final static int[] NRA_NO_EXC_CAT ={CAT_NON_RESIDENT_ALIEN};
		public static final int[] SPA_YES_INC_CAT = {CAT_SPANISH};
		public static final int[] SPA_NO_EXC_CAT = {CAT_SPANISH};
		
		public final static int[] FORM_CAT_WH ={CAT_WITHHOLDING};
		public final static int[] FORM_CAT_EXEMPT={CAT_EXEMPT};
		
        public final static String STRING_YES = "YES";
        public final static String STRING_NO = "NO";
		
   public static final short THIRD_PARTY_SRC = 1;	
   
   public static final int TAX_DELETED_IN_UI_ONBSTAT = 12;
   public static final short TAX_ADDED_IN_UI_SOURCE = 2;
   public static final int TAX_DELETED_IN_UI_SOURCE = 3;
   public static final int TAX_UNDO_DELETED_IN_UI_ONBSTAT = -1;
 public static final int TAX_UNDO_DELETED_IN_UI_SOURCE = 0;
 
 public static final int TAX_APPROVAL_REQUIRED = 1;
 public static final int TAX_APPROVAL_NOT_REQUIRED = 0;
 
 
   //Worksite Address Override SOURCE 
 public static final int SRC_DEFAULT = 0;
 public static final int SRC_FROM_EE_PROFILE = 1;
 public static final int SRC_COPIED_FROM_ER_WKSTE = 2;
 
 public static final String[] TFLV2_STATES ={"AL","AR", "CA","CO","DE","IN","KY","MD","MI","MO","NJ","NY","OH","OR","PA","TX","WV"};
 
 //RATE_CODE_AUTHS
 public static final String PA_AUTH = "0042";
 public static final List<String> RATE_CODE_AUTHS = Arrays.asList(PA_AUTH); 
 
 public static final String ITEM_DEFAULT_FEIN = "_DEFAULT";

 public static final String ITEM_DEFAULT_LEGAL_NAME = "For Administrative Use";
 
 public static final List<Integer> Exempt_Status_List = new  ArrayList<Integer>(Arrays.asList(1,3,4,5,6,7));
 
 //Global settings for TJC mappings.
	public static final String ADP_MAP_TJC="ADP_MAP_TJC";
	public static final String LAWSON_MAP_TJC="LAWSON_MAP_TJC";
	public static final String SAP_MAP_TJC="SAP_MAP_TJC";
	public static final String SAP_CLOUD_MAP_TJC="SAP_CLOUD_MAP_TJC";
	public static final String TESSERACT_MAP_TJC="TESSERACT_MAP_TJC";
	public static final String WORKDAY_MAP_TJC="WORKDAY_MAP_TJC";
	public static final String NONE_MAP_TJC="NONE_MAP_TJC";
	
	public static final String VALUE1 = "V1";
	 public static final String VALUE2 = "V2";
	 
	 public static final String[] V1V2_ITEMS ={"ags","clientnr", "dme","hostname","intg_type","nrecords","password","portnr","product_line","sap_language","sysnr","tenant","username","version",                                         
	"webservice_url","ftpfilename","ftphostname","ftppath","ftpusername","ftppassword","ftpportnr","pgpkey","nrecords_adp"};
	 
	 public static final String[] SPL_VALUE_ITEMS ={"time_zone_display","ee_alert_delv_meth","tax_effective_date","exp_emp_taxes_with_base_forms","ee_profile_edit_by_employee","wsext_config","employee_app_theme","er_auto_apprv_fmrs_with_alerts","primary_email_address_type"};
	 
	 public static final String[] DECRYPT_ITEMS ={"ags","dme","ftpportnr","hostname", "portnr","sysnr","webservice_url"};
	 
	 public static final String[] OBFUSCATE_ITEMS={"ftppassword","password"};
	 
	 public static final String[] EXP_LIST = {"Number of allowances allowed", "Exemption amount allowed", "Number of years to preserve forms"};
		
 
 public static final List<String> config_itemnames_list = new  ArrayList<String>(Arrays.asList("ITEM_EMP_IMPORT_LOG","ITEM_EMP_EXPORT_LOG","ITEM_ONBOARDING_DONE","EE_USER_ACCOUNT",
		 "ENABLED","ENCRYPTION_KEY","ER_CHANGES_EMP_PASSWORD_EML","ER_EMP_LOGIN_INFO_EML","ER_EMP_PASSWORD_INFO_EML","SEC_MASTER","SSO_ACCESS_CONFIG",
		 "ER_BACKUP_SUCCESS_EML","ER_BACKUP_FAILURE_EML","ER_RESTORE_SUCCESS_EML","ER_RESTORE_FAILURE_EML","SAML_OPTION","SECURE_EMPLOYEE_PII","VIEW_MY_PERSONAL_INFO"));
 
 public static final String[] COMP_LVL_SETUP_TBL_LIST  ={"BTOAUTHRVW","BTOCODEMAP","BTOCOMPNX","BTOMSMAP", "BTORPTEXCL","BTORPTOVR","BTOTAXC","BTOTJC","BTOWKSTE","BTOCONFIG","BTOCSMSG"};//datasetlevel ,"BTOTJCDES"
 public static final String[] COMP_LVL_FULL_TBL_LIST  ={"BTOEALRT","BTOEEPROC","BTOEEPREF","BTOEMP","BTOEMPWH","BTOEMPWK","BTOERULE","BTOFPRGS","BTOOUTP","BTOSTDT","BTOTAX"};
 public static final String[] DS_LVL_SETUP_TBL_LIST  ={"BTOTJCDES"};
 public static final String[] DS_LVL_FULL_TBL_LIST  ={"BTOLOGIN L"};
 public static final String[] COMP_TBL_LIST  ={"BTOCOMP"};
 public static final String[] NOLOCK_TBL_LIST  ={"BTOLOGIN L","BTOTAX","BTOOUTP","BTOSTDT","BTOFPRGS","BTOEEPROC"};
 
 public final static  String BR_TRIGGER = "BR_TRIGGER";
 public final static  String BR_GROUP= "BR_GROUP";
 public final static  String BR_JOB= "BR_JOB";
 public final static String REQ_B ="B";
 public final static String REQ_R ="R";
 public final static String REQ_BACKUP ="Backup";
 public final static String REQ_RESTORE ="Restore";
 public final static String BACKUPTYPE_B ="B";
 public final static String BACKUPTYPE_F ="F";
 public final static String BACKUPTYPE_SETUP ="Setup";
 public final static String BACKUPTYPE_FULL ="Full";
 public static final String ITEM_BACKUP_RESTORE_CONFIG = "BACKUP_RESTORE_CONFIG";
 public static final String ITEM_BACKUP_RESTORE_FREQUENCY = "BACKUP_RESTORE_FREQUENCY";
 
 	//Added for SAML Options SF# 00100164
 	public static final String SAMLUID = "1";
 	public static final String EMPNUM = "2";
 	public static final String EMAIL = "3";
 	
 	//pii
 	public static final String PII_CAPTURED = "PII_CAPTURED";
 	public static final String PII_PROGRESS = "PII_PROGRESS";
	public static final String WDTIME = "WD-TIMESTAMP";
	public static final String PII_FLAG = "SECUREEMPPII";
	public static final String PII_TAG="PII";
	
	
	//ThirdParty
	public static final String TEST_SUCCESS_IMPORT = "Import settings tested successfully";
	public static final String TEST_FAILED_IMPORT = "Import settings test failed";
	public static final String TEST_SUCCESS_EXPORT = "Export settings tested successfully";
	public static final String TEST_FAILED_EXPORT = "Export settings test failed";
	public static final String TEST_SUCCESS_IMP_EXP = "Import/Export settings tested successfully";
	public static final String TEST_FAILED_IMP_EXP = "Import/Export settings test failed";

	public static final String LOCKINLETTERCONFIG="LOCKINLETTERCONFIG";
	
	public static final String LOG_LOGIN_STATUS = "LOG_LOGIN_STATUS";
	public static final String AUDIT_TRAIL_CONTEXT = "Login Audit";
	public static final String ON_BEHALF = "Admin on behalf of employee";
	public static final String ADMIN_VIEWED_EMP_DATA = "Admin viewed employee data";
	public static final String EMPLOYEE_VIEWED = "Employee Login";
	public static final String INFORMATION_SEVERITY = "Information";
	public static final String PERSONAL_INFO_FILE_NAME="info.pdf";
	
	public static final String SINGLE_SPACE = " ";
	public static final String PIPE_SAPERATOR = "||";
	public static final String SINGLE_PIPE_SAPERATOR="\\|";
	public static final String SESSION_COMP_KEY = "CUSTOMER";
	public static final String BLOCK_PII_CONVERSION = "BLOCKPII";
	
	public static final String WINDOWS_DEFAULT_ENCODING = "Windows-1252";
	
 }

