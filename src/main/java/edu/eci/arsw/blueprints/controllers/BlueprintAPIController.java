package edu.eci.arsw.blueprints.controllers;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
@RestController
@RequestMapping("/blueprints")
public class BlueprintAPIController
{

    private final BlueprintsServices blueprintServices;

    @Autowired
    public BlueprintAPIController(BlueprintsServices blueprintServices)
    {
        this.blueprintServices = blueprintServices;
    }

    @GetMapping
    public ResponseEntity<?> getBlueprints()
    {
        try {
            return new ResponseEntity<>(blueprintServices.getAllBlueprints(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al obtener los planos", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{author}")
    public ResponseEntity<?> getBlueprintsByAuthor(@PathVariable("author") String author)
    {
        try {
            var blueprints = blueprintServices.getBlueprintsByAuthor(author);
            if (blueprints.isEmpty()) {
                return new ResponseEntity<>("No se encontraron planos para el autor: " + author, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(blueprints, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error específico: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{author}/{bpname}")
    public ResponseEntity<?> getBlueprints(@PathVariable("author") String author, @PathVariable("bpname") String bpname)
    {
        try {
            var blueprints = blueprintServices.getBlueprint(author, bpname);
            if (blueprints == null)
            {
                return new ResponseEntity<>("No se encontraron planos para el autor: " + author, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(blueprints, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace(); // 🔥 Mostrará el error real en la consola
            return new ResponseEntity<>("Error específico: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> createBlueprints(@RequestBody Blueprint blueprint)
    {
        try {
            blueprintServices.addNewBlueprint(blueprint);
            return new ResponseEntity<>("Plano agregado con éxito", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al agregar los planos", HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{author}/{bpname}")
    public ResponseEntity<?> actualizarBlueprint(@PathVariable("author") String author,
                                                 @PathVariable("bpname") String bpname,
                                                 @RequestBody Blueprint blueprintActualizado) {
        try {
            blueprintServices.updateBlueprint(author, bpname, blueprintActualizado);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BlueprintNotFoundException e) {
            return new ResponseEntity<>("Blueprint no encontrado", HttpStatus.NOT_FOUND);
        }
    }


}

