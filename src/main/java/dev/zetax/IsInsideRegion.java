package dev.zetax;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import net.raidstone.wgevents.WorldGuardEvents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;


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

        try {
            File f = new File("plugins/WorldGuardEvents-1.18.1.jar");
            if(f.exists() && !f.isDirectory()) {
                getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                    public Object request() {
                        return WorldGuardEvents.isPlayerInAnyRegion(player.getUniqueId(), region);
                    }
                });
            } else {
                Bukkit.getServer().getOnlinePlayers().stream().filter(player1 -> player1.isOp()).forEach(player1 -> player1.playSound(player1.getLocation(), "block.note_block.bass", 1, 1));
                Bukkit.getServer().getOnlinePlayers().stream().filter(player1 -> player1.isOp()).forEach(player1 -> player1.sendTitle(ChatColor.RED + "WorldGuardEvents is", ChatColor.RED + "not installed", 10, 70, 20));
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error: " + e.getMessage());
        }
        getConnectors(elementInfo)[0].run(scriptInstance);
    }
}
