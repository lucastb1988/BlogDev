<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Postagem</title>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/css/style.css" />" />
</head>

<body>
	<c:import url="../menu.jsp" />
	<br>

	<c:url var="save" value="/postagem/save" />
	<form:form modelAttribute="postagem" action="${save}" method="post">

		<form:hidden path="id" />
		<fieldset>
			<legend> Cadastro de Postagem</legend>
			<div class="campo">
				<form:label path="titulo">Titulo</form:label>
				<br>
				<form:input path="titulo" type="text" size="35" />
				<form:errors path="titulo" cssClass="error" />
			</div>

			<div class="campo">
				<form:label path="texto">Post</form:label>
				<br>
				<form:textarea path="texto" cols="61" rows="10" />
				<form:errors path="texto" cssClass="error" />
			</div>

			<div class="campo">
				<form:label path="categorias">Selecione a(s) categoria(s)</form:label>
				<br>
				<form:select path="categorias" multiple="true">
					<form:options items="${categorias}" itemValue="id"
						itemLabel="descricao" />
				</form:select>
			</div>

			<div>
				<input type="submit" value="Salvar"> 
				<input type="reset"	value="Limpar">
			</div>
		</fieldset>
	</form:form>
</body>
</html>