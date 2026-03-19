<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Nouvelle réservation</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=202602112238">
    <link rel="icon" href="data:image/svg+xml,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'><text y='.9em' font-size='90'>🏨</text></svg>">
</head>
<body>
    <header class="app-header">
        <div class="brand">3176-3186-3139</div>
        <nav class="nav">
            <a href="${pageContext.request.contextPath}/reservations">Réservations</a>
            <a href="${pageContext.request.contextPath}/reservations/new" class="active">Nouvelle réservation</a>
            <a href="${pageContext.request.contextPath}/vehicules">Véhicules</a>
        </nav>
    </header>
    
    <div class="container">
            <h1>Nouvelle réservation</h1>
            <p>Ajoutez une nouvelle réservation pour vos clients</p>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%= request.getAttribute("error") %></div>
        <% } %>
        <% if (request.getAttribute("success") != null) { %>
            <div class="alert alert-success"><%= request.getAttribute("success") %></div>
        <% } %>

        <form method="post" action="${pageContext.request.contextPath}/reservations/new">
            <div class="form-group">
                <label for="id_client">👤 ID Client (4 caractères)</label>
                <input type="text" id="id_client" name="id_client" maxlength="4" required placeholder="Ex: CLI1" class="form-control" />
            </div>

            <div class="form-group">
                <label for="nb_passager">👥 Nombre de passagers</label>
                <input type="number" id="nb_passager" name="nb_passager" min="1" required placeholder="Ex: 2" class="form-control" />
            </div>

            <div class="form-group">
                <label for="date_heure_arrive">📅 Date et heure d'arrivée avion</label>
                <input type="datetime-local" id="date_heure_arrive" name="date_heure_arrive" required class="form-control" />
            </div>

            <div class="form-group">
                <label for="id_hotel">🏨 ID Hôtel</label>
                <input type="number" id="id_hotel" name="id_hotel" min="1" required placeholder="Ex: 1" class="form-control" />
            </div>

            <div class="actions">
                <button type="submit" class="btn btn-primary">
                    <span>💾</span> Enregistrer la réservation
                </button>
                <a href="${pageContext.request.contextPath}/reservations" class="btn btn-secondary">
                    <span>📋</span> Voir les assignations
                </a>
                <a href="${pageContext.request.contextPath}/reservations/new" class="btn btn-secondary">
                    <span>🔄</span> Réinitialiser le formulaire
                </a>
            </div>
        </form>
    </div>
</body>
</html>
