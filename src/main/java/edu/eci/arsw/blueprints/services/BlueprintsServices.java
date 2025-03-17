/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.services;

import java.util.HashSet;

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

    public synchronized void updateBlueprint(String author, String bpname, Blueprint nuevoBlueprint) throws BlueprintNotFoundException {
        Blueprint blueprintExistente = bpp.getBlueprint(author, bpname);
        if (blueprintExistente == null) {
            throw new BlueprintNotFoundException("El plano no existe");
        }
        blueprintExistente.setPoints(nuevoBlueprint.getPoints());
    }



    public synchronized void addNewBlueprint(Blueprint bp) throws BlueprintPersistenceException
    {
        bpp.saveBlueprint(bp);
    }

    public Set<Blueprint> getAllBlueprints()
    {
        Set<Blueprint> blueprints = new HashSet<>(bpp.getAllBlueprints());
        System.out.println("Blueprints obtenidos: " + blueprints);
        return blueprints;
    }


    public synchronized Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException
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
