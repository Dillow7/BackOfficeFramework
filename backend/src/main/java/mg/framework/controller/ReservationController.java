package mg.framework.controller;

import mg.framework.annotations.Controlleur;
import mg.framework.annotations.GetMapping;
import mg.framework.annotations.Json;
import mg.framework.annotations.PostMapping;
import mg.framework.annotations.RequestParam;
import mg.framework.dao.ClientDao;
import mg.framework.dao.HotelDao;
import mg.framework.dao.ReservationDao;
import mg.framework.entity.Reservation;
import mg.framework.model.JsonResponse;
import mg.framework.model.ModelView;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Controlleur("")
public class ReservationController {
    private final ClientDao clientDao = new ClientDao();
    private final HotelDao hotelDao = new HotelDao();
    private final ReservationDao dao = new ReservationDao();

    @GetMapping("/reservations/new")
    public ModelView newReservationForm() throws SQLException {
        ModelView mv = new ModelView("WEB-INF/views/reservation-form.jsp");
        mv.addAttribute("clients", clientDao.findAll());
        mv.addAttribute("hotels", hotelDao.findAll());
        return mv;
    }

    @GetMapping("/reservations/all")
    @Json
    public JsonResponse list() throws SQLException {
        return new JsonResponse("success", 200, dao.findAll(), "Opération réussie");
    }

    @GetMapping("/reservations/find")
    @Json
    public JsonResponse find(@RequestParam("id") int id) throws SQLException {
        return dao.findById(id)
                .map(reservation -> new JsonResponse("success", 200, reservation, "Opération réussie"))
                .orElseGet(() -> new JsonResponse("error", 404, null, "Reservation introuvable"));
    }

    @PostMapping("/reservations/create")
    @Json
    public JsonResponse create(@RequestParam("idClient") String idClient,
                                            @RequestParam("nbPassager") int nbPassager,
                                            @RequestParam("dateHeureArrive") String dateHeureArrive,
                                            @RequestParam("idHotel") int idHotel) throws SQLException {
        Reservation reservation = new Reservation(
                null,
                idClient,
                nbPassager,
                LocalDateTime.parse(dateHeureArrive),
                idHotel
        );
        return new JsonResponse("success", 200, dao.save(reservation), "Opération réussie");
    }

    @PostMapping("/reservations/update")
    @Json
    public JsonResponse update(@RequestParam("id") int id,
                                        @RequestParam("idClient") String idClient,
                                        @RequestParam("nbPassager") int nbPassager,
                                        @RequestParam("dateHeureArrive") String dateHeureArrive,
                                        @RequestParam("idHotel") int idHotel) throws SQLException {
        Reservation reservation = new Reservation(
                id,
                idClient,
                nbPassager,
                LocalDateTime.parse(dateHeureArrive),
                idHotel
        );
        return new JsonResponse("success", 200, dao.update(reservation), "Opération réussie");
    }

    @PostMapping("/reservations/delete")
    @Json
    public JsonResponse delete(@RequestParam("id") int id) throws SQLException {
        return new JsonResponse("success", 200, dao.delete(id), "Opération réussie");
    }
}
