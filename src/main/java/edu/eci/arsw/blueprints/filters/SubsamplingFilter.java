package edu.eci.arsw.blueprints.filters;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Component
public class SubsamplingFilter implements BlueprintFilter
{

    @Override
    public Blueprint filter(Blueprint blueprint)
    {
        List<Point> points = blueprint.getPoints();

        List<Point> filteredPoints = new ArrayList<>();
        filteredPoints.add(points.get(0)); // Siempre agregar el primer punto

        for (int i = 0; i < points.size(); i += 2)
        {
            filteredPoints.add(points.get(i));
        }

        // Convertir la lista a un array de Point[]
        Point[] filteredArray = filteredPoints.toArray(new Point[0]);

        return new Blueprint(blueprint.getAuthor(), blueprint.getName(), filteredArray);
    }
}
