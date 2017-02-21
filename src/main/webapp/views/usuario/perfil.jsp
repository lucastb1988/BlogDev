<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Perfil</title>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/css/style.css" />" />
</head>

<body>
	<c:import url="../menu.jsp" />
	<br>

	<fieldset>
		<legend>Perfil</legend>

		<table class="table">
			<tr>
				<th>Avatar</th>
				<th>Nome do Usuário</th>
				<th>E-mail</th>
				<th>Data de Cadastro</th>
				<th>Perfil</th>
				<th>Ação</th>
			</tr>

			<tr>
				<td>
					<a href="<c:url value="/avatar/update/${usuario.avatar.id}"/>" title="Editar Avatar"> 
					<img src="<c:url value="/avatar/load/${usuario.avatar.id}"/>" style="width: 64px; height: 64px;" />
					</a>
				</td>
				<td>${usuario.nome}</td>
				<td>${usuario.email}</td>
				<td>${usuario.dataCadastro}</td>
				<td>${usuario.perfil}</td>
				<td>
					<c:url var="update" value="/usuario/update/${usuario.id}" />
					<a href="${update}" title="Editar">&#9445</a> 
					
					<c:url var="delete" value="/usuario/delete/${usuario.id}" />
					<a href="${delete}"	title="Excluir">&#9447</a>
				</td>
			</tr>
		</table>
	</fieldset>
</body>
</html>