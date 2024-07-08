import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

public class CodeGenRules {

    private static Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);

    static {
        try {
            cfg.setDirectoryForTemplateLoading(new File(".")); // Set to current directory
            cfg.setDefaultEncoding("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String generateNavigatorEntries(Properties properties, int numEntries) throws IOException, TemplateException {
        Map<String, Object> dataModel = new HashMap<>();
        List<Map<String, Object>> navigatorEntries = new ArrayList<>();

        for (int i = 1; i <= numEntries; i++) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("name", properties.getProperty("navigatorEntry" + i + "Name"));
            entry.put("label", properties.getProperty("navigatorEntry" + i + "Label"));
            entry.put("isTopLevel", i == 1);
            entry.put("index", 10);

            if (i == numEntries) {
                entry.put("page", properties.getProperty("navigatorEntry" + i + "PageName"));
            } else {
                entry.put("nextEntry", properties.getProperty("navigatorEntry" + i + "NextEntryName"));
            }

            navigatorEntries.add(entry);
        }

        dataModel.put("navigatorEntries", navigatorEntries);

        StringWriter writer = new StringWriter();
        Template template = cfg.getTemplate("ClientTemplates/NavigatorEntriesTemplate.ftl");
        template.process(dataModel, writer);
        return writer.toString();
    }

    public static String generateMainPage(Properties properties) throws IOException, TemplateException {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("pageName", properties.getProperty("mainPageName"));
        dataModel.put("pageLabel", properties.getProperty("mainPageLabel"));

        int numTabs = Integer.parseInt(properties.getProperty("numTabs"));
        List<Map<String, String>> tabs = new ArrayList<>();
        for (int i = 1; i <= numTabs; i++) {
            Map<String, String> tab = new HashMap<>();
            tab.put("label", properties.getProperty("tab" + i + "Label"));
            tabs.add(tab);
        }
        dataModel.put("tabs", tabs);

        StringWriter writer = new StringWriter();
        Template template = cfg.getTemplate("ClientTemplates/MainPageTemplate.ftl");
        template.process(dataModel, writer);
        return writer.toString();
    }


    public static String generateListSection(Properties properties) {
        Map<String, Object> dataModel = new HashMap<>();
        List<Map<String, Object>> lists = new ArrayList<>();

        // Read number of lists from properties
        int numLists = Integer.parseInt(properties.getProperty("numLists"));

        // Iterate through each list definition
        for (int i = 1; i <= numLists; i++) {
            Map<String, Object> list = new HashMap<>();
            list.put("listName", properties.getProperty("list" + i + "Name"));
            list.put("entitySetName", properties.getProperty("list" + i + "EntitySetName"));
            list.put("listLabel", properties.getProperty("list" + i + "Label"));

            int numFields = Integer.parseInt(properties.getProperty("list" + i + "NumFields"));
            List<String> fields = new ArrayList<>();
            for (int j = 1; j <= numFields; j++) {
                fields.add(properties.getProperty("list" + i + "Field" + j));
            }
            list.put("fields", fields);

            lists.add(list);
        }

        dataModel.put("lists", lists);

        return processTemplate("ClientTemplates/ListTemplate.ftl", dataModel);
    }

    public static String generateVirtualSection(Properties properties) {
        Map<String, Object> dataModel = new HashMap<>();
        List<Map<String, Object>> virtuals = new ArrayList<>();

        int numVirtuals = Integer.parseInt(properties.getProperty("numVirtuals"));
        for (int i = 1; i <= numVirtuals; i++) {
            Map<String, Object> virtual = new HashMap<>();
            virtual.put("virtualName", properties.getProperty("virtual" + i + "Name"));

            int numAttributes = Integer.parseInt(properties.getProperty("virtual" + i + "NumAttributes"));
            List<Map<String, String>> attributes = new ArrayList<>();
            for (int j = 1; j <= numAttributes; j++) {
                Map<String, String> attribute = new HashMap<>();
                String attributeName = properties.getProperty("virtual" + i + "Attribute" + j + "Name");
                String attributeType = properties.getProperty("virtual" + i + "Attribute" + j + "Type");
                attribute.put("name", attributeName != null ? attributeName : "");
                attribute.put("type", attributeType != null ? attributeType : "");
                attributes.add(attribute);
            }
            virtual.put("attributes", attributes);

            virtuals.add(virtual);
        }

        dataModel.put("virtuals", virtuals);

        return processTemplate("ProjectionTemplates/VirtualTemplate.ftl", dataModel);
    }




    public static String generateEntitySet(Properties properties) {
        StringBuilder entitySetSection = new StringBuilder();
        String entitySetName = properties.getProperty("entitySetName");
        String entityName = properties.getProperty("entityName");

        entitySetSection.append("entityset ").append(entitySetName).append(" for ").append(entityName).append(";\n");
        return entitySetSection.toString();
    }

    public static String generateCommandSection(Properties properties) {
        int numCommands = Integer.parseInt(properties.getProperty("numCommands"));
        List<Map<String, Object>> commands = new ArrayList<>();

        for (int i = 1; i <= numCommands; i++) {
            Map<String, Object> commandDataModel = new HashMap<>();
            commandDataModel.put("commandName", properties.getProperty("command" + i + "Name"));
            commandDataModel.put("commandLabel", properties.getProperty("command" + i + "Label"));
            commandDataModel.put("functionName", properties.getProperty("command" + i + "FunctionName"));

            int numCommandParameters = Integer.parseInt(properties.getProperty("command" + i + "NumParameters"));
            List<Map<String, String>> parameters = new ArrayList<>();
            for (int j = 1; j <= numCommandParameters; j++) {
                Map<String, String> parameter = new HashMap<>();
                parameter.put("name", properties.getProperty("command" + i + "Parameter" + j + "Name"));
                parameter.put("type", properties.getProperty("command" + i + "Parameter" + j + "Type"));
                parameters.add(parameter);
            }
            commandDataModel.put("parameters", parameters);
            commands.add(commandDataModel);
        }

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("commands", commands);

        return processTemplate("ClientTemplates/CommandTemplate.ftl", dataModel);
    }


    public static String generateActionSection(Properties properties) {
        int numCommands = Integer.parseInt(properties.getProperty("numCommands"));
        List<Map<String, Object>> actions = new ArrayList<>();

        for (int i = 1; i <= numCommands; i++) {
            Map<String, Object> actionDataModel = new HashMap<>();
            actionDataModel.put("functionName", properties.getProperty("command" + i + "FunctionName"));

            int numActionParameters = Integer.parseInt(properties.getProperty("command" + i + "NumParameters"));
            List<Map<String, String>> parameters = new ArrayList<>();
            for (int j = 1; j <= numActionParameters; j++) {
                Map<String, String> parameter = new HashMap<>();
                parameter.put("name", properties.getProperty("command" + i + "Parameter" + j + "Name"));
                parameter.put("type", properties.getProperty("command" + i + "Parameter" + j + "Type"));
                parameters.add(parameter);
            }
            actionDataModel.put("parameters", parameters);
            actions.add(actionDataModel);
        }

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("actions", actions);

        return processTemplate("ProjectionTemplates/ActionTemplate.ftl", dataModel);
    }



    public static String generateQuerySection(Properties properties) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("queryName", properties.getProperty("queryName"));
        dataModel.put("fromTable", properties.getProperty("fromTable"));

        int numAttributes = Integer.parseInt(properties.getProperty("numQueryAttributes"));
        List<Map<String, String>> attributes = new ArrayList<>();
        for (int i = 1; i <= numAttributes; i++) {
            Map<String, String> attribute = new HashMap<>();
            attribute.put("name", properties.getProperty("queryAttribute" + i + "Name"));
            attribute.put("type", properties.getProperty("queryAttribute" + i + "Type"));
            attributes.add(attribute);
        }
        dataModel.put("attributes", attributes);

        return processTemplate("ProjectionTemplates/QueryTemplate.ftl", dataModel);
    }


    public static String generateStructureSection(Properties properties) {
        int numStructures = Integer.parseInt(properties.getProperty("numStructures"));
        StringBuilder structures = new StringBuilder();

        for (int i = 1; i <= numStructures; i++) {
            Map<String, Object> dataModel = new HashMap<>();
            dataModel.put("structureName", properties.getProperty("structure" + i + "Name"));

            int numAttributes = Integer.parseInt(properties.getProperty("structure" + i + "NumAttributes"));
            List<Map<String, String>> attributes = new ArrayList<>();
            for (int j = 1; j <= numAttributes; j++) {
                Map<String, String> attribute = new HashMap<>();
                attribute.put("name", properties.getProperty("structure" + i + "Attribute" + j + "Name"));
                attribute.put("type", properties.getProperty("structure" + i + "Attribute" + j + "Type"));
                attributes.add(attribute);
            }
            dataModel.put("attributes", attributes);

            structures.append(processTemplate("ProjectionTemplates/StructureTemplate.ftl", dataModel));
            structures.append("\n");
        }

        return structures.toString();
    }



    public static String generateFunctionSection(Properties properties) {
        List<Map<String, Object>> functions = new ArrayList<>();

        // Read number of functions from properties
        int numFunctions = Integer.parseInt(properties.getProperty("numFunctions"));

        // Iterate through each function definition
        for (int i = 1; i <= numFunctions; i++) {
            Map<String, Object> function = new HashMap<>();
            function.put("functionName", properties.getProperty("function" + i + "Name"));
            function.put("structureName", properties.getProperty("function" + i + "structureName"));
            int numParameters = Integer.parseInt(properties.getProperty("function" + i + "NumParameters"));
            List<Map<String, String>> parameters = new ArrayList<>();
            for (int j = 1; j <= numParameters; j++) {
                Map<String, String> parameter = new HashMap<>();
                parameter.put("name", properties.getProperty("function" + i + "Parameter" + j + "Name"));
                parameter.put("type", properties.getProperty("function" + i + "Parameter" + j + "Type"));
                parameters.add(parameter);
            }
            function.put("parameters", parameters);

            functions.add(function);
        }

        // Prepare the data model for the template
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("functions", functions);

        // Generate the function sections using the template
        return processTemplate("ProjectionTemplates/FunctionTemplate.ftl", dataModel);
    }

    public static String generateGroupsSection(Properties properties) {
        Map<String, Object> dataModel = new HashMap<>();
        List<Map<String, Object>> groups = new ArrayList<>();

        int numGroups = Integer.parseInt(properties.getProperty("numGroups"));
        for (int i = 1; i <= numGroups; i++) {
            Map<String, Object> group = new HashMap<>();
            group.put("groupName", properties.getProperty("group" + i + "Name"));
            group.put("datasourceRef", properties.getProperty("group" + i + "DatasourceRef"));
            group.put("label", properties.getProperty("group" + i + "Label"));

            int numFields = Integer.parseInt(properties.getProperty("group" + i + "NumFields"));
            List<String> fields = new ArrayList<>();
            for (int j = 1; j <= numFields; j++) {
                fields.add(properties.getProperty("group" + i + "Field" + j));
            }
            group.put("fields", fields);

            groups.add(group);
        }

        dataModel.put("groups", groups);

        return processTemplate("ClientTemplates/GroupsTemplate.ftl", dataModel);
    }

    public static String generateSingletonSection(Properties properties) {
        Map<String, Object> dataModel = new HashMap<>();
        List<Map<String, String>> singletons = new ArrayList<>();

        int numSingletons = Integer.parseInt(properties.getProperty("numSingletons"));

        for (int i = 1; i <= numSingletons; i++) {
            Map<String, String> singleton = new HashMap<>();
            singleton.put("singletonName", properties.getProperty("singleton" + i + "Name"));
            singleton.put("structureName", properties.getProperty("singleton" + i + "StructureName"));
            singletons.add(singleton);
        }

        dataModel.put("singletons", singletons);

        return processTemplate("ClientTemplates/SingletonTemplate.ftl", dataModel);
    }


    public static void generateJavaClass(Properties properties) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("fileName", properties.getProperty("fileName"));

        int numStructures = Integer.parseInt(properties.getProperty("numStructures"));
        Map<String, List<Map<String, String>>> structures = new HashMap<>();

        // Read structures
        for (int i = 1; i <= numStructures; i++) {
            List<Map<String, String>> attributes = new ArrayList<>();
            int numAttributes = Integer.parseInt(properties.getProperty("structure" + i + "NumAttributes"));
            for (int j = 1; j <= numAttributes; j++) {
                Map<String, String> attribute = new HashMap<>();
                attribute.put("name", properties.getProperty("structure" + i + "Attribute" + j + "Name"));
                attribute.put("type", properties.getProperty("structure" + i + "Attribute" + j + "Type"));
                attributes.add(attribute);
            }
            structures.put(properties.getProperty("structure" + i + "Name"), attributes);
        }

        int numFunctions = Integer.parseInt(properties.getProperty("numFunctions"));
        List<Map<String, Object>> functions = new ArrayList<>();

        // Read functions
        for (int i = 1; i <= numFunctions; i++) {
            Map<String, Object> functionData = new HashMap<>();
            functionData.put("functionName", properties.getProperty("function" + i + "Name"));

            int numParameters = Integer.parseInt(properties.getProperty("function" + i + "NumParameters"));
            List<Map<String, String>> parameters = new ArrayList<>();
            for (int j = 1; j <= numParameters; j++) {
                Map<String, String> parameter = new HashMap<>();
                parameter.put("name", properties.getProperty("function" + i + "Parameter" + j + "Name"));
                parameter.put("type", properties.getProperty("function" + i + "Parameter" + j + "Type"));
                parameters.add(parameter);
            }
            functionData.put("functionParameters", parameters);

            // Map structure attributes to function
            String structureName = properties.getProperty("function" + i + "structureName");
            functionData.put("structureAttributes", structures.get(structureName));

            functions.add(functionData);
        }

        dataModel.put("functions", functions);

        writeFileFromTemplate("JavaTemplates/FunctionClassTemplate.ftl", properties.getProperty("fileName") + "FunctionsImpl.java", dataModel);
    }


    public static void generateActionClass(Properties properties) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("fileName", properties.getProperty("fileName"));

        int numCommands = Integer.parseInt(properties.getProperty("numCommands"));
        List<Map<String, Object>> actions = new ArrayList<>();

        for (int i = 1; i <= numCommands; i++) {
            Map<String, Object> actionDataModel = new HashMap<>();
            actionDataModel.put("functionName", properties.getProperty("command" + i + "FunctionName"));

            int numActionParameters = Integer.parseInt(properties.getProperty("command" + i + "NumParameters"));
            List<Map<String, String>> parameters = new ArrayList<>();
            for (int j = 1; j <= numActionParameters; j++) {
                Map<String, String> parameter = new HashMap<>();
                parameter.put("name", properties.getProperty("command" + i + "Parameter" + j + "Name"));
                parameter.put("type", properties.getProperty("command" + i + "Parameter" + j + "Type"));
                parameter.put("varName", properties.getProperty("command" + i + "Parameter" + j + "Name").substring(0, 1).toLowerCase() + properties.getProperty("command" + i + "Parameter" + j + "Name").substring(1));
                parameters.add(parameter);
            }
            actionDataModel.put("parameters", parameters);
            actions.add(actionDataModel);
        }

        dataModel.put("actions", actions);

        writeFileFromTemplate("JavaTemplates/ActionClassTemplate.ftl", properties.getProperty("fileName") + "ActionsImpl.java", dataModel);
    }




    public static void generateClientFile(Properties properties) throws TemplateException, IOException {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("fileName", properties.getProperty("fileName"));
        dataModel.put("description", properties.getProperty("description"));

        String addNavigator = properties.getProperty("addNavigator").toLowerCase();
        if (addNavigator.equals("yes") || addNavigator.equals("y")) {
            int numEntries = Integer.parseInt(properties.getProperty("numNavigatorEntries"));
            dataModel.put("navigatorEntries", generateNavigatorEntries(properties, numEntries));
        } else {
            dataModel.put("navigatorEntries", "");
        }

        String addMainPage = properties.getProperty("addMainPage").toLowerCase();
        if (addMainPage.equals("yes") || addMainPage.equals("y")) {
            dataModel.put("mainPages", generateMainPage(properties));
        } else {
            dataModel.put("mainPages", "");
        }

        String addList = properties.getProperty("addList").toLowerCase();
        if (addList.equals("yes") || addList.equals("y")) {
            dataModel.put("listSection", generateListSection(properties));
        } else {
            dataModel.put("listSection", "");
        }

        String addCommand = properties.getProperty("addCommand").toLowerCase();
        if (addCommand.equals("yes") || addCommand.equals("y")) {
            dataModel.put("commandSection", generateCommandSection(properties));
        } else {
            dataModel.put("commandSection", "");
        }

        String addGroups = properties.getProperty("addGroups").toLowerCase();
        if (addGroups.equals("yes") || addGroups.equals("y")) {
            dataModel.put("groupsSection", generateGroupsSection(properties));
        } else {
            dataModel.put("groupsSection", "");
        }

        String addSingleton = properties.getProperty("addSingleton").toLowerCase();
        if (addSingleton.equals("yes") || addSingleton.equals("y")) {
            dataModel.put("singletonSection", generateSingletonSection(properties));
        } else {
            dataModel.put("singletonSection", "");
        }

        writeFileFromTemplate("MasterTemplates/MasterClientTemplate.ftl", properties.getProperty("fileName") + ".client", dataModel);
    }

    public static void generateProjectionFile(Properties properties) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("fileName", properties.getProperty("fileName"));
        dataModel.put("description", properties.getProperty("description"));

        dataModel.put("entitySetSection", generateEntitySet(properties));

        String addCommand = properties.getProperty("addCommand").toLowerCase();
        if (addCommand.equals("yes") || addCommand.equals("y")) {
            dataModel.put("actionSection", generateActionSection(properties));
            String addActionImpl = properties.getProperty("addActionImpl").toLowerCase();
            if(addActionImpl.equals("yes") || addActionImpl.equals("y") ){
                generateActionClass(properties);
            }
        } else {
            dataModel.put("actionSection", "");
        }

        String addQuery = properties.getProperty("addQuery").toLowerCase();
        if (addQuery.equals("yes") || addQuery.equals("y")) {
            dataModel.put("querySection", generateQuerySection(properties));
        } else {
            dataModel.put("querySection", "");
        }

        String addStructure = properties.getProperty("addStructure").toLowerCase();
        if (addStructure.equals("yes") || addStructure.equals("y")) {
            dataModel.put("structureSection", generateStructureSection(properties));
        } else {
            dataModel.put("structureSection", "");
        }

        String addFunction = properties.getProperty("addFunction").toLowerCase();
        if (addFunction.equals("yes") || addFunction.equals("y")) {
            dataModel.put("functionSection", generateFunctionSection(properties));
            generateJavaClass(properties);
        } else {
            dataModel.put("functionSection", "");
        }

        String addVirtuals = properties.getProperty("addVirtuals").toLowerCase();
        if (addVirtuals.equals("yes") || addVirtuals.equals("y")) {
            dataModel.put("virtualSection", generateVirtualSection(properties));
        } else {
            dataModel.put("virtualSection", "");
        }



        writeFileFromTemplate("MasterTemplates/MasterProjectionTemplate.ftl", properties.getProperty("fileName") + "Impl.projection", dataModel);
    }

    private static void writeFileFromTemplate(String templateFileName, String outputFileName, Map<String, Object> dataModel) {
        try (FileWriter writer = new FileWriter(outputFileName)) {
            Template template = cfg.getTemplate(templateFileName);
            template.process(dataModel, writer);
        } catch (IOException | TemplateException e) {
            System.out.println("An error occurred while writing the file: " + outputFileName);
            e.printStackTrace();
        }
    }


    private static String processTemplate(String templateFileName, Map<String, Object> dataModel) {
        try {
            Template template = cfg.getTemplate(templateFileName);
            StringWriter writer = new StringWriter();
            template.process(dataModel, writer);
            return writer.toString();
        } catch (IOException | TemplateException e) {
            System.out.println("An error occurred while processing the template: " + templateFileName);
            e.printStackTrace();
        }
        return "";
    }

}
