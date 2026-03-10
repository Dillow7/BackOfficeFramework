<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.hotel.backoffice.model.Vehicule" %>
<!DOCTYPE html>
<html>
<head>
    <title><%= request.getAttribute("vehicule") != null ? "Modifier" : "Ajouter" %> un véhicule</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=202602112238">
    <link rel="icon" href="data:image/svg+xml,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'><text y='.9em' font-size='90'>🏨</text></svg>">
</head>
<body>
    <header class="app-header">
        <div class="brand">Hotel Manager Pro</div>
        <nav class="nav">
            <a href="${pageContext.request.contextPath}/reservations">Réservations</a>
            <a href="${pageContext.request.contextPath}/vehicules" class="active">Véhicules</a>
        </nav>
    </header>
    
    <div class="container">
        <h1><%= request.getAttribute("vehicule") != null ? "Modifier" : "Ajouter" %> un véhicule</h1>
            <p><%= request.getAttribute("vehicule") != null ? "Mettez à jour les informations du véhicule" : "Ajoutez un nouveau véhicule à votre flotte" %></p>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%= request.getAttribute("error") %></div>
        <% } %>

        <% Vehicule vehicule = (Vehicule) request.getAttribute("vehicule"); %>
        <form method="post" action="${pageContext.request.contextPath}/vehicules/save">
                <% if (vehicule != null) { %>
                    <input type="hidden" name="id" value="<%= vehicule.getId() %>">
                <% } %>
                
                <div class="form-group">
                    <label for="reference">🔖 Référence du véhicule</label>
                    <input type="text" id="reference" name="reference" value="<%= vehicule != null ? vehicule.getReference() : "" %>" required placeholder="Ex: VEH-001" class="form-control" />
                </div>

                <div class="form-group">
                    <label for="nb_place">💺 Nombre de places</label>
                    <input type="number" id="nb_place" name="nb_place" min="1" value="<%= vehicule != null ? vehicule.getNbPlace() : "" %>" required placeholder="Ex: 4" class="form-control" />
                </div>

                <div class="form-group">
                    <label for="type_carburant">⛽ Type de carburant</label>
                    <select id="type_carburant" name="type_carburant" required class="form-control">
                        <option value="">Sélectionner un type</option>
                        <option value="Essence" <%= vehicule != null && "Essence".equals(vehicule.getTypeCarburant()) ? "selected" : "" %>>⛽ Essence</option>
                        <option value="Diesel" <%= vehicule != null && "Diesel".equals(vehicule.getTypeCarburant()) ? "selected" : "" %>>🛢️ Diesel</option>
                        <option value="Électrique" <%= vehicule != null && "Électrique".equals(vehicule.getTypeCarburant()) ? "selected" : "" %>>⚡ Électrique</option>
                        <option value="Hybride" <%= vehicule != null && "Hybride".equals(vehicule.getTypeCarburant()) ? "selected" : "" %>>🔋 Hybride</option>
                    </select>
                </div>

                <div class="actions">
                    <button type="submit" class="btn btn-primary">
                        <span><%= vehicule != null ? "💾" : "➕" %></span> 
                        <%= vehicule != null ? "Mettre à jour" : "Ajouter le véhicule" %>
                    </button>
                    <a href="${pageContext.request.contextPath}/vehicules" class="btn btn-secondary">
                        <span>❌</span> Annuler
                    </a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>