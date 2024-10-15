package elysia.hook;

import org.bukkit.entity.Player;
import org.serverct.ersha.api.AttributeAPI;
import org.serverct.ersha.attribute.data.AttributeData;

import java.util.List;

public class AttributePlusHook {
    public static void addAttribute(Player player, List<String> attribute, String key){
        AttributeData attributeData = AttributeAPI.getAttrData(player);
        AttributeAPI.addSourceAttribute(attributeData,"ElysiaPotion_" + key,attribute);
    }
    public static void takeAttribute(Player player,String key){
        AttributeData attributeData = AttributeAPI.getAttrData(player);
        AttributeAPI.takeSourceAttribute(attributeData,"ElysiaPotion_" + key);
    }
}
