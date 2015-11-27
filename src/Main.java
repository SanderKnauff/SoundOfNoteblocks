
import nl.imine.soundofnoteblocks.Track;
import nl.imine.soundofnoteblocks.TrackManager;


/**
 *
 * @author Sander
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TrackManager tm = new TrackManager();
        for(Track track : tm.getTracks()){
            System.out.println("=====");
            System.out.println(track.getId());
            System.out.println(track.getName());
            System.out.println(track.getArtist());
        }
    }
    
}
