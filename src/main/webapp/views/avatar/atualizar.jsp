<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edição Avatar</title>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/css/style.css" />" />
</head>

<body>
	<c:import url="../menu.jsp" />

	<c:url var="save" value="/avatar/update?${_csrf.parameterName}=${_csrf.token}" />
	<form:form modelAttribute="avatar" action="${save}" method="post"
		enctype="multipart/form-data">
		<form:hidden path="id" />
		<fieldset class="grupo">
			<legend>Edição de Avatar</legend>

			<div class="campo">
				<form:label path="file">Avatar</form:label>
				<br>
				<form:input type="file" path="file" />
				<form:errors path="file" cssClass="error" />
			</div>

			<div>
				<input type="submit" value="Salvar"> 
				<input type="reset"	value="Limpar">
			</div>
		</fieldset>
	</form:form>
</body>
</html>