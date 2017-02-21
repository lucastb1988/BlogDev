<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Home</title>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/css/style.css" />" />
</head>

<body>
	<fieldset class="header">
		<h1>LUKE BOSS - Blog de Desenvolvimento Web</h1>
	</fieldset>

	<c:import url="menu.jsp" />
	<br>

	<fieldset>
	
	<div>
		<form action=<c:url value="search"/> method="get">
			<input name="texto" type="search" placeholder="Busca por palavra chave">
			<input type="submit" value="Localizar">
		</form>
	</div>
	
		<c:forEach var="p" items="${page.content}">
			<div>
				<div>
					<h2>
						<a href="<c:url value="/${p.permalink}"/>" title="${p.titulo}">${p.titulo}</a>
					</h2>
					<p>
						Autor: <a href="<c:url value="/autor/${p.autor.id}/page/1"/>">${p.autor.nome}</a>
						<fmt:parseDate var="date" value="${p.dataPostagem}"
							pattern="yyyy-MM-dd'T'HH:mm:ss" />
						| Data:
						<fmt:formatDate value="${date}" type="both" />
						| # ${fn:length(p.comentarios)} Comentário(s)
					</p>
				</div>

				<div>
					<p class="post-texto">
						<c:forTokens var="resumo" items="${p.texto}" delims=" " begin="0"
							end="100">
							${resumo}
						</c:forTokens>
						<a href="<c:url value="/${p.permalink}" />">...[Leia Mais]</a>
					</p>
				</div>

				<div>
					<p class="post-categ">
						<span>Categoria(s): </span>
						<c:forEach var="c" items="${p.categorias}">
							<a href="<c:url value="/categoria/${c.permalink}/page/1"/>"
								title="${c.descricao}">${c.descricao}</a>
						</c:forEach>
					</p>
				</div>
			</div>
		</c:forEach>
		
		<c:import url="paginacao.jsp" />
		
	</fieldset>
</body>
</html>