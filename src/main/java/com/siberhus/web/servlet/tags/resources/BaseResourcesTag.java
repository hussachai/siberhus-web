package com.siberhus.web.servlet.tags.resources;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siberhus.com.yahoo.platform.yui.compressor.YUICompressor;

public abstract class BaseResourcesTag extends SimpleTagSupport {

	private static final long serialVersionUID = 1L;
	
	private final Logger log = LoggerFactory.getLogger(BaseResourcesTag.class);
	
	private String baseDir = "";
	private String src;
	private String name;
	private Boolean merge = Boolean.TRUE;
	private Boolean minify = Boolean.FALSE;
	private String charset = "UTF-8";
	
	private String bodyContent;
	
	private ResourceInfo resourceInfo = new ResourceInfo();
	private String resourcePath;
	private List<BaseResourcesTag> children = new ArrayList<BaseResourcesTag>();
	private static final Map<String, Long> FILE_CHANGE_MAP = new HashMap<String, Long>();
//	private static final Map<String, String> RESOURCE_MAP = new HashMap<String, String>();
	
	public static class ResourceInfo {
		public String srcPath;
		public File src;
		public String destPath;
		public File dest;
		public boolean changesMade = false;
	}
	
	public abstract String getType();
	
	public abstract void render(String resourcePath) throws IOException;
	
	public abstract Class<? extends BaseResourcesTag> getResourcesClass();
	
	public PageContext getPageContext(){
		return (PageContext)getJspContext();
	}
	
	@Override
	public void doTag() throws JspException, IOException{
		doStartResourcesTag();
		JspFragment jspFragment = getJspBody();
		if(jspFragment!=null){
			StringWriter out = new StringWriter();
			jspFragment.invoke(out);
			this.bodyContent = out.toString();
			doBodyResourcesTag();
		}
		doEndResourcesTag();
	}
	
	public void doStartResourcesTag() throws IOException {
		
		if(getParent() instanceof BaseResourcesTag){
			BaseResourcesTag parentTag = (BaseResourcesTag)getParent();
			if("".equals(baseDir)){
				baseDir = parentTag.baseDir;
			}
			parentTag.children.add(this);
		}else{
			log.debug("====================== Resources Bundle ======================");
		}
		
		log.debug("Resource attributes: [baseDir:{}, src:{}, name:{}, merge:{}, minify:{}, charset:{}]",
				new Object[]{baseDir, src, name, merge, minify, charset});
		
		String basePath = baseDir.trim();
		if(!"".equals(baseDir) && !baseDir.endsWith("/")){
			basePath += "/";
		}
		String srcPath = basePath+src.trim();
		//make sure there are .js/.css as a suffix
		if(!srcPath.endsWith("."+getType())){
			srcPath += "."+getType();
		}
		if(getName()==null || getName().trim().equals("")){
			name = "_"+DigestUtils.md5Hex(srcPath);
		}
		ServletContext servletContext = getPageContext().getServletContext();
		
		String srcFilePath = servletContext.getRealPath(srcPath);
		resourceInfo.src = new File(srcFilePath);
		if(srcPath.startsWith("/")){
			resourceInfo.srcPath = servletContext.getContextPath()+srcPath;
		}else{
			resourceInfo.srcPath = srcPath;
		}
		
		String destPath = srcPath.substring(0, srcPath.lastIndexOf("/")+1)+name+"."+getType();
		if(destPath.startsWith("/")){
			resourceInfo.destPath = servletContext.getContextPath()+destPath;
		}else{
			resourceInfo.destPath = destPath;
		}
		String destFilePath = servletContext.getRealPath(destPath);
		resourceInfo.dest = new File(destFilePath);
		//source file overriding protection
		if(resourceInfo.srcPath.equals(resourceInfo.destPath)){
			throw new IllegalArgumentException("Destination file has the same name as source file");
		}
		
		log.debug("ResourceInfo.srcPath: {}", resourceInfo.srcPath);
		log.debug("ResourceInfo.srcFile: {}", resourceInfo.src);
		log.debug("ResourceInfo.destPath: {}", resourceInfo.destPath);
		log.debug("ResourceInfo.destFile: {}", resourceInfo.dest);
		
		resourcePath = resourceInfo.destPath;
		
	}
	
	public void doEndResourcesTag() throws IOException {
		
		boolean changesMade = hasChanges(resourceInfo);
		
		if( (getParent()==null || !getResourcesClass().isAssignableFrom(getParent().getClass())) 
				&& children.size()==0){
			
			if(!resourceInfo.src.exists()){
				throw new FileNotFoundException("Src: "+resourceInfo.src+" not found");
			}
			
			if(changesMade){
				if(minify){
					 minify();
				}else{
					FileUtils.copyFile(resourceInfo.src, resourceInfo.dest);
				}
			}
			render(resourcePath);
		}
		
		long lastModified = resourceInfo.src.lastModified();
		FILE_CHANGE_MAP.put(resourceInfo.src.getAbsolutePath(), lastModified);
	}
	
