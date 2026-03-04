package mg.framework.controller;

import mg.framework.annotations.Controlleur;
import mg.framework.annotations.GetMapping;
import mg.framework.annotations.Json;
import mg.framework.annotations.PostMapping;
import mg.framework.annotations.RequestParam;
import mg.framework.dao.ClientDao;
import mg.framework.entity.Client;
import mg.framework.model.JsonResponse;

import java.sql.SQLException;
import java.util.List;

@Controlleur("")
public class ClientController {
    private final ClientDao dao = new ClientDao();

    @GetMapping("/clients/all")
    @Json
    public JsonResponse list() throws SQLException {
        return new JsonResponse("success", 200, dao.findAll(), "Opération réussie");
    }

    @GetMapping("/clients/find")
    @Json
    public JsonResponse find(@RequestParam("id") String id) throws SQLException {
        return dao.findById(id)
                .map(client -> new JsonResponse("success", 200, client, "Opération réussie"))
                .orElseGet(() -> new JsonResponse("error", 404, null, "Client introuvable"));
    }

    @PostMapping("/clients/create")
    @Json
    public JsonResponse create(@RequestParam("id") String id,
                                       @RequestParam("nom") String nom,
                                       @RequestParam("prenom") String prenom,
                                       @RequestParam("telephone") String telephone,
                                       @RequestParam("email") String email) throws SQLException {
        Client client = new Client(id, nom, prenom, telephone, email);
        return new JsonResponse("success", 200, dao.save(client), "Opération réussie");
    }

    @PostMapping("/clients/update")
    @Json
    public JsonResponse update(@RequestParam("id") String id,
                                        @RequestParam("nom") String nom,
                                        @RequestParam("prenom") String prenom,
                                        @RequestParam("telephone") String telephone,
                                        @RequestParam("email") String email) throws SQLException {
        Client client = new Client(id, nom, prenom, telephone, email);
        return new JsonResponse("success", 200, dao.update(client), "Opération réussie");
    }

    @PostMapping("/clients/delete")
    @Json
    public JsonResponse delete(@RequestParam("id") String id) throws SQLException {
        return new JsonResponse("success", 200, dao.delete(id), "Opération réussie");
    }
}
