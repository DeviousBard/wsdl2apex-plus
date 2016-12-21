/**
 * This class is responsible for applying WS-Security to SOAP requests. It can add a UsernameToken profile for applying a user name and password, and
 * also can apply a Timestamp with expiry.
 *
 * NOTE: As of the "Summer '16" release Salesforce has not yet implemented any XML canonicalization algorithms.  Therefore, it is not possible to apply a
 * WS-Security digitial signature without first ensuring that the SOAP request is already in the correct canonical format (which would require a Herculean
 * effort, to say the least).
 */
public class WS_Security {

    /**
     * This class creates the UsernameToken profile element of a WS-Security header.
     */
    public class UsernameToken {
        public String wsseNamespace = 'http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd';
        public String wsuNamespace = 'http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd';
        public String Username;
        public String Password;
        public string wsuid = 'UsernameToken-19';
        private String[] Username_type_info = new String[]{'wsse:Username','http://www.w3.org/2001/XMLSchema','string','0','1','false'};
        private String[] Password_type_info = new String[]{'wsse:Password','http://www.w3.org/2001/XMLSchema','string','0','1','false'};
        private String[] apex_schema_type_info = new String[]{'http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd','true','false'};
        private String[] field_order_type_info = new String[]{'Username','Password'};
        private String[] wsuid_att_info = new String[] {'wsu:Id'};

        /**
         * Construct a new UsernameToken element for a WS-Security header.
         *
         * @param username a String representing the user name to set in the UsernameToken profile.
         * @param password a String representing the password to set in UsernameToken profile.
         */
        public UsernameToken(String username,String password) {
            this.Username = username;
            this.Password = password;
        }
    }

    /**
     * This class creates the Timestamp element of a WS-Security header including Created and Expires elements.
     */
    public class Timestamp {
        public String wsseNamespace = 'http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd';
        public String wsuNamespace = 'http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd';
        public String Created;
        public String Expires;
        public string wsuid = 'TS-18';
        private String[] Created_type_info = new String[]{'wsu:Created','http://www.w3.org/2001/XMLSchema','string','0','1','false'};
        private String[] Expires_type_info = new String[]{'wsu:Expires','http://www.w3.org/2001/XMLSchema','string','0','1','false'};
        private String[] apex_schema_type_info = new String[]{'http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd','true','false'};
        private String[] field_order_type_info = new String[]{'Created','Expires'};
        private String[] wsuid_att_info = new String[] {'wsu:Id'};

        /**
         * Construct a new Timestamp element for a WS-Security header including Created and Expires elements.
         * The expiry defaults to 300 seconds (5 minutes).
         */
        public Timestamp() {
            this(300);
        }

        /**
         * Construct a new Timestamp element for a WS-Security header including Created and Expires elements.
         * The expiry is set to the amount of seconds specified by the "expiryInSeconds" parameter.
         *
         * @param expiryInSeconds an Integer representing the number of seconds in the future to which the Expires
         *        element of the Timestamp will be set
         */
        public Timestamp(Integer expiryInSeconds) {
            Datetime currentDatetime = Datetime.now();
            this.Created = getGmtDatetimeInZuluFormat(currentDatetime);
            this.Expires = getGmtDatetimeInZuluFormat(currentDatetime, expiryInSeconds);
        }

        /**
         * Return a String representing the input date and time converted to GMT. The date time String is returned in Zulu format
         * (yyyy-mm-ddThh:mm:ssZ).
         *
		 * @param dt a Datetime object (in the local timezone) to be converted to a string in GMT in Zulu format.
		 *
         * @return a String representing the input date and time converted to GMT and rendered in Zulu format.
         */
        private String getGmtDatetimeinZuluFormat(Datetime dt) {
            return getGmtDatetimeinZuluFormat(dt, 0);
        }

        /**
         * Return a String representing a GMT date and time that is the specified number of seconds in the future relative to
         * the input date and time.  The date time String is returned in Zulu format (yyyy-mm-ddThh:mm:ssZ).
         *
		 * @param dt a Datetime object (in the local timezone) to be converted to a string in GMT in Zulu format.
         * @param expiryInSeconds an Integer representing the number of seconds in the future to offset the timestamp relative
         *        to the input date and time.
         *
         * @return a String representing the input date and time converted to GMT and rendered in Zulu format offset by the
         *         specified number of seconds into the future.
         */
        private String getGmtDatetimeinZuluFormat(Datetime dt, Integer futureOffsetInSeconds) {
			return dt.addSeconds(futureOffsetInSeconds).formatGmt('yyyy-MM-dd\'T\'HH:mm:ss\'Z\'');
        }
    }

    /**
     * An implemention of the WS-Security header in a SOAP message supporting only the "UsernameToken" and "Timestamp" elements.
     *
     * NOTE: As of the "Summer '16" release Salesforce has not yet implemented any XML canonicalization algorithms.  Therefore,
     * it is not possible to apply a WS-Security digitial signature.
     */
    public class SecurityHeaderType {
        public string mustUnderstand = 'True';
        public String wsseNamespace = 'http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd';
        public String wsuNamespace = 'http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd';
        public String envNamespace = 'http://schemas.xmlsoap.org/soap/envelope/';
        public WS_Security.UsernameToken UsernameToken;
        public WS_Security.Timestamp Timestamp;
        private String[] UsernameToken_type_info = new String[]{'wsse:UsernameToken','http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd','UsernameToken','1','1','false'};
        private String[] Timestamp_type_info = new String[]{'wsu:Timestamp','http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd','Timestamp','1','1','false'};
        private String[] mustUnderstand_att_info = new String[] {'env:mustUnderstand'};
        private String[] wsseNamespace_att_info = new String[]{'xmlns:wsse'};
        private String[] wsuNamespace_att_info = new String[]{'xmlns:wsu'};
        private String[] apex_schema_type_info = new String[]{'http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd','true','false'};
        private String[] field_order_type_info = new String[]{'UsernameToken','Timestamp'};

        /**
         * Construct a new SecurityHeaderType object creating the UsernameToken and Timestamp elements.
         *
         * @param username a String representing the username of UsernameToken element.
         * @param password a String representing the password of the UsernameToken element.
         */
        public SecurityHeaderType(String username, String password) {
            this.UsernameToken = new WS_Security.UsernameToken(username, password);
            this.Timestamp = new WS_Security.Timestamp();
        }
    }
}