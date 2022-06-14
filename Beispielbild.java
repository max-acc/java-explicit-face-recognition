import imp.*; 
import java.io.File;
/**
 * Klasse für Beispielbilder
 *
 * @author  Thomas Schaller
 * @version v1.0 (28.11.2019)
 */
public class Beispielbild extends Picture
{

    /**
     * Konstruktor: öffnet das Bild katze.jpg
     */
    public Beispielbild()
    {
        super("rosen_normal.jpg");
    }

    public void load(String dateiname) {
        super.load("images/"+dateiname);
    }

    public void save(String dateiname) {
        super.save("images/"+dateiname);
    }

}
