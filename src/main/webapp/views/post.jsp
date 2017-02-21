<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${postagem.titulo}</title>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/css/style.css"  />" />
</head>

<body>
	<fieldset class="header">
		<h1>LUKE BOSS - Blog de Desenvolvimento Web</h1>
	</fieldset>

	<c:import url="menu.jsp" />
	<br>

	<fieldset>
		<div>
			<div>
				<h2>${postagem.titulo}</h2>
				<p>
					Autor: <a
						href="<c:url value="/autor/${postagem.autor.id}/page/1"/>">${postagem.autor.nome}</a>
					<fmt:parseDate var="date" value="${postagem.dataPostagem}"
						pattern="yyyy-MM-dd'T'HH:mm:ss" />
					postado dia:
					<fmt:formatDate value="${date}" type="both" />
				</p>
			</div>

			<div>
				<p class="post-texto">"${postagem.texto}"</p>
			</div>

			<div>
				<p class="post-categ">
					<span>Categorias: </span>
					<c:forEach var="c" items="${postagem.categorias}">
						<a href="<c:url value="/categoria/${c.permalink}/page/1"/>"
							title="${c.descricao}">${c.descricao}</a>
					</c:forEach>
				</p>
			</div>

			<div class="post-autor">
				<img class="post-avatar"
					src="<c:url value="/avatar/load/${postagem.autor.usuario.avatar.id}"/>">
				<p>
					<strong>${postagem.autor.nome}</strong>
				</p>
				<p>${postagem.autor.biografia}</p>
			</div>
		</div>

		<c:if test="${ logado == null }">
			<p>
				<em>Apenas usuários logados podem comentar neste post.</em>
			</p>
		</c:if>
				
			<c:import url="comments.jsp" />
		
	</fieldset>
</body>
</html>