	public void doBodyResourcesTag() throws JspException, IOException {
		
		log.debug("Children list: {}",children);
		
		//If this tag is parent tag
//		if(baseDir==null){
//			throw new JspException("baseDir is mandatory attribute for parent tag!");
//		}
		this.resourceInfo.changesMade = false;
		for(BaseResourcesTag childTag: children){
			if(childTag.resourceInfo.changesMade){
				this.resourceInfo.changesMade = true;
				break;
			}
		}
		if(merge && this.resourceInfo.changesMade){
			log.debug("Merging resources: {}", children);
			BufferedWriter out = null;
			try{
				out = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(resourceInfo.src), charset));
				for(BaseResourcesTag tag: children){
					BufferedReader in = null;
					try{
						in = new BufferedReader(new InputStreamReader(
							new FileInputStream(tag.resourceInfo.src), charset));
						String line = null;
						while((line=in.readLine())!=null){
							out.append(line+"\n");
						}
						out.flush();
					}finally{
						try{ if(in!=null) in.close(); }catch(Exception e){}
					}
				}
				out.flush();
			}finally{
				try{ if(out!=null) out.close(); }catch(Exception e){}
			}
			if(minify){
				minify();
			}else{
				FileUtils.copyFile(resourceInfo.src, resourceInfo.dest);
			}
		}
		
		if(merge){
			render(resourcePath);
		}else{
			log.debug("Parent resource file: {} was abandoned", resourcePath);
			for(BaseResourcesTag tag: children){
				render(tag.resourcePath);
			}
		}
	}
	
	protected ResourceInfo getResourceInfo(){
		return resourceInfo;
	}
	
	protected String getBodyContent(){
		return bodyContent;
	}
	
	private boolean hasChanges(ResourceInfo resInfo){
		Long lastModified = FILE_CHANGE_MAP.get(resInfo.src.getAbsolutePath());
		if(lastModified!=null){
			log.debug("Resource: {} last modified: {}", 
					new Object[]{resInfo.srcPath, lastModified});
			if(lastModified!=resInfo.src.lastModified()){
				log.debug("Resource: {} was modified at: {}", 
						new Object[]{resInfo.srcPath, 
						resInfo.src.lastModified()});
				resInfo.changesMade = true;
				return true;
			}
		}else{
			resInfo.changesMade = false;
			return false;
		}
		if(getParent()==null && !resInfo.dest.exists()){
			resInfo.changesMade = true;
			return true;
		}
		resInfo.changesMade = false;
		return false;
	}
	
	/**
	 * 
	 * @return minified file
	 */
	private void minify(){
//		String fileName = resourceInfo.src.getAbsolutePath();
//		String minFileName = null;
//		int extLen = getType().length()+1;//include dot
//		int pathLen = fileName.length();//a.min.js/.css
//		if(pathLen>6){
//			String minStr = fileName.substring(pathLen-3-extLen, pathLen-extLen);
//			if("min".equalsIgnoreCase(minStr)){
//				//this file is already minified.
//				log.debug("Resource: {} is already minified", resourceInfo.srcPath);
//				return resourceInfo.srcPath; 
//			}
//		}
//		
//		minFileName = fileName.substring(0,pathLen-extLen);//basename
//		minFileName += (".min."+getType());
//		

		log.debug("Minifying file: {}", resourceInfo.src.getAbsolutePath());
		YUICompressor.main(
			new String[]{resourceInfo.src.getAbsolutePath(),"--type", getType(),
				"--charset","utf-8",
				"-o", resourceInfo.dest.getAbsolutePath()
		});
		log.debug("Minified file: {}", resourceInfo.dest.getAbsolutePath());
		
//		String minResourcePath = resourceInfo.srcPath.substring(0, 
//				resourceInfo.srcPath.length()-extLen)+".min."+getType();
//		log.debug("Minified resource path: {}", minResourcePath);
//		return minResourcePath;
	}
	
	@Override
	public String toString(){
		return resourceInfo.srcPath;
	}
	
	public String getBaseDir() {
		return baseDir;
	}
	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getMerge() {
		return merge;
	}

	public void setMerge(Boolean merge) {
		this.merge = merge;
	}

	public Boolean getMinify() {
		return minify;
	}

	public void setMinify(Boolean minify) {
		this.minify = minify;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
	
}
