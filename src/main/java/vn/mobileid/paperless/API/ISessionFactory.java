package vn.mobileid.paperless.API;

import vn.mobileid.paperless.object.ConnectorLogRequest;

public interface ISessionFactory {

    // auth/login
    IServerSession getServerSession() throws Exception;

    IServerSession getServerSession(ConnectorLogRequest connectorLogRequest) throws Exception;

    // auth/login
    IUserSession getUserSession(String username, String password);
}
