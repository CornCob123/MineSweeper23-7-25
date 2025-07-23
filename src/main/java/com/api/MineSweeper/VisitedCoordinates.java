package com.api.MineSweeper;
import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

@Getter
@Setter
public class VisitedCoordinates
{
    int visitX;
    int visitY;
    public VisitedCoordinates(int visitX, int visitY)
    {
        this.visitX = visitX;
        this.visitY = visitY;
    }
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof VisitedCoordinates)) return false;
        VisitedCoordinates c = (VisitedCoordinates) o;
        return visitX == c.visitX && visitY == c.visitY;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(visitX, visitY);
    }
    @Override
    public String toString()
    {
        return "(" + visitX + "," + visitY + ")";
    }
}
