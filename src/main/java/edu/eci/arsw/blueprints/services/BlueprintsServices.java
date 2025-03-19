package edu.eci.arsw.blueprints.services;

import java.util.HashSet;
import java.util.Set;

import edu.eci.arsw.blueprints.filters.BlueprintFilter;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class BlueprintsServices {
    private final BlueprintsPersistence bpp;
    private final BlueprintFilter filter;

    @Autowired
    public BlueprintsServices(@Qualifier("inMemoryBlueprintPersistence") BlueprintsPersistence bpp,
                              @Qualifier("subsamplingFilter") BlueprintFilter filter) {
        this.bpp = bpp;
        this.filter = filter;
    }

    public synchronized void updateBlueprint(String author, String bpname, Blueprint nuevoBlueprint) throws BlueprintNotFoundException {
        Blueprint blueprintExistente = bpp.getBlueprint(author, bpname);

        if (blueprintExistente == null) {
            throw new BlueprintNotFoundException("Blueprint not found for author: " + author + ", name: " + bpname);
        }

        blueprintExistente.setPoints(nuevoBlueprint.getPoints());

        // ✅ Guardar cambios en la persistencia
        bpp.updateBlueprint(author, bpname, blueprintExistente);
    }

    public synchronized void addNewBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        bpp.saveBlueprint(bp);
    }

    public Set<Blueprint> getAllBlueprints() {
        return new HashSet<>(bpp.getAllBlueprints());
    }

    public synchronized Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException {
        Blueprint blueprint = bpp.getBlueprint(author, name);
        if (blueprint == null) {
            throw new BlueprintNotFoundException("Blueprint not found for author: " + author + ", name: " + name);
        }
        return filter != null ? filter.filter(blueprint) : blueprint;
    }

    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        Set<Blueprint> blueprints = bpp.getBlueprintsByAuthor(author);
        return blueprints != null ? blueprints : new HashSet<>();
    }

    public synchronized void deleteBlueprint(String author, String bpname) throws BlueprintNotFoundException {
        Blueprint blueprint = bpp.getBlueprint(author, bpname);
        if (blueprint == null) {
            throw new BlueprintNotFoundException("El plano con autor: " + author + " y nombre: " + bpname + " no existe.");
        }
        bpp.deleteBlueprint(author, bpname);
    }

    public synchronized void saveBlueprint(String author, Blueprint blueprint) throws BlueprintPersistenceException {
        blueprint.setAuthor(author);
        bpp.saveBlueprint(blueprint);
    }
}
