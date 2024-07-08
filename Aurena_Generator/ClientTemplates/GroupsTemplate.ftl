<#list groups as group>
group ${group.groupName} for ${group.datasourceRef} {
<#if group.label?? && group.label != "">
label = "${group.label}";
    </#if>

    <#list group.fields as field>
    field ${field};
    </#list>
}
</#list>
