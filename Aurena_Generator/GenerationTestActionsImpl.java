package com.ifsworld.projection;

import com.ifsworld.fnd.odp.api.exception.ProjectionException;
import com.ifsworld.mxcore.projection.api.Maintenix;
import com.ifsworld.mxcore.projection.api.MaintenixGateway;
import com.ifsworld.mxcore.projection.api.ResponseUtils;
import com.ifsworld.mxcore.projection.api.ServletRequest;
import com.ifsworld.mxcore.projection.api.ServletResponse;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObjectBuilder;

/*
* Implementation class for all global actions defined in the GenerationTest projection model.
*/
public class GenerationTestActionsImpl implements GenerationTestActions {

    private static final String SERVLET_PATH = "";
    public static final Logger LOGGER = Logger.getLogger(GenerationTestActionsImpl.class.getName());

    @Override
    public Map<String, Object> AcknowledgeAlert(final Map<String, Object> parameters, final Connection connection) {
        //=======================================================================================================
        //Call Duplicate Task Requirement servlet to duplicate a requirement
        //=======================================================================================================
        ServletRequest initReqRequest = new ServletRequest(SERVLET_PATH);

        String code = parameters.get("Code").toString();
        String assmblKey = parameters.get("AssmblKey").toString();
        String assmblBomCd = parameters.get("AssmblBomCd").toString();
        String classModeCd = parameters.get("ClassModeCd").toString();
        String orgkey = parameters.get("Orgkey").toString();
        String requirementDefinitionKey = parameters.get("RequirementDefinitionKey").toString();
        String configSlotKey = parameters.get("ConfigSlotKey").toString();

        // Add parameters to the request, Define the parameters as per the requirement
        initReqRequest.withParameter("code", code);
        initReqRequest.withParameter("assmblKey", assmblKey);
        initReqRequest.withParameter("assmblBomCd", assmblBomCd);
        initReqRequest.withParameter("classModeCd", classModeCd);
        initReqRequest.withParameter("orgkey", orgkey);
        initReqRequest.withParameter("requirementDefinitionKey", requirementDefinitionKey);
        initReqRequest.withParameter("configSlotKey", configSlotKey);

        Map<String, Object> resultMap = new HashMap<>();
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();

        try ( MaintenixGateway gateway = Maintenix.connect(parameters, connection)) {

            ServletResponse initReqResponse = gateway.send(initReqRequest);

            if (initReqResponse) {
                // If response success, implement your own logic here
                resultMap.put("AcknowledgeAlert", new ByteArrayInputStream(jsonBuilder.build().toString().getBytes(StandardCharsets.UTF_8)));
            } else {
                String errorTitle = ResponseUtils.getStringFromJsonObj(ResponseUtils.createJsonObject(initReqResponse.getResultJson()), "detail");
                jsonBuilder.add("Msg", errorTitle);
                resultMap.put("AcknowledgeAlert", new ByteArrayInputStream(jsonBuilder.build().toString().getBytes(StandardCharsets.UTF_8)));
            }
        } catch (ProjectionException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            throw ex;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            throw new ProjectionException("IFSMXCORE_ERROR: IFSMXCore exception occurred.", ex);
        }

        return resultMap;
    }
    @Override
    public Map<String, Object> AnotherFunction(final Map<String, Object> parameters, final Connection connection) {
        //=======================================================================================================
        //Call Duplicate Task Requirement servlet to duplicate a requirement
        //=======================================================================================================
        ServletRequest initReqRequest = new ServletRequest(SERVLET_PATH);

        String param1 = parameters.get("Param1").toString();
        String param2 = parameters.get("Param2").toString();
        String param3 = parameters.get("Param3").toString();

        // Add parameters to the request, Define the parameters as per the requirement
        initReqRequest.withParameter("param1", param1);
        initReqRequest.withParameter("param2", param2);
        initReqRequest.withParameter("param3", param3);

        Map<String, Object> resultMap = new HashMap<>();
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();

        try ( MaintenixGateway gateway = Maintenix.connect(parameters, connection)) {

            ServletResponse initReqResponse = gateway.send(initReqRequest);

            if (initReqResponse) {
                // If response success, implement your own logic here
                resultMap.put("AnotherFunction", new ByteArrayInputStream(jsonBuilder.build().toString().getBytes(StandardCharsets.UTF_8)));
            } else {
                String errorTitle = ResponseUtils.getStringFromJsonObj(ResponseUtils.createJsonObject(initReqResponse.getResultJson()), "detail");
                jsonBuilder.add("Msg", errorTitle);
                resultMap.put("AnotherFunction", new ByteArrayInputStream(jsonBuilder.build().toString().getBytes(StandardCharsets.UTF_8)));
            }
        } catch (ProjectionException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            throw ex;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            throw new ProjectionException("IFSMXCORE_ERROR: IFSMXCore exception occurred.", ex);
        }

        return resultMap;
    }

    // Implement your own methods here

}
