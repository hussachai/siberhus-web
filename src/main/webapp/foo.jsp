<%@taglib prefix="res" uri="http://www.siberhus.com/web/tags/resources"%>
<%@page contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<%--
	<res:script src="/js/enhance.js" minify="true"/>
	<res:script src="/js/example.js" minify="true"/>
	<res:script baseDir="/js" src="custom.js" minify="true" merge="true">
	 	<res:script src="enhance" />
		<res:script src="example" minify="true"/>
	 </res:script>
	 
	 
	
	<res:style src="/css/basic" minify="true"/>
	
	<res:style baseDir="/css" src="common.css" minify="true" refType="import">
		<res:style src="basic" />
		<res:style src="visualize" />
	</res:style>
	
	<res:script baseDir="/js" src="jquery.min.js"></res:script>
	<res:script baseDir="/js" src="jquery.validate.js" minify="true"></res:script>
	
	 --%>
	<res:script baseDir="/js" src="custom.js" minify="true" merge="true">
	 	<res:script src="jquery.min.js" />
		<res:script src="jquery.validate.js" minify="true"/>
	</res:script>
	<res:script src="/js/date.js" />
	
</head>
<body>
	Hello
</body>
</html>