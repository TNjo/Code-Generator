<#list commands as command>
command ${command.commandName} {
label = "${command.commandLabel}";

   execute {
call ${command.functionName}(
      <#list command.parameters as parameter>
      ${parameter.name}<#if parameter_has_next>, </#if>
      </#list>
      );
      success("${command.commandLabel}");
      exit OK;
   }
}
</#list>
