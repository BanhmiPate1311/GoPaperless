/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobileid.paperless.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.mobileid.paperless.object.ConnectorName;
import vn.mobileid.paperless.object.CountryModel;
import vn.mobileid.paperless.object.ENTERPRISE;
import vn.mobileid.paperless.process.process;
import vn.mobileid.paperless.utils.Difinitions;
import vn.mobileid.paperless.utils.LoadParamSystem;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;

/**
 *
 * @author PHAM HOANG YEN
 */
@Component
public class LoadConnectorToRAM implements ServletContextListener {

    /**
     * @param arg0
     * @throws Exception
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    @Autowired
    private process connect;

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        System.out.println("*********Load ConnectorToRAM started*********");
        try {

            ConnectorName[][] object = new ConnectorName[1][];

            connect.USP_GW_CONNECTOR_LIST(object);
            if (object[0].length > 0 && object != null) {
                ArrayList<ConnectorName> tempList;
                tempList = new ArrayList<>();
                tempList.addAll(Arrays.asList(object[0]));
                LoadParamSystem.updateParamSystem(Difinitions.CONFIG_LOAD_PARAM_CONNECTOR_NAME, tempList);
                System.out.println("*********load ram done started*********");

            }
            ENTERPRISE[][] enterprise = new ENTERPRISE[1][];
            connect.USP_GW_ENTERPRISE_LIST(enterprise);
            if (enterprise[0].length > 0 && enterprise != null) {
                ArrayList<ENTERPRISE> getEnterprise;
                getEnterprise = new ArrayList<>();
                getEnterprise.addAll(Arrays.asList(enterprise[0]));
                LoadParamSystem.updateParamEnterpriseSystem(Difinitions.CONFIG_LOAD_PARAM_ENTERPRISE, getEnterprise);
                System.out.println("*********load ram done started*********");
            }
            CountryModel[][] country = new CountryModel[1][];
            connect.USP_GW_COUNTRY_WITH_ELECTRONIC_LIST(country);
            if (country[0].length > 0 && country != null) {
                ArrayList<CountryModel> getCountry;
                getCountry = new ArrayList<>();
                getCountry.addAll(Arrays.asList(country[0]));
                LoadParamSystem.updateParamCountrySystem(Difinitions.CONFIG_LOAD_PARAM_COUNTRY, getCountry);
                System.out.println("*********load country ram done started*********");
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    /**
     * @param arg0
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        ServletContext servletContext = arg0.getServletContext();
// get our timer from the Context
        Timer timer = (Timer) servletContext.getAttribute("timerSynchNEAC");

// cancel all pending tasks in the timers queue
        if (timer != null) {
            timer.cancel();
        }

// remove the timer from the servlet context
        servletContext.removeAttribute("timerSynchNEAC");
        System.out.println("SynchNEACListener destroyed");

    }
}
