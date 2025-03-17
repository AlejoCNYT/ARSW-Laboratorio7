package edu.eci.arsw.blueprints.persistence;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.impl.Tuple;

import java.util.Set;

/**
 * Interfaz para la persistencia de Blueprints.
 * Define métodos para almacenar y recuperar blueprints.
 *
 * @author hcadavid
 */
public interface BlueprintsPersistence
{



    void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException;
    Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException;
    Set<Blueprint> getBlueprintsByAuthor(String author);
    Set<Blueprint> getAllBlueprints();
}
