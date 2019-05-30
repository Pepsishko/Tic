package javafxapplication1.data;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
/**
 * Класс для вывода информации о цепи
 */
public class GomokuChain {
    public Set<Point> points;
    /**
     * Метод возвращения размера цепи {@link GomokuChain#size()}
     * @return размер цепи
     */
    public int size() {
        return points.size();
    }
    /**
     * Метод проверки на содержание объекта в цепи
     * @param o - объект для сравнения
     * @return содержиться или не содержиться
     */
    public boolean contains(Object o) {
        return points.contains(o);
    }
    /**
     * Метод обновления компонентов в цепи
     * @param points - множество точек
     */
    public GomokuChain(Set<Point> points) {
        this.points = points;
    }
    /**
     * Создание новой цепи
     */
    public GomokuChain() {
        points = new HashSet();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GomokuChain other = (GomokuChain) obj;        
        for (Point my:points) {
        }
        return true;
    }
    
    
    
}
