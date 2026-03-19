<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="com.hotel.backoffice.model.TransferAssignment" %>
<!DOCTYPE html>
<html>
<head>
    <title>Assignation des véhicules</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=2026030301">
    <link rel="icon" href="data:image/svg+xml,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'><text y='.9em' font-size='90'>🏨</text></svg>">
</head>
<body>
    <header class="app-header">
        <div class="brand">3176-3186-3139</div>
        <nav class="nav">
            <a href="${pageContext.request.contextPath}/reservations" class="active">Réservations</a>
            <a href="${pageContext.request.contextPath}/reservations/new">Nouvelle réservation</a>
            <a href="${pageContext.request.contextPath}/vehicules">Véhicules</a>
        </nav>
    </header>

    <div class="container">
        <h1>Assignation des véhicules</h1>
        <p>Transferts aéroport vers hôtel avec filtre par date</p>

        <% String selectedDate = (String) request.getAttribute("selectedDate");
           Double vitesseMoyenne = (Double) request.getAttribute("vitesseMoyenne");
           Integer tempsAttenteMax = (Integer) request.getAttribute("tempsAttenteMax");
           Boolean bufferActif = (Boolean) request.getAttribute("bufferActif");
           Integer bufferMinutes = (Integer) request.getAttribute("bufferMinutes");
           Integer totalReservations = (Integer) request.getAttribute("totalReservations");
           Integer totalTrajets = (Integer) request.getAttribute("totalTrajets");
           Integer assignedCount = (Integer) request.getAttribute("assignedCount");
           Integer unassignedCount = (Integer) request.getAttribute("unassignedCount");
           List<TransferAssignment> assignations = (List<TransferAssignment>) request.getAttribute("assignations");
           List<TransferAssignment> nonAssignees = (List<TransferAssignment>) request.getAttribute("nonAssignees");
           if (assignations == null) assignations = new ArrayList<>();
           if (nonAssignees == null) nonAssignees = new ArrayList<>();
           if (totalReservations == null) totalReservations = assignations.size() + nonAssignees.size();
           if (totalTrajets == null) totalTrajets = 0;
           if (assignedCount == null) assignedCount = assignations.size();
           if (unassignedCount == null) unassignedCount = nonAssignees.size();
           DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        %>

        <div style="display:flex; gap:12px; flex-wrap:wrap; justify-content:space-between; margin: 0 0 16px 0; align-items:flex-end;">
            <form method="get" action="${pageContext.request.contextPath}/reservations" style="display:flex; gap:8px; flex-wrap:wrap; align-items:flex-end;">
                <div class="form-group" style="margin:0;">
                    <label for="date">Date des arrivées avion</label>
                    <input class="form-control" type="date" id="date" name="date" value="<%= selectedDate != null ? selectedDate : "" %>" required>
                </div>
                <button type="submit" class="btn btn-primary">Filtrer</button>
            </form>

            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/reservations/new">Ajouter une réservation</a>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%= request.getAttribute("error") %></div>
        <% } %>
        <% if (request.getAttribute("success") != null) { %>
            <div class="alert alert-success"><%= request.getAttribute("success") %></div>
        <% } %>

        <div style="display:flex; gap:8px; flex-wrap:wrap; margin-bottom:16px;">
            <span class="badge badge--info">Date: <%= selectedDate != null ? selectedDate : "-" %></span>
            <span class="badge badge--muted">Vitesse moyenne: <%= vitesseMoyenne != null ? String.format(Locale.US, "%.1f", vitesseMoyenne) : "-" %> km/h</span>
            <span class="badge badge--muted">Temps attente max: <%= tempsAttenteMax != null ? tempsAttenteMax : "-" %> min</span>
            <span class="badge badge--muted">Buffer départ: <%= (bufferActif != null && bufferActif) ? "actif (" + (bufferMinutes != null ? bufferMinutes : 0) + " min)" : "désactivé" %></span>
            <span class="badge badge--accent">Total réservations: <%= totalReservations %></span>
            <span class="badge badge--info">Trajets planifiés: <%= totalTrajets %></span>
            <span class="badge badge--success">Assignées: <%= assignedCount %></span>
            <span class="badge badge--danger">Non assignées: <%= unassignedCount %></span>
        </div>

        <h2 style="margin: 0 0 10px 0;">Réservations assignées</h2>
        <div class="table-wrapper">
            <table>
                <thead>
                    <tr>
                        <th>Réservation</th>
                        <th>Lot</th>
                        <th>Passagers</th>
                        <th>Parcours</th>
                        <th>Distance</th>
                        <th>Départ aéroport</th>
                        <th>Arrivée hôtel</th>
                        <th>Détails trajet</th>
                        <th>Véhicule</th>
                    </tr>
                </thead>
                <tbody>
                <% if (!assignations.isEmpty()) {
                       for (TransferAssignment a : assignations) { %>
                    <tr>
                        <td>
                            <strong>#<%= a.getReservationId() %></strong><br>
                            Client <%= a.getIdClient() %>
                        </td>
                        <td>
                            <span class="badge badge--info">T<%= a.getTrajetId() != null ? a.getTrajetId() : "-" %></span><br>
                            <small>
                                dépôt <%= a.getOrdreDepot() != null ? a.getOrdreDepot() : "-" %>
                                /
                                <%= a.getNbReservationsTrajet() != null ? a.getNbReservationsTrajet() : "-" %>
                            </small>
                        </td>
                        <td><span class="badge badge--accent"><%= a.getNbPassager() %></span></td>
                        <td>
                            <%= a.getAeroportCode() != null ? a.getAeroportCode() : "-" %>
                            &rarr;
                            <%= a.getHotelNom() != null ? a.getHotelNom() : ("Hotel #" + a.getIdHotel()) %>
                            (<%= a.getHotelCode() != null ? a.getHotelCode() : "-" %>)
                            <% if (a.getNbReservationsTrajet() != null && a.getNbReservationsTrajet() > 1) { %>
                                <br><small>Mutualisé: <%= a.getNbReservationsTrajet() %> réservations / <%= a.getPassagersTrajet() %> passagers</small>
                            <% } %>
                        </td>
                        <td>
                            <%= String.format(Locale.US, "%.1f", a.getDistanceKm()) %> km
                            (<%= a.getDureeTrajetMinutes() %> min)
                        </td>
                        <td><%= a.getHeureDepartAeroport() != null ? a.getHeureDepartAeroport().format(dtf) : "-" %></td>
                        <td><%= a.getHeureArriveeHotel() != null ? a.getHeureArriveeHotel().format(dtf) : "-" %></td>
                        <td>
                            <div style="font-size: 0.9em;">
                                <strong>Trajet #<%= a.getTrajetId() != null ? a.getTrajetId() : "-" %></strong><br>
                                <span class="badge badge--info">Ordre: <%= a.getOrdreDepot() != null ? a.getOrdreDepot() : "-" %>/<%= a.getNbReservationsTrajet() != null ? a.getNbReservationsTrajet() : "-" %></span><br>
                                <% if (a.getTempsAttenteEstimeMinutes() != null && a.getTempsAttenteEstimeMinutes() > 0) { %>
                                    <small>Attente: <%= a.getTempsAttenteEstimeMinutes() %> min</small>
                                <% } else { %>
                                    <small>Départ immédiat</small>
                                <% } %>
                                <% if (a.getDetailsTrajet() != null && !a.getDetailsTrajet().isEmpty()) { %>
                                    <br><small style="color: #666;"><%= a.getDetailsTrajet() %></small>
                                <% } %>
                            </div>
                        </td>
                        <td>
                            <span class="badge badge--success">#<%= a.getVehiculeId() %> <%= a.getVehiculeReference() %></span><br>
                            <small><%= a.getVehiculeNbPlace() %> places - <%= a.getVehiculeTypeCarburant() %></small>
                        </td>
                    </tr>
                <%     }
                   } else { %>
                    <tr>
                        <td colspan="9">
                            <div class="empty-message">
                                <span class="empty-icon">🚫</span>
                                <div class="empty-title">Aucune réservation assignée</div>
                                <div class="empty-text">Aucun véhicule n'a pu être affecté pour cette date</div>
                            </div>
                        </td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </div>

        <h2 style="margin: 0 0 10px 0;">Réservations non assignées</h2>
        <div class="table-wrapper">
            <table>
                <thead>
                    <tr>
                        <th>Réservation</th>
                        <th>Passagers</th>
                        <th>Hôtel</th>
                        <th>Arrivée avion</th>
                        <th>Motif</th>
                    </tr>
                </thead>
                <tbody>
                <% if (!nonAssignees.isEmpty()) {
                       for (TransferAssignment a : nonAssignees) { %>
                    <tr>
                        <td><strong>#<%= a.getReservationId() %></strong> - Client <%= a.getIdClient() %></td>
                        <td><span class="badge badge--accent"><%= a.getNbPassager() %></span></td>
                        <td>
                            <%= a.getHotelNom() != null ? a.getHotelNom() : ("Hotel #" + a.getIdHotel()) %>
                            (<%= a.getHotelCode() != null ? a.getHotelCode() : "-" %>)
                        </td>
                        <td><%= a.getHeureArriveeHotel() != null ? a.getHeureArriveeHotel().format(dtf) : "-" %></td>
                        <td><span class="badge badge--danger"><%= a.getMotif() != null ? a.getMotif() : "-" %></span></td>
                    </tr>
                <%     }
                   } else { %>
                    <tr>
                        <td colspan="5">
                            <div class="empty-message">
                                <span class="empty-icon">✅</span>
                                <div class="empty-title">Aucune réservation non assignée</div>
                                <div class="empty-text">Toutes les réservations de la date sont assignées</div>
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
