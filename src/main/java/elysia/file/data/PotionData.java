package elysia.file.data;

import java.util.HashMap;
import java.util.List;

public class PotionData {
    private final String id;
    private final String name;
    private final Integer cooldown;
    private final Integer useTime;
    private final String group;
    private final HashMap<String,String> effects;
    private final HashMap<String,String> potions;
    private final List<String> attributes;
    private final Integer attributeContinue;
    private final Boolean consume;

    public PotionData(String id, String name, Integer cooldown, Integer useTime, String group, HashMap<String, String> effects, HashMap<String, String> potions, List<String> attributes, Integer attributeContinue, Boolean consume) {
        this.id = id;
        this.name = name;
        this.cooldown = cooldown;
        this.useTime = useTime;
        this.group = group;
        this.effects = effects;
        this.potions = potions;
        this.attributes = attributes;
        this.attributeContinue = attributeContinue;
        this.consume = consume;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getCooldown() {
        return cooldown;
    }

    public Integer getUseTime() {
        return useTime;
    }

    public String getGroup() {
        return group;
    }

    public HashMap<String, String> getEffects() {
        return effects;
    }

    public HashMap<String, String> getPotions() {
        return potions;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public Integer getAttributeContinue() {
        return attributeContinue;
    }

    public Boolean getConsume() {
        return consume;
    }
}
