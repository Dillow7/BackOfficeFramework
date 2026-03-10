package com.hotel.backoffice;

import mg.framework.annotations.Controlleur;
import mg.framework.annotations.GetMapping;
import mg.framework.annotations.PostMapping;
import mg.framework.annotations.RequestParam;
import mg.framework.model.ModelView;

import java.sql.SQLException;
import java.util.List;

import com.hotel.backoffice.model.Vehicule;


// Contrôleur pour gérer les opérations CRUD sur les véhicules
@Controlleur
public class VehiculeController {
    private final VehiculeDao vehiculeDao = new VehiculeDao();

    @GetMapping("/vehicules")
    public ModelView list() {
        ModelView mv = new ModelView("/WEB-INF/jsp/vehicules.jsp");
        try {
            List<Vehicule> vehicules = vehiculeDao.findAll();
            mv.addAttribute("vehicules", vehicules);
        } catch (SQLException e) {
            mv.addAttribute("error", "Erreur lors de la récupération des véhicules");
        }
        return mv;
    }

    @GetMapping("/vehicules/new")
    public ModelView form() {
        return new ModelView("/WEB-INF/jsp/vehicule-form.jsp");
    }

    @GetMapping("/vehicules/edit")
    public ModelView editForm(@RequestParam("id") String id) {
        ModelView mv = new ModelView("/WEB-INF/jsp/vehicule-form.jsp");
        try {
            int vehiculeId = Integer.parseInt(id);
            Vehicule vehicule = vehiculeDao.findById(vehiculeId);
            if (vehicule != null) {
                mv.addAttribute("vehicule", vehicule);
            } else {
                mv.addAttribute("error", "Véhicule non trouvé");
            }
        } catch (NumberFormatException | SQLException e) {
            mv.addAttribute("error", "Erreur lors de la récupération du véhicule");
        }
        return mv;
    }

    @PostMapping("/vehicules/save")
    public ModelView save(
            @RequestParam("id") String id,
            @RequestParam("reference") String reference,
            @RequestParam("nb_place") String nbPlace,
            @RequestParam("type_carburant") String typeCarburant
    ) {
        ModelView mv = new ModelView("/WEB-INF/jsp/vehicules.jsp");
        try {
            int nb = Integer.parseInt(nbPlace);
            Vehicule vehicule = new Vehicule();
            vehicule.setReference(reference);
            vehicule.setNbPlace(nb);
            vehicule.setTypeCarburant(typeCarburant);

            if (id != null && !id.isEmpty()) {
                // Update
                vehiculeDao.update(Integer.parseInt(id), reference, nb, typeCarburant);
                mv.addAttribute("success", "Véhicule modifié");
            } else {
                // Insert
                vehiculeDao.insert(reference, nb, typeCarburant);
                mv.addAttribute("success", "Véhicule ajouté");
            }
            List<Vehicule> vehicules = vehiculeDao.findAll();
            mv.addAttribute("vehicules", vehicules);
        } catch (NumberFormatException | SQLException e) {
            mv.addAttribute("error", "Erreur lors de l'enregistrement");
            try {
                List<Vehicule> vehicules = vehiculeDao.findAll();
                mv.addAttribute("vehicules", vehicules);
            } catch (SQLException ex) {
                // ignore
            }
        }
        return mv;
    }

    @PostMapping("/vehicules/delete")
    public ModelView delete(@RequestParam("id") String id) {
        ModelView mv = new ModelView("/WEB-INF/jsp/vehicules.jsp");
        try {
            int vehiculeId = Integer.parseInt(id);
            vehiculeDao.delete(vehiculeId);
            mv.addAttribute("success", "Véhicule supprimé");
            List<Vehicule> vehicules = vehiculeDao.findAll();
            mv.addAttribute("vehicules", vehicules);
        } catch (NumberFormatException | SQLException e) {
            mv.addAttribute("error", "Erreur lors de la suppression");
            try {
                List<Vehicule> vehicules = vehiculeDao.findAll();
                mv.addAttribute("vehicules", vehicules);
            } catch (SQLException ex) {
                // ignore
            }
        }
        return mv;
    }
}