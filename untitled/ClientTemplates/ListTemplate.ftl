<#list lists as list>
list ${list.listName} for ${list.entitySetName} {
label = "${list.listLabel}";

    <#list list.fields as field>
    field ${field};
    </#list>
}
</#list>
