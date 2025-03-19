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

@RestController
@RequestMapping("/blueprints")
public class BlueprintAPIController {

    private final BlueprintsServices blueprintServices;

    @Autowired
    public BlueprintAPIController(BlueprintsServices blueprintServices) {
        this.blueprintServices = blueprintServices;
    }

    @GetMapping
    public ResponseEntity<?> getAllBlueprints() {
        try {
            return ResponseEntity.ok(blueprintServices.getAllBlueprints());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener los planos");
        }
    }

    @GetMapping("/{author}")
    public ResponseEntity<?> getBlueprintsByAuthor(@PathVariable String author) {
        try {
            var blueprints = blueprintServices.getBlueprintsByAuthor(author);
            return blueprints.isEmpty()
                    ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron planos para el autor: " + author)
                    : ResponseEntity.ok(blueprints);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error específico: " + e.getMessage());
        }
    }

    @GetMapping("/{author}/{bpname}")
    public ResponseEntity<?> getBlueprintByAuthorAndName(@PathVariable String author, @PathVariable String bpname) {
        try {
            var blueprint = blueprintServices.getBlueprint(author, bpname);
            return (blueprint != null)
                    ? ResponseEntity.ok(blueprint)
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blueprint no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error específico: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> createBlueprint(@RequestBody Blueprint blueprint) {
        try {
            blueprintServices.addNewBlueprint(blueprint);
            return ResponseEntity.status(HttpStatus.CREATED).body("Plano agregado con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error al agregar el plano");
        }
    }

    @PutMapping("/{author}/{bpname}")
    public ResponseEntity<Void> updateBlueprint(@PathVariable String author,
                                                @PathVariable String bpname,
                                                @RequestBody Blueprint blueprintActualizado) {
        try {
            blueprintServices.updateBlueprint(author, bpname, blueprintActualizado);
            return ResponseEntity.ok().build();
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{author}/{bpname}")
    public ResponseEntity<Void> deleteBlueprint(@PathVariable String author, @PathVariable String bpname) {
        try {
            blueprintServices.deleteBlueprint(author, bpname);
            return ResponseEntity.noContent().build();
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Manejo global de excepciones específicas
     */
    @ExceptionHandler(BlueprintNotFoundException.class)
    public ResponseEntity<String> handleBlueprintNotFoundException(BlueprintNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + e.getMessage());
    }
}
