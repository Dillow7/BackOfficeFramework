package mg.framework.controller;

import mg.framework.annotations.Controlleur;
import mg.framework.annotations.GetMapping;
import mg.framework.dao.ClientDao;
import mg.framework.dao.HotelDao;
import mg.framework.model.ModelView;

import java.sql.SQLException;

@Controlleur("")
public class HomeController {
    private final ClientDao clientDao = new ClientDao();
    private final HotelDao hotelDao = new HotelDao();

    @GetMapping("/")
    public Object home() {
        try {
            ModelView mv = new ModelView("WEB-INF/views/reservation-form.jsp");
            mv.addAttribute("clients", clientDao.findAll());
            mv.addAttribute("hotels", hotelDao.findAll());
            return mv;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erreur DB: " + e;
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur: " + e;
        }
    }
}
