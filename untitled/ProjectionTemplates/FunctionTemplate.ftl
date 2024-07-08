<#list functions as function>
function ${function.functionName} List<Structure(${function.structureName})> {
initialcheck none;
implementation = "Java";

<#list function.parameters as parameter>
   parameter ${parameter.name} ${parameter.type};
</#list>
   syncpolicy Online;
}
</#list>
