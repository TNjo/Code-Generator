package com.ifsworld.projection;

import com.ifsworld.fnd.odp.api.exception.ProjectionException;
import com.ifsworld.mxcore.projection.api.Maintenix;
import com.ifsworld.mxcore.projection.api.MaintenixGateway;
import com.ifsworld.mxcore.projection.api.QueryQrxRequest;
import com.ifsworld.mxcore.projection.api.QueryQrxResponse;
import com.ifsworld.mxcore.projection.api.ResponseUtils;
import java.sql.Connection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class GenerationTestFunctionsImpl implements GenerationTestFunctions {


    public static final Logger LOGGER = Logger.getLogger(GenerationTestFunctionsImpl.class.getName());

    private static final String GETREQUIREMENTDETAILS_QRX = "";
    @Override
    public Map<String, Object> GetRequirementDetails(final Map<String, Object> parameters, final Connection connection) {
        String RequirementDefinitionKey = (String) parameters.get("RequirementDefinitionKey");
        String RequirementName = (String) parameters.get("RequirementName");
        String RequirementType = (String) parameters.get("RequirementType");

        //=======================================================================================================
          // Call Query QRX API to get the info that populates Requirement Details page
        //=======================================================================================================
        QueryQrxRequest request = new QueryQrxRequest(GETREQUIREMENTDETAILS_QRX);
        //If you want to bind parameters with request add here
        /*
        request.withParameter("", )
               .withParameter("", );
        */
        try (MaintenixGateway gateway = Maintenix.connect(parameters, connection)) {
            // Connect to Maintenix and send the query request
            QueryQrxResponse response = gateway.send(request);
            // Get JSON response from the query
            String reqDetailsEntity = response.getResultJson();
            // Generate the response in the required format
            String retString = generateResponse(reqDetailsEntity);
            // Map and return the response in Aurena requested format
            return ResponseUtils.mapResponseToAurenaRequestedFormat("GetRequirementDetails", retString);
        } catch (ProjectionException ex) {
            // Handle and log specific projection exceptions
            LOGGER.log(Level.SEVERE, null, ex);
            throw ex;
        } catch (Exception ex) {
            // Handle and log general exceptions
            LOGGER.log(Level.SEVERE, null, ex);
            throw new ProjectionException("IFSMXCORE_ERROR: IFSMXCore exception occurred.", ex);
        }
    }

   // map maintenix response to aurena
   private String generateGetRequirementDetailsResponse(String response) {

        // Initialize a JsonArrayBuilder to construct the JSON array for the response
        final JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        // Convert the input response string to a JsonObject for easy access to its data
        JsonObject entityObject = ResponseUtils.createJsonObject(response);

        // Iterate over each element in the classificationsArray
        if (entityObject.containsKey("data")) {
            // Extract the JsonArray associated with the "data" key
            JsonArray classificationsArray = entityObject.getJsonArray("data");
            // Iterate over each element in the classificationsArray
            for (int i = 0; i < classificationsArray.size(); i++) {
                // Get the current JsonObject from the classificationsArray
                JsonObject classificationObj = classificationsArray.getJsonObject(i);

                // Extract various fields from the current JsonObject using helper methods
                JsonObjectBuilder builder = Json.createObjectBuilder();
                String RequirementDefinitionKey = ResponseUtils.getStringFromJsonObj(classificationObj, "ParameterName");
                String Code = ResponseUtils.getStringFromJsonObj(classificationObj, "ParameterName");
                String Status = ResponseUtils.getStringFromJsonObj(classificationObj, "ParameterName");
                String Name = ResponseUtils.getStringFromJsonObj(classificationObj, "ParameterName");
                String Revision = ResponseUtils.getStringFromJsonObj(classificationObj, "ParameterName");
                String Assembly = ResponseUtils.getStringFromJsonObj(classificationObj, "ParameterName");
                builder.add("RequirementDefinitionKey", RequirementDefinitionKey);
                builder.add("Code", Code);
                builder.add("Status", Status);
                builder.add("Name", Name);
                builder.add("Revision", Revision);
                builder.add("Assembly", Assembly);

                arrayBuilder.add(builder);
            }
        }
        return arrayBuilder.build().toString();
   }
    private static final String ACKNOWLEDGEALERT_QRX = "";
    @Override
    public Map<String, Object> AcknowledgeAlert(final Map<String, Object> parameters, final Connection connection) {
        String Code = (String) parameters.get("Code");
        String AssmblKey = (String) parameters.get("AssmblKey");
        String AssmblBomCd = (String) parameters.get("AssmblBomCd");
        String ClassModeCd = (String) parameters.get("ClassModeCd");
        String Orgkey = (String) parameters.get("Orgkey");
        String RequirementDefinitionKey = (String) parameters.get("RequirementDefinitionKey");
        String ConfigSlotKey = (String) parameters.get("ConfigSlotKey");

        //=======================================================================================================
          // Call Query QRX API to get the info that populates Requirement Details page
        //=======================================================================================================
        QueryQrxRequest request = new QueryQrxRequest(ACKNOWLEDGEALERT_QRX);
        //If you want to bind parameters with request add here
        /*
        request.withParameter("", )
               .withParameter("", );
        */
        try (MaintenixGateway gateway = Maintenix.connect(parameters, connection)) {
            // Connect to Maintenix and send the query request
            QueryQrxResponse response = gateway.send(request);
            // Get JSON response from the query
            String reqDetailsEntity = response.getResultJson();
            // Generate the response in the required format
            String retString = generateResponse(reqDetailsEntity);
            // Map and return the response in Aurena requested format
            return ResponseUtils.mapResponseToAurenaRequestedFormat("AcknowledgeAlert", retString);
        } catch (ProjectionException ex) {
            // Handle and log specific projection exceptions
            LOGGER.log(Level.SEVERE, null, ex);
            throw ex;
        } catch (Exception ex) {
            // Handle and log general exceptions
            LOGGER.log(Level.SEVERE, null, ex);
            throw new ProjectionException("IFSMXCORE_ERROR: IFSMXCore exception occurred.", ex);
        }
    }

   // map maintenix response to aurena
   private String generateAcknowledgeAlertResponse(String response) {

        // Initialize a JsonArrayBuilder to construct the JSON array for the response
        final JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        // Convert the input response string to a JsonObject for easy access to its data
        JsonObject entityObject = ResponseUtils.createJsonObject(response);

        // Iterate over each element in the classificationsArray
        if (entityObject.containsKey("data")) {
            // Extract the JsonArray associated with the "data" key
            JsonArray classificationsArray = entityObject.getJsonArray("data");
            // Iterate over each element in the classificationsArray
            for (int i = 0; i < classificationsArray.size(); i++) {
                // Get the current JsonObject from the classificationsArray
                JsonObject classificationObj = classificationsArray.getJsonObject(i);

                // Extract various fields from the current JsonObject using helper methods
                JsonObjectBuilder builder = Json.createObjectBuilder();
                String Attribute1 = ResponseUtils.getStringFromJsonObj(classificationObj, "ParameterName");
                String Attribute2 = ResponseUtils.getStringFromJsonObj(classificationObj, "ParameterName");
                String Attribute3 = ResponseUtils.getStringFromJsonObj(classificationObj, "ParameterName");
                builder.add("Attribute1", Attribute1);
                builder.add("Attribute2", Attribute2);
                builder.add("Attribute3", Attribute3);

                arrayBuilder.add(builder);
            }
        }
        return arrayBuilder.build().toString();
   }
}
