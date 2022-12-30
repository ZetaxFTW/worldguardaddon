package dev.zetax;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import net.raidstone.wgevents.WorldGuardEvents;
import org.bukkit.entity.Player;


public class IsInsideRegion extends Element {
    public IsInsideRegion(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getRequiredPlugin() {
        return "WorldGuard";
    }

    public String getName() {
        return "Inside worldguard region";
    }

    public String getInternalName() {
        return "inside-wg-region";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.BAMBOO;
    }

    public String[] getDescription() {
        return new String[] { "Checks if a player is inside a worldguard region" };
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[] { new Argument("player", "Player", DataType.PLAYER, elementInfo), new Argument("region", "Region", DataType.STRING, elementInfo) };
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[] { new OutcomingVariable("result", "Result", DataType.BOOLEAN, elementInfo) };
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[] { (Child)new DefaultChild(elementInfo, "next") };
    }

    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        final Player player = (Player)getArguments(elementInfo)[0].getValue(scriptInstance);
        final String region = (String)getArguments(elementInfo)[1].getValue(scriptInstance);

        final Boolean result = WorldGuardEvents.isPlayerInAnyRegion(player.getUniqueId(), region);
        System.out.println("Result: " + result);
        getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
            public Object request() {
                return result;
            }
        });
        getConnectors(elementInfo)[0].run(scriptInstance);
    }
}
