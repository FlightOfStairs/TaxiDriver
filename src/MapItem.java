import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;

public abstract class MapItem extends Point {
    private String name;

    public MapItem(String newName, int x, int y)
    {
        super(x, y);
        name = newName;
    }


    public String getName() { return name; }
        public void setName(String name) { this.name = name; }



    public void draw(Graphics2D graphic)
    {

        graphic.fillRect(x-1 , y-1, 3, 3);
        graphic.setFont(new Font("Helvetica", Font.PLAIN,  10));
        graphic.drawString(name, x+1, y-1);

    }


}
