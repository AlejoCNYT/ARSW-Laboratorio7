package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementación en memoria de la persistencia de Blueprints.
 * Simula una base de datos almacenando Blueprints en un mapa concurrente.
 */
@Repository
public class InMemoryBlueprintPersistence implements BlueprintsPersistence {

    private static final Map<Tuple<String, String>, Blueprint> blueprints = new ConcurrentHashMap<>();

    public InMemoryBlueprintPersistence() {
        // Datos de prueba
        Point[] pts1 = new Point[]{new Point(140, 160), new Point(155, 125)};
        Point[] pts2 = new Point[]{new Point(150, 120), new Point(135, 95)};
        Point[] pts3 = new Point[]{new Point(180, 100), new Point(175, 140)};

        Blueprint bp1 = new Blueprint("LeCorbusier", "Villa Savoye", pts1);
        Blueprint bp2 = new Blueprint("LeCorbusier", "Unidad de Habitación de Marsella", pts2);
        Blueprint bp3 = new Blueprint("Antoni Gaudí", "La Sagrada Familia", pts3);

        blueprints.put(new Tuple<>(bp1.getAuthor(), bp1.getName()), bp1);
        blueprints.put(new Tuple<>(bp2.getAuthor(), bp2.getName()), bp2);
        blueprints.put(new Tuple<>(bp3.getAuthor(), bp3.getName()), bp3);
    }

    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        if (blueprints.putIfAbsent(new Tuple<>(bp.getAuthor(), bp.getName()), bp) != null) {
            throw new BlueprintPersistenceException("El blueprint ya existe: " + bp);
        }
    }

    @Override
    public Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException {
        Tuple<String, String> key = new Tuple<>(author, name);
        Blueprint bp = blueprints.get(key);

        if (bp == null) {
            throw new BlueprintNotFoundException("Blueprint no encontrado: Autor=" + author + ", Nombre=" + name);
        }

        return bp;
    }

    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        Set<Blueprint> result = new HashSet<>();

        for (Map.Entry<Tuple<String, String>, Blueprint> entry : blueprints.entrySet()) {
            if (entry.getKey().getElem1().equals(author)) {
                result.add(entry.getValue());
            }
        }

        if (result.isEmpty()) {
            throw new BlueprintNotFoundException("No se encontraron blueprints para el autor: " + author);
        }

        return result;
    }

    @Override
    public Set<Blueprint> getAllBlueprints() {
        return new HashSet<>(blueprints.values());
    }

    @Override
    public void deleteBlueprint(String author, String bpname) throws BlueprintNotFoundException {
        Tuple<String, String> key = new Tuple<>(author, bpname);

        if (blueprints.remove(key) == null) {
            throw new BlueprintNotFoundException("Blueprint no encontrado: Autor=" + author + ", Nombre=" + bpname);
        }
    }

    @Override
    public void updateBlueprint(String author, String bpname, Blueprint blueprintExistente) throws BlueprintNotFoundException {
        Tuple<String, String> key = new Tuple<>(author, bpname);

        if (!blueprints.containsKey(key)) {
            throw new BlueprintNotFoundException("No se puede actualizar. Blueprint no encontrado: Autor=" + author + ", Nombre=" + bpname);
        }

        blueprints.put(key, blueprintExistente);
    }
}
