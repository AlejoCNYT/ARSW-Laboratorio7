package edu.eci.arsw.blueprints.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Blueprint {
    private String author;
    private List<Point> points;
    private String name;

    public Blueprint(String author, String name, Point[] pnts) {
        this.author = author;
        this.name = name;
        this.points = new ArrayList<>(Arrays.asList(pnts)); // ✅ Mutable list
    }

    public Blueprint(String author, String name) {
        this.author = author;
        this.name = name;
        this.points = new ArrayList<>();
    }

    public Blueprint() {
        this.points = new ArrayList<>();  // ✅ Evita NullPointerException
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void addPoint(Point p) {
        this.points.add(p);
    }

    @Override
    public String toString() {
        return "Blueprint{" + "author=" + author + ", name=" + name + ", points=" + points + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, name, points);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Blueprint other = (Blueprint) obj;
        return Objects.equals(this.author, other.author) &&
                Objects.equals(this.name, other.name) &&
                Objects.equals(this.points, other.points); // ✅ Comparación correcta
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void removePoint(Point p) {
        this.points.remove(p);
    }
}
