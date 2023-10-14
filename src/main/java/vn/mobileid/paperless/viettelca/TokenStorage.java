package vn.mobileid.paperless.viettelca;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TokenStorage {

    private static final String FILE_PATH = "access_token.txt";
    private static final String FILE_CRED = "credentialID.txt";
    private static final String FILE_TRANS = "transactionID.txt";

    public static void saveToken(String accessToken) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(accessToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String loadToken() {
        StringBuilder tokenBuilder = new StringBuilder();
        try (FileReader reader = new FileReader(FILE_PATH)) {
            int character;
            while ((character = reader.read()) != -1) {
                tokenBuilder.append((char) character);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tokenBuilder.toString();
    }

    public static void saveCred(String accessToken) {
        try (FileWriter writer = new FileWriter(FILE_CRED)) {
            writer.write(accessToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String loadCred() {
        StringBuilder tokenBuilder = new StringBuilder();
        try (FileReader reader = new FileReader(FILE_CRED)) {
            int character;
            while ((character = reader.read()) != -1) {
                tokenBuilder.append((char) character);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tokenBuilder.toString();
    }

    public static void saveTransID(String accessToken) {
        try (FileWriter writer = new FileWriter(FILE_TRANS)) {
            writer.write(accessToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String loadTransID() {
        StringBuilder tokenBuilder = new StringBuilder();
        try (FileReader reader = new FileReader(FILE_TRANS)) {
            int character;
            while ((character = reader.read()) != -1) {
                tokenBuilder.append((char) character);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tokenBuilder.toString();
    }
}
