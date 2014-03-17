import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Loader {
    private BufferedImage background;
    private BufferedImage mask;

    private String title;

    private int persons;
    private int taxis;



    public void loadFile(String str) { loadFile(new File(str)); }

    public void loadFile(File file) {

        boolean debugOn = false;

        // Some of this is taken from http://www.javaworld.com/javaworld/javatips/jw-javatip49.html
        Hashtable<String, byte[]> zipContents = new Hashtable<String, byte[]>();
        try {
            FileInputStream fis=new FileInputStream(file);
            BufferedInputStream bis=new BufferedInputStream(fis);
            ZipInputStream zis=new ZipInputStream(bis);
            ZipEntry ze=zis.getNextEntry();
            while (ze!=null) {

                if (debugOn) {
                    System.out.println(
                            "ze.getName()="+ze.getName()+","+"getSize()="+ze.getSize()
                    );
                }
                int size=(int)ze.getSize();
                // -1 means unknown size.
//                if (size==-1) {
//                size=((Integer)htSizes.get(ze.getName())).intValue();
//                }
                byte[] b=new byte[(int)size];
                int rb=0;
                int chunk=0;
                while (((int)size - rb) > 0) {
                    chunk=zis.read(b,rb,(int)size - rb);
                    if (chunk==-1) {
                        break;
                    }
                    rb+=chunk;
                }
                // add to internal resource hashtable
                zipContents.put(ze.getName(), b);


                ze=zis.getNextEntry();
            }

            zis.close();

            Image maskImage = Toolkit.getDefaultToolkit().createImage(zipContents.get("mask.png"));
            Image backgroundImage = Toolkit.getDefaultToolkit().createImage(zipContents.get("city.png"));

            mask = toBufferedImage(maskImage);
            background = toBufferedImage(backgroundImage);

            String settings = new String(zipContents.get("settings"));

            Scanner sn = new Scanner(settings);
            Hashtable<String, String> settingsTable = new Hashtable<String, String>();

            while (sn.hasNext()) {
                String tempString = sn.next();
                if((tempString == null) || (tempString.length() < 3)) continue;
                String restOfLine = sn.nextLine();
                settingsTable.put(tempString, restOfLine);
            }


            title = settingsTable.get("title");

            persons = Integer.parseInt(settingsTable.get("persons").trim());
            taxis = Integer.parseInt(settingsTable.get("taxis").trim());

        } catch (Exception e) { e.printStackTrace(); }

    }

    public BufferedImage getBackground() { return background; }
    public BufferedImage getMask() { return mask; }

    public String getTitle() { return title; }
    public int getPersons() { return persons; }
    public int getTaxis() { return taxis; }




    private BufferedImage toBufferedImage(Image image) {

        image = new ImageIcon(image).getImage();
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);

        // Copy image to buffered image
        Graphics g = bufferedImage.createGraphics();

        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bufferedImage;
    }
    public int getHeight() { return mask.getHeight(); }
    public int getWidth() { return mask.getWidth(); }

}
