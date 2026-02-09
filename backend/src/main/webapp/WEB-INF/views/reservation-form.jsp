<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.framework.entity.Client" %>
<%@ page import="mg.framework.entity.Hotel" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Nouvelle réservation</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f6f7fb; margin: 0; padding: 0; }
        .container { max-width: 720px; margin: 40px auto; background: #fff; padding: 24px; border-radius: 12px; box-shadow: 0 6px 18px rgba(0,0,0,0.08); }
        h1 { margin-top: 0; }
        .field { margin-bottom: 16px; }
        label { display: block; margin-bottom: 6px; font-weight: 600; }
        input, select { width: 100%; padding: 10px 12px; border: 1px solid #d0d5dd; border-radius: 8px; font-size: 14px; }
        .row { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
        button { background: #2563eb; color: #fff; border: none; padding: 12px 16px; border-radius: 8px; cursor: pointer; font-weight: 600; }
        button:hover { background: #1d4ed8; }
        .hint { font-size: 12px; color: #667085; }
    </style>
</head>
<body>
<div class="container">
    <h1>Ajouter une réservation</h1>

    <form action="<%= request.getContextPath() %>/reservations/create" method="post">
        <div class="row">
            <div class="field">
                <label for="idClient">Client</label>
                <select id="idClient" name="idClient" required>
                    <option value="" disabled selected>Choisir un client</option>
                    <%
                        Object clientsObj = request.getAttribute("clients");
                        if (clientsObj instanceof List) {
                            for (Object cObj : (List<?>) clientsObj) {
                                Client c = (Client) cObj;
                    %>
                    <option value="<%= c.getId() %>"><%= c.getId() %> - <%= c.getNom() %> <%= c.getPrenom() %></option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>
            <div class="field">
                <label for="idHotel">Hôtel</label>
                <select id="idHotel" name="idHotel" required>
                    <option value="" disabled selected>Choisir un hôtel</option>
                    <%
                        Object hotelsObj = request.getAttribute("hotels");
                        if (hotelsObj instanceof List) {
                            for (Object hObj : (List<?>) hotelsObj) {
                                Hotel h = (Hotel) hObj;
                    %>
                    <option value="<%= h.getId() %>"><%= h.getId() %> - <%= h.getNom() %></option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>
        </div>

        <div class="row">
            <div class="field">
                <label for="nbPassager">Nombre de passagers</label>
                <input type="number" id="nbPassager" name="nbPassager" min="1" required />
            </div>
            <div class="field">
                <label for="dateHeureArrive">Date et heure d'arrivée</label>
                <input type="datetime-local" id="dateHeureArrive" name="dateHeureArrive" required />
                <div class="hint">Format attendu : yyyy-MM-ddTHH:mm</div>
            </div>
        </div>

        <button type="submit">Enregistrer</button>
    </form>
</div>
</body>
</html>
