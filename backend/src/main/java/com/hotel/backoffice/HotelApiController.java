package com.hotel.backoffice;

import com.hotel.backoffice.model.Hotel;
import mg.framework.annotations.Controlleur;
import mg.framework.annotations.GetMapping;
import mg.framework.annotations.Json;
import mg.framework.annotations.RequestParam;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controlleur
public class HotelApiController {
    private final HotelDao hotelDao = new HotelDao();

    @GetMapping("/api/hotels")
    @Json
    public List<Map<String, Object>> list() {
        try {
            List<Hotel> hotels = hotelDao.findAll();
            List<Map<String, Object>> out = new ArrayList<>();
            for (Hotel h : hotels) {
                Map<String, Object> m = new HashMap<>();
                m.put("id", h.getIdHotel());
                m.put("nom", h.getNom());
                out.add(m);
            }
            return out;
        } catch (SQLException e) {
            return List.of();
        }
    }

    @GetMapping("/api/hotels/by-id")
    @Json
    public Map<String, Object> getById(@RequestParam("id") String id) {
        try {
            int hotelId = Integer.parseInt(id);
            Hotel h = hotelDao.findById(hotelId);
            if (h == null) {
                Map<String, Object> err = new HashMap<>();
                err.put("error", "Hôtel non trouvé");
                return err;
            }
            Map<String, Object> out = new HashMap<>();
            out.put("id", h.getIdHotel());
            out.put("nom", h.getNom());
            return out;
        } catch (Exception e) {
            Map<String, Object> err = new HashMap<>();
            err.put("error", "Requête invalide");
            return err;
        }
    }
}
