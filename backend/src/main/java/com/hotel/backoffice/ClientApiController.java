package com.hotel.backoffice;

import com.hotel.backoffice.model.Client;
import mg.framework.annotations.Controlleur;
import mg.framework.annotations.GetMapping;
import mg.framework.annotations.Json;
import mg.framework.annotations.PostMapping;
import mg.framework.annotations.RequestParam;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controlleur
public class ClientApiController {
    private final ClientDao clientDao = new ClientDao();

    @GetMapping("/api/clients")
    @Json
    public List<Map<String, Object>> list() {
        try {
            List<Client> clients = clientDao.findAll();
            List<Map<String, Object>> out = new ArrayList<>();
            for (Client c : clients) {
                out.add(toMap(c));
            }
            return out;
        } catch (SQLException e) {
            return List.of();
        }
    }

    @GetMapping("/api/clients/by-id")
    @Json
    public Map<String, Object> getById(@RequestParam("id") String id) {
        try {
            Client c = clientDao.findById(id);
            if (c == null) {
                Map<String, Object> err = new HashMap<>();
                err.put("error", "Client non trouvé");
                return err;
            }
            return toMap(c);
        } catch (SQLException e) {
            Map<String, Object> err = new HashMap<>();
            err.put("error", "Erreur serveur");
            return err;
        }
    }

    @PostMapping("/api/clients")
    @Json
    public Map<String, Object> create(
            @RequestParam("id") String id,
            @RequestParam("nom") String nom,
            @RequestParam("prenom") String prenom,
            @RequestParam("telephone") String telephone,
            @RequestParam("email") String email
    ) {
        try {
            Client c = new Client();
            c.setId(id);
            c.setNom(nom);
            c.setPrenom(prenom);
            c.setTelephone(telephone);
            c.setEmail(email);
            clientDao.insert(c);
            return toMap(c);
        } catch (SQLException e) {
            Map<String, Object> err = new HashMap<>();
            err.put("error", "Erreur serveur");
            return err;
        }
    }

    private Map<String, Object> toMap(Client c) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", c.getId());
        m.put("nom", c.getNom());
        m.put("prenom", c.getPrenom());
        m.put("telephone", c.getTelephone());
        m.put("email", c.getEmail());
        return m;
    }
}
