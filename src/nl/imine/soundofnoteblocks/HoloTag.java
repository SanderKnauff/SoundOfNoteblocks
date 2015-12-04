package nl.imine.soundofnoteblocks;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.TagName;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

/**
 *
 * @author Sander
 */
public class HoloTag {

    private Location location;

    private List<ArmorStand> tags = new ArrayList<>();

    public HoloTag(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void addTag(String tagName) {
        ArmorStand tagEntity = (ArmorStand) location.getWorld().spawnEntity(location.add(0.5, 0, 0.5), EntityType.ARMOR_STAND);
        tagEntity.setVisible(false);
        tagEntity.setGravity(false);
        tagEntity.setBasePlate(false);
        tagEntity.setRemoveWhenFarAway(true);
        tagEntity.setCustomName(tagName);
        tagEntity.setCustomNameVisible(true);
        tags.add(tagEntity);
    }

    public void spawnTags() {

    }

    public class Tag {

        private String tagName;

        public Tag(String tagName) {
            this.tagName = tagName; 
        }
        
        public void setName(String tagName){
            this.tagName = tagName;
        }

        public String getName() {
            return tagName;
        }
    }
}
