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

public class ${fileName}FunctionsImpl implements ${fileName}Functions {


    public static final Logger LOGGER = Logger.getLogger(${fileName}FunctionsImpl.class.getName());

<#list functions as function>
    private static final String ${function.functionName?upper_case}_QRX = "";
    @Override
    public Map<String, Object> ${function.functionName}(final Map<String, Object> parameters, final Connection connection) {
        <#-- Generate parameter variables -->
        <#list function.functionParameters as param>
        String ${param.name} = (String) parameters.get("${param.name}");
        </#list>

        //=======================================================================================================
          // Call Query QRX API to get the info that populates Requirement Details page
        //=======================================================================================================
        QueryQrxRequest request = new QueryQrxRequest(${function.functionName?upper_case}_QRX);
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
            String retString = generate${function.functionName}Response(reqDetailsEntity);
            // Map and return the response in Aurena requested format
            return ResponseUtils.mapResponseToAurenaRequestedFormat("${function.functionName}", retString);
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
   private String generate${function.functionName}Response(String response) {

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
                <#list function.structureAttributes as attribute>
                String ${attribute.name} = ResponseUtils.getStringFromJsonObj(classificationObj, "ParameterName");
                </#list>
                <#list function.structureAttributes as attribute>
                builder.add("${attribute.name}", ${attribute.name});
                </#list>

                arrayBuilder.add(builder);
            }
        }
        return arrayBuilder.build().toString();
   }
</#list>
}
