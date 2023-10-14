package vn.mobileid.paperless.entity;

public class PostBack {
    String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public static class SignFileRequest {
        private String tabName;
        private String requestID;
        private String signingToken;
        private String filename;
        private String signerToken;
        private String connector_name;
        private String value;
        private String type;
        private String name;

        public String getTabName() {
            return tabName;
        }

        public void setTabName(String tabName) {
            this.tabName = tabName;
        }

        public String getRequestID() {
            return requestID;
        }

        public void setRequestID(String requestID) {
            this.requestID = requestID;
        }

        public String getSigningToken() {
            return signingToken;
        }

        public void setSigningToken(String signingToken) {
            this.signingToken = signingToken;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getSignerToken() {
            return signerToken;
        }

        public void setSignerToken(String signerToken) {
            this.signerToken = signerToken;
        }

        public String getConnector_name() {
            return connector_name;
        }

        public void setConnector_name(String connector_name) {
            this.connector_name = connector_name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
