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
* Implementation class for all global actions defined in the ${fileName} projection model.
*/
public class ${fileName}ActionsImpl implements ${fileName}Actions {

    private static final String SERVLET_PATH = "";
    public static final Logger LOGGER = Logger.getLogger(${fileName}ActionsImpl.class.getName());

    <#list actions as action>
    @Override
    public Map<String, Object> ${action.functionName}(final Map<String, Object> parameters, final Connection connection) {
        //=======================================================================================================
        //Call Duplicate Task Requirement servlet to duplicate a requirement
        //=======================================================================================================
        ServletRequest initReqRequest = new ServletRequest(SERVLET_PATH);

        <#list action.parameters as param>
        String ${param.varName} = parameters.get("${param.name}").toString();
        </#list>

        // Add parameters to the request, Define the parameters as per the requirement
        <#list action.parameters as param>
        initReqRequest.withParameter("${param.varName}", ${param.varName});
        </#list>

        Map<String, Object> resultMap = new HashMap<>();
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();

        try ( MaintenixGateway gateway = Maintenix.connect(parameters, connection)) {

            ServletResponse initReqResponse = gateway.send(initReqRequest);

            if (initReqResponse) {
                // If response success, implement your own logic here
                resultMap.put("${action.functionName}", new ByteArrayInputStream(jsonBuilder.build().toString().getBytes(StandardCharsets.UTF_8)));
            } else {
                String errorTitle = ResponseUtils.getStringFromJsonObj(ResponseUtils.createJsonObject(initReqResponse.getResultJson()), "detail");
                jsonBuilder.add("Msg", errorTitle);
                resultMap.put("${action.functionName}", new ByteArrayInputStream(jsonBuilder.build().toString().getBytes(StandardCharsets.UTF_8)));
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
    </#list>

    // Implement your own methods here

}
