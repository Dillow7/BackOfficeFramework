package mg.framework.controller;

import mg.framework.annotations.Controlleur;
import mg.framework.annotations.GetMapping;
import mg.framework.annotations.Json;
import mg.framework.annotations.PostMapping;
import mg.framework.annotations.RequestParam;
import mg.framework.dao.HotelDao;
import mg.framework.entity.Hotel;
import mg.framework.model.JsonResponse;

import java.sql.SQLException;
import java.util.List;

@Controlleur("")
public class HotelController {
    private final HotelDao dao = new HotelDao();

    @GetMapping("/hotels/all")
    @Json
    public JsonResponse list() throws SQLException {
        return new JsonResponse("success", 200, dao.findAll(), "Opération réussie");
    }

    @GetMapping("/hotels/find")
    @Json
    public JsonResponse find(@RequestParam("id") int id) throws SQLException {
        return dao.findById(id)
                .map(hotel -> new JsonResponse("success", 200, hotel, "Opération réussie"))
                .orElseGet(() -> new JsonResponse("error", 404, null, "Hotel introuvable"));
    }

    @PostMapping("/hotels/create")
    @Json
    public JsonResponse create(@RequestParam("nom") String nom) throws SQLException {
        Hotel hotel = new Hotel(null, nom);
        return new JsonResponse("success", 200, dao.save(hotel), "Opération réussie");
    }

    @PostMapping("/hotels/update")
    @Json
    public JsonResponse update(@RequestParam("id") int id,
                                        @RequestParam("nom") String nom) throws SQLException {
        Hotel hotel = new Hotel(id, nom);
        return new JsonResponse("success", 200, dao.update(hotel), "Opération réussie");
    }

    @PostMapping("/hotels/delete")
    @Json
    public JsonResponse delete(@RequestParam("id") int id) throws SQLException {
        return new JsonResponse("success", 200, dao.delete(id), "Opération réussie");
    }
}
