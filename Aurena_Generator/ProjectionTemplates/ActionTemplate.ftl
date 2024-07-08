<#list actions as action>
action ${action.functionName} {
initialcheck none;
implementation = "Java";

<#list action.parameters as parameter>
   parameter ${parameter.name} ${parameter.type};
</#list>
}
</#list>
