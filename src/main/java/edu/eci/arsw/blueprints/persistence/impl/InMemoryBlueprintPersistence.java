/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author hcadavid
 */
@Repository
public class InMemoryBlueprintPersistence implements BlueprintsPersistence
{

    public static final Map<Tuple<String,String>, Blueprint> blueprints = new ConcurrentHashMap<>();

    public InMemoryBlueprintPersistence()
    {
        //load stub data
        Point[] pts1=new Point[]{new Point(140, 160),new Point(155, 125)};
        Point[] pts2=new Point[]{new Point(150, 120),new Point(135, 95)};
        Point[] pts3=new Point[]{new Point(180, 100),new Point(175, 140)};
        Blueprint bp1=new Blueprint("LeCorbusier", "Villa Savoye",pts1);
        Blueprint bp2=new Blueprint("LeCorbusier", "Unidad de Habitación de Marsella",pts2);
        Blueprint bp3=new Blueprint("Antoni Gaudí", "la Sagrada Familia",pts3);
        blueprints.put(new Tuple<>(bp1.getAuthor(),bp1.getName()), bp1);
        blueprints.put(new Tuple<>(bp2.getAuthor(),bp2.getName()), bp2);
        blueprints.put(new Tuple<>(bp3.getAuthor(),bp3.getName()), bp3);
        /*try {
            saveBlueprint(new Blueprint("Frank Lloyd Wright", "Fallingwater", pts1));
            System.out.println(getBlueprint("Frank Lloyd Wright", "Fallingwater"));
        } catch (BlueprintPersistenceException | BlueprintNotFoundException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException
    {
        if (blueprints.putIfAbsent(new Tuple<>(bp.getAuthor(), bp.getName()), bp) != null) {
            throw new BlueprintPersistenceException("The given blueprint already exists: " + bp);
        }

    }

    @Override
    public Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException {
        Tuple<String, String> key = new Tuple<>(author, name);

        if (!blueprints.containsKey(key)) {
            throw new BlueprintNotFoundException("Blueprint not found for author: " + author + ", name: " + name);
        }

        return blueprints.get(key);
    }




    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) {
        Set<Blueprint> result = new HashSet<>();

        for (Map.Entry<Tuple<String, String>, Blueprint> entry : blueprints.entrySet()) {
            if (entry.getKey().getElem1().equals(author)) {
                result.add(entry.getValue());
            }
        }

        return result;
    }

    @Override
    public Set<Blueprint> getAllBlueprints()
    {
        return new HashSet<>(blueprints.values());
    }

}
