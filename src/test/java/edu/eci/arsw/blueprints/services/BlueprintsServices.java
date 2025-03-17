/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.services;

import edu.eci.arsw.blueprints.filters.BlueprintFilter;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class BlueprintsServices
{
    private final BlueprintsPersistence bpp;
    private final BlueprintFilter filter;  // Estrategia de filtrado

    @Autowired
    public BlueprintsServices(@Qualifier("inMemoryBlueprintPersistence") BlueprintsPersistence bpp,
                              @Qualifier("subsamplingFilter") BlueprintFilter filter)
    {
        this.bpp = bpp;
        this.filter = filter;
    }

    public void addNewBlueprint(Blueprint bp) throws BlueprintPersistenceException
    {
        bpp.saveBlueprint(bp);
    }

    public Set<Blueprint> getAllBlueprints()
    {
        return null;
    }

    public Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException
    {
        Blueprint blueprint = bpp.getBlueprint(author, name);
        if (blueprint == null)
        {
            throw new BlueprintNotFoundException("Blueprint not found for author: " + author + ", name: " + name);
        }
        return filter.filter(blueprint);
    }

    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException
    {
        Set<Blueprint> blueprints = bpp.getBlueprintsByAuthor(author);

        if (blueprints.isEmpty())
        {
            throw new BlueprintNotFoundException("Blueprints not found for author: " + author);
        }

        return blueprints;
    }
}
