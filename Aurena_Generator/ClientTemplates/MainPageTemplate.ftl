page ${pageName} {
    label = "${pageLabel}";

    <#if tabs?? && tabs?size gt 0>
    tabs {
        <#list tabs as tab>
        tab {
            label = "${tab.label}";
        }
        </#list>
    }
    </#if>
}
