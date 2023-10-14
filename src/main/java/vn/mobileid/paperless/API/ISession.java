package vn.mobileid.paperless.API;

public interface ISession {
    boolean close() throws Exception;
    void login() throws Exception;
}
