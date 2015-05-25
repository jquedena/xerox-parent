<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ page language="java" contentType="text/html; charset=UTF8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="expires" content="-1"/>
    <tiles:insertAttribute name="script" ignore="true"/>
    <title><tiles:insertAttribute name="title" ignore="true"/></title>
</head>
<body>
<div id="layout" class="hide">
    <tiles:insertAttribute name="head"/>
    <tiles:insertAttribute name="menu"/>
    <div id="body">
        <tiles:insertAttribute name="body"/>
    </div>
    <div id="panelMensaje"></div>
    <div id="jqLoad" class="loading" style="display:none;text-align:center;">
        <div class="loadingContent">
            <div class="loadingImage"></div>
            <span class="loadingText">Cargando</span>
        </div>
    </div>
    <tiles:insertAttribute name="foot"/>
</div>
</body>
</html>