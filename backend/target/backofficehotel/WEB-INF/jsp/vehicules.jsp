<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hotel.backoffice.model.Vehicule" %>
<!DOCTYPE html>
<html>
<head>
    <title>Gestion des véhicules</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=202602112238">
    <link rel="icon" href="data:image/svg+xml,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'><text y='.9em' font-size='90'>🏨</text></svg>">
</head>
<body>
    <header class="app-header">
        <div class="brand">3176-3186-3139</div>
        <nav class="nav">
            <a href="${pageContext.request.contextPath}/reservations">Réservations</a>
            <a href="${pageContext.request.contextPath}/vehicules" class="active">Véhicules</a>
        </nav>
    </header>
    
    <div class="container">
        <h1>Gestion des véhicules</h1>
            <p>Consultez et gérez votre flotte de véhicules</p>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%= request.getAttribute("error") %></div>
        <% } %>
        <% if (request.getAttribute("success") != null) { %>
            <div class="alert alert-success"><%= request.getAttribute("success") %></div>
        <% } %>

        <div class="table-wrapper">
            <table>
                <thead>
                    <tr>
                        <th>🆔 ID</th>
                        <th>🔖 Référence</th>
                        <th>💺 Places</th>
                        <th>⛽ Carburant</th>
                        <th>🔧 Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% List<Vehicule> vehicules = (List<Vehicule>) request.getAttribute("vehicules");
                       if (vehicules != null && !vehicules.isEmpty()) {
                           for (Vehicule v : vehicules) { %>
                    <tr>
                        <td><span class="badge badge--muted">#<%= v.getId() %></span></td>
                        <td><strong><%= v.getReference() %></strong></td>
                        <td><span class="badge badge--accent"><%= v.getNbPlace() %> places</span></td>
                        <td>
                            <% String type = v.getTypeCarburant();
                               String icon = "";
                               String badgeClass = "badge--muted";
                               if ("Essence".equals(type)) { icon = "⛽"; badgeClass = "badge--warning"; }
                               else if ("Diesel".equals(type)) { icon = "🛢️"; badgeClass = "badge--muted"; }
                               else if ("Électrique".equals(type)) { icon = "⚡"; badgeClass = "badge--success"; }
                               else if ("Hybride".equals(type)) { icon = "🔋"; badgeClass = "badge--info"; } %>
                            <span class="badge <%= badgeClass %>"><%= icon %> <%= type %></span>
                        </td>
                        <td>
                            <div class="actions">
                                <a href="${pageContext.request.contextPath}/vehicules/edit?id=<%= v.getId() %>" class="btn btn-secondary">
                                    <span>✏️</span> Modifier
                                </a>
                                <form method="post" action="${pageContext.request.contextPath}/vehicules/delete" style="display:inline;">
                                    <input type="hidden" name="id" value="<%= v.getId() %>">
                                    <button type="submit" class="btn btn-danger" onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce véhicule ?')">
                                        <span>🗑️</span> Supprimer
                                    </button>
                                </form>
                            </div>
                        </td>
                    </tr>
                    <%     }
                       } else { %>
                    <tr>
                        <td colspan="5">
                                <div class="empty-message">
                                    <span class="empty-icon">🚗</span>
                                    <div class="empty-title">Aucun véhicule trouvé</div>
                                    <div class="empty-text">Commencez par ajouter votre premier véhicule</div>
                                </div>
                            </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>