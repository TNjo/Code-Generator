navigator {
<#list navigatorEntries as entry>
entry ${entry.name}<#if entry.isTopLevel> toplevel at index ${entry.index}</#if> {
label = "${entry.label}";
        <#if entry.page??>
        page ${entry.page};
        <#elseif entry.nextEntry??>
        entry ${entry.nextEntry};
        </#if>
    }
</#list>
}
