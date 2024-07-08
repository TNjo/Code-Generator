query ${queryName} {
from = "${fromTable}";

<#list attributes as attribute>
   attribute ${attribute.name} ${attribute.type};
</#list>
}
