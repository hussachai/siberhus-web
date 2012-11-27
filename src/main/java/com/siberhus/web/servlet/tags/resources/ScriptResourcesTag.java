package com.siberhus.web.servlet.tags.resources;

import java.io.IOException;
/*

ui.accordion
ui.table

<ui:script baseDir="" file="" merged="true" minified="true">
	<ui:script baseDir="" file=""/>
	<ui:script file=""/>
	<ui:script file=""/>
</ui:script>
<ui:style ...>

 */
public class ScriptResourcesTag extends BaseResourcesTag {
	
	private static final long serialVersionUID = 1L;
	
	public void render(String resourcePath) throws IOException {
		String script = "<script type=\"text/javascript\" src=\""+resourcePath
			+ "\" charset=\""+getCharset()+"\"></script>\n";
		getJspContext().getOut().write(script);
	}
	
	@Override
	public String getType() {
		return "js";
	}
	
	@Override
	public Class<? extends BaseResourcesTag> getResourcesClass() {
		return ScriptResourcesTag.class;
	}
	
}