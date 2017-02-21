<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div id="comentarios">
	<security:authorize access="hasAnyAuthority('ADMIN','AUTOR','LEITOR')">
		<c:url var="save" value="/comentario/save" />
		<form:form modelAttribute="comentario" action="${save}" method="post">
			<input type="hidden" value="${postagem.permalink}" name="permalink">
			<div>
				<form:label path="texto">
					<h3 align="center">DEIXE UMA RESPOSTA</h3>
					<h3 align="center">&darr;</h3>
					<h4 align="center">Comentário</h4>
				</form:label>
				<form:textarea path="texto" rows="6" cols="262" />
				<form:errors path="texto" cssClass="error" />
			</div>

			<div>
				<input type="submit" value="Enviar Comentário">
			</div>
			<br>
		</form:form>
	</security:authorize>

	<hr>
	<c:forEach var="c" items="${postagem.comentarios}">
		<div class="comentarios">
			<img class="comentarios-avatar"
				src="<c:url value="/avatar/load/${c.usuario.avatar.id}"/>"> 
			<em>${c.usuario.nome} - 
				<fmt:parseDate var="date" value="${c.dataComentario}" pattern="yyyy-MM-dd'T'HH:mm:ss" /> 
				<fmt:formatDate	value="${date}" type="both" />
			</em>

			<p class="post-texto">${c.texto}</p>
		</div>
	</c:forEach>
</div>