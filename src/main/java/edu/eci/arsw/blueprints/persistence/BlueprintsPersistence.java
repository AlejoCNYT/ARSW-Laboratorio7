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
public interface BlueprintsPersistence {

    /**
     * Guarda un nuevo Blueprint en la base de datos.
     *
     * @param bp Blueprint a guardar.
     * @throws BlueprintPersistenceException si el blueprint ya existe.
     */
    void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException;

    /**
     * Obtiene un Blueprint específico por autor y nombre.
     *
     * @param author Autor del Blueprint.
     * @param name Nombre del Blueprint.
     * @return El Blueprint encontrado.
     * @throws BlueprintNotFoundException si el Blueprint no existe.
     */
    Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException;

    /**
     * Obtiene todos los Blueprints de un autor.
     *
     * @param author Autor de los Blueprints.
     * @return Un conjunto de Blueprints asociados al autor.
     * @throws BlueprintNotFoundException si no hay Blueprints para el autor dado.
     */
    Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException;

    /**
     * Obtiene todos los Blueprints almacenados.
     *
     * @return Un conjunto de todos los Blueprints.
     */
    Set<Blueprint> getAllBlueprints();

    /**
     * Elimina un Blueprint específico por autor y nombre.
     *
     * @param author Autor del Blueprint.
     * @param bpname Nombre del Blueprint.
     * @throws BlueprintNotFoundException si el Blueprint no existe.
     */
    void deleteBlueprint(String author, String bpname) throws BlueprintNotFoundException;

    /**
     * Actualiza un Blueprint existente.
     *
     * @param author Autor del Blueprint.
     * @param bpname Nombre del Blueprint.
     * @param blueprintExistente Blueprint con los nuevos datos.
     * @throws BlueprintNotFoundException si el Blueprint no existe.
     */
    void updateBlueprint(String author, String bpname, Blueprint blueprintExistente) throws BlueprintNotFoundException;
}
