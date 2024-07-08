<#list virtuals as virtual>
virtual ${virtual.virtualName} {
<#list virtual.attributes as attribute>
<#if attribute.name?exists && attribute.type?exists>
attribute ${attribute.name} ${attribute.type};
<#else>
<!-- Handle missing attribute.name or attribute.type -->
<!-- Example: attribute UnknownName UnknownType; -->
</#if>
</#list>
}
</#list>
