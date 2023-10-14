/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobileid.paperless.utils;

import org.springframework.stereotype.Component;
import vn.mobileid.paperless.object.ConnectorName;
import vn.mobileid.paperless.object.CountryModel;
import vn.mobileid.paperless.object.ENTERPRISE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author PHAM HOANG YEN
 */
@Component
public class LoadParamSystem {
     private static final Map<String, ArrayList<ConnectorName>> mapStart = new HashMap<>();
//    private static final Map<PREFIX_UUID, String> mapPrefix = new HashMap<PREFIX_UUID, String>();

    public static ArrayList<ConnectorName> getParamStart(String value) {
        ArrayList<ConnectorName> result = mapStart.get(value.trim());
        
        return result;
    }

    public static void updateParamSystem(String sKey, ArrayList<ConnectorName> sValue) {
        mapStart.put(sKey, sValue);
    }

    private static final Map<String, ArrayList<ENTERPRISE>> mapEnterpriseStart = new HashMap<>();
//    private static final Map<PREFIX_UUID, String> mapPrefix = new HashMap<PREFIX_UUID, String>();

    public static ArrayList<ENTERPRISE> getParamEnterpriseStart(String value) {
        ArrayList<ENTERPRISE> result = mapEnterpriseStart.get(value.trim());

        return result;
    }

    public static void updateParamEnterpriseSystem(String sKey, ArrayList<ENTERPRISE> sValue) {
        mapEnterpriseStart.put(sKey, sValue);
    }

    private static final Map<String, ArrayList<CountryModel>> mapCountryStart = new HashMap<>();
    public static ArrayList<CountryModel> getParamCountry(String value) {
        ArrayList<CountryModel> result = mapCountryStart.get(value.trim());

        return result;
    }

    public static void updateParamCountrySystem(String sKey, ArrayList<CountryModel> sValue) {
        mapCountryStart.put(sKey, sValue);
    }
}
