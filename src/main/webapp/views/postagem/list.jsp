<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Postagens</title>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/css/style.css" />" />
<script type="text/javascript"
	src="<c:url value="/js/jquery-3.1.1.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/postagem.js"/>"></script>
</head>

<body>
	<c:import url="../menu.jsp" />
	<br>

	<fieldset>
		<legend>Lista de Postagens</legend>

		<div>
			<input id="search" type="search" placeholder="Busca por titulo"
				value="">
		</div>

		<table id="table-ajax" class="table" title="${ autorId != null ? autorId : 0 }">
			<jsp:include page="table-rows.jsp" />
		</table>

		<div id="info"></div>

	</fieldset>
</body>
</html>