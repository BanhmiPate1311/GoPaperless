package vn.mobileid.paperless.API;

import com.google.gson.*;

import javax.xml.bind.DatatypeConverter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.security.*;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;

public class Utils {

    public static String base64Encode(Object o) {
        return base64Encode(toJson(o));
    }

    public static String base64Encode(String s) {
//        LOG.debug("Input: " + s);
        return base64Encode(s.getBytes());
    }

    public static boolean isNullOrEmpty(String value) {
        if (value == null) {
            return true;
        }
        if (value.compareTo("") == 0) {
            return true;
        }
        return false;
    }

    public static String base64Encode(byte[] b) {
        return Base64.getEncoder().encodeToString(b);
    }

    public static byte[] base64Decode(String s) {
        return Base64.getMimeDecoder().decode(s);
    }

    public static byte[] base64Decode(byte[] b) {
        return Base64.getMimeDecoder().decode(b);
    }

    public static String toJson(Object o) {
        return gsTmp.toJson(o);
    }

    public static String getPKCS1Signature(String data, String relyingPartyKeyStore, String relyingPartyKeyStorePassword) throws Exception {
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        InputStream is = new FileInputStream(relyingPartyKeyStore);
        System.out.println("relyingPartyKeyStorePassword: " + relyingPartyKeyStorePassword);
        keystore.load(is, relyingPartyKeyStorePassword.toCharArray());

        Enumeration<String> e = keystore.aliases();
        PrivateKey key = null;
        String aliasName = "";
        while (e.hasMoreElements()) {
            aliasName = e.nextElement();
            key = (PrivateKey) keystore.getKey(aliasName, relyingPartyKeyStorePassword.toCharArray());
            if (key != null) {
                break;
            }
        }

        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initSign(key);
        sig.update(data.getBytes());
        return DatatypeConverter.printBase64Binary(sig.sign());
    }

    public static final Gson gsTmp = new GsonBuilder()
            .disableHtmlEscaping()
            .registerTypeHierarchyAdapter(byte[].class, new ByteArrayToBase64TypeAdapter())
            .registerTypeHierarchyAdapter(byte[][].class, new ByteArray2DimensionsToBase64TypeAdapter())
            .registerTypeHierarchyAdapter(boolean.class, new IntToBooleanTypeAdapter())
            //.registerTypeHierarchyAdapter(Class<T>.class, CustomDeserializer<T>)
            //.disableInnerClassSerialization()
            //.serializeNulls()
            //.registerTypeAdapterFactory(new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, Excluder.DEFAULT, jsonAdapterFactory))
            //.setPrettyPrinting()
            .create();

    public static class IntToBooleanTypeAdapter implements JsonDeserializer<Boolean> {

        @Override
        public Boolean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

            String in = json.getAsString();

            return in != null && (in.equals("true") || in.equals("1"));
//            try {
//                return json.getAsBoolean();
//            } catch (Exception ex) {
//                int in = json.getAsInt();
//                return in != 0;
//            }
        }
    }

    // Using Android's base64 libraries. This can be replaced with any base64 library.
    public static class ByteArrayToBase64TypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {

        @Override
        public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Base64.getMimeDecoder().decode(json.getAsString());  //Base64.decode(json.getAsString(), Base64.NO_WRAP);
        }

        @Override
        public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(Base64.getEncoder().encodeToString(src)); //JsonPrimitive(Base64.encodeToString(src, Base64.NO_WRAP));
        }
    }

    public static class ByteArray2DimensionsToBase64TypeAdapter implements JsonSerializer<byte[][]>, JsonDeserializer<byte[][]> {

        @Override
        public byte[][] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonArray jsonArray = json.getAsJsonArray();
            if (jsonArray == null || jsonArray.size() == 0) {
                return null;
            }
            byte[][] response = new byte[jsonArray.size()][];
            for (int i = 0; i < jsonArray.size(); i++) {
                response[i] = Base64.getMimeDecoder().decode(jsonArray.get(i).getAsString());
            }
            return response;
        }

        @Override
        public JsonElement serialize(byte[][] src, Type typeOfSrc, JsonSerializationContext context) {

            //String[] array = new String[src.length];
            //StringBuilder response = new StringBuilder();
            JsonArray jsonArray = new JsonArray();
            for (int i = 0; i < src.length; i++) {
                jsonArray.add(new JsonPrimitive(Base64.getEncoder().encodeToString(src[i])));
                //response.append(Base64.getEncoder().encodeToString(src[i])).append(",");
            }

            return jsonArray;//new JsonPrimitive(jsonArray.getAsString()); //jsonArray.getAsJsonPrimitive();
        }
    }

    public static String computeVC(List<byte[]> hashesList) throws NoSuchAlgorithmException {

        byte[][] hashes = new byte[hashesList.size()][];
        for (int i = 0; i < hashesList.size(); i++) {
            hashes[i] = hashesList.get(i);
        }
        if (hashes == null || hashes.length == 0) {
            throw new RuntimeException("The input is null or empty");
        }
        //single hash
        byte[] vcData = new byte[hashes[0].length];
        System.arraycopy(hashes[0], 0, vcData, 0, vcData.length);

        if (hashes.length > 1) {
            padding(hashes);

            for (int ii = 1; ii < hashes.length; ii++) {
                if (hashes[ii].length > vcData.length) {
                    byte[] tmp = new byte[hashes[ii].length];
                    System.arraycopy(vcData, 0, tmp, 0, vcData.length);
                    for (int ttt = vcData.length; ttt < hashes[ii].length; ttt++) {
                        tmp[ttt] = (byte) 0xFF;
                    }
                    vcData = new byte[tmp.length];
                    System.arraycopy(tmp, 0, vcData, 0, tmp.length);
                }
                for (int idx = 0; idx < hashes[ii].length; idx++) {
                    vcData[idx] |= hashes[ii][idx];
                }
            }
        }

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(vcData);
        byte[] vc = md.digest();
        short first = (short) (vc[0] << 8 | vc[1] & 0x00FF);
        short last = (short) (vc[vc.length - 2] << 8 | vc[vc.length - 1] & 0x00FF);
        return String.format("%04X-%04X", first, last);
    }

    public static byte[][] padding(byte[][] hashes) {
        int max = findMaxLen(hashes);
        byte[][] rsp = new byte[hashes.length][];

        for (int idx = 0; idx < hashes.length; idx++) {
            int len = hashes[idx].length;
            if (len < max) {
                byte[] tmp = new byte[len];
                System.arraycopy(hashes[idx], 0, tmp, 0, len);
                hashes[idx] = new byte[max];
                System.arraycopy(tmp, 0, hashes[idx], 0, len);
                for (int ii = len; ii < max; ii++) {
                    hashes[idx][ii] = (byte) 0xFF;
                }
            }
        }
        return rsp;
    }

    private static int findMaxLen(byte[][] hashes) {
        int max = 0;
        for (byte[] hh : hashes) {
            if (max < hh.length) {
                max = hh.length;
            }
        }
        return max;
    }
}
