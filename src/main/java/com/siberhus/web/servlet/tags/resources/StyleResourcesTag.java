package com.siberhus.web.servlet.tags.resources;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.jsp.JspWriter;

public class StyleResourcesTag extends BaseResourcesTag {
	
	private static final long serialVersionUID = 1L;
	
	/*
	 * <link rel="" href="" type="text/css" title="" media="screen, print" />
	 * <style type="text/css" media="">
	 * bodycontent
	 * </style>
	 * <style type="text/css" media="">
	 * <!--
	 * 	@import url(/css/foo.css);
	 *  bodycontent
	 * -->
	 * </style>
	 */
	private String refType = "link"; //link, embed, import
	/*
	 * 
	 * screen, for presentation on non-paged computer screens;
	 * print, for output to a printer;
	 * projection, for projected presentations;
	 * aural, for speech synthesizers;
	 * braille, for presentation on braille tactile feedback devices;
	 * tty, for character cell displays (using a fixed-pitch font);
	 * tv, for televisions;
	 * all, for all output devices.
	 */
	private String media;
	
	private String title;
	
	private String rel = "stylesheet";
	
	@Override
	public void doStartResourcesTag() throws IOException {
		if(!"link".equals(refType) && !"embed".equals(refType) && !"import".equals(refType)){
			throw new IllegalArgumentException("Unkown refType: "+refType);
		}
		super.doStartResourcesTag();
	}
	
	@Override
	public void render(String resourcePath) throws IOException {
		JspWriter writer = getJspContext().getOut();
		if("link".equals(refType)){
			StringBuilder style = new StringBuilder();
			style.append("<link rel=\"").append(rel).append("\" href=\"")
				.append(resourcePath).append("\" type=\"text/css\" ");
			if(title!=null){
				style.append("title=\"").append(title).append("\"");
			}
			if(media!=null){
				style.append("media=\"").append(media).append("\" ");
			}
			style.append("/>\n");
			writer.write(style.toString());
		}else{
			
			writer.write("<style type=\"text/css\" ");
			if(media!=null){
				writer.write("media=\""+media+"\" ");
			}
			writer.write(">\n");
//			writer.write("<!--\n");
			if("embed".equals(refType)){
				InputStreamReader reader = null;
				try{
					reader = new InputStreamReader(
						new FileInputStream(getResourceInfo().src), getCharset());
					char[] buffer = new char[1024*4];
					int n = 0;
					while (-1 != (n = reader.read(buffer))) {
						writer.write(buffer, 0, n);
					}
				}finally{
					if(reader!=null) try{ reader.close(); }catch(Exception e){}
				}
				writer.write("\n");
			}else if("import".equals(refType)){
				writer.write("@import url("+resourcePath+");\n");
			}
			
			writer.write(getBodyContent());
//			writer.write("\n-->\n");
			writer.write("</style>\n");
		}
	}
	
	@Override
	public Class<? extends BaseResourcesTag> getResourcesClass() {
		return StyleResourcesTag.class;
	}
	
	@Override
	public String getType() {
		return "css";
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}
	
}