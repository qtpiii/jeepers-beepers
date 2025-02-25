package net.qtpi.jeepersbeepers.world.biome;

import net.minecraft.resources.ResourceLocation;
import net.qtpi.jeepersbeepers.JeepersBeepers;
import net.qtpi.jeepersbeepers.world.biome.surface.ModSurfaceRules;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;
import terrablender.api.TerraBlenderApi;

public class ModTerrablenderAPI implements TerraBlenderApi {
    @Override
    public void onTerraBlenderInitialized() {
        Regions.register(new ModOverworldRegion(new ResourceLocation(JeepersBeepers.MOD_ID, "overworld"), 4));
        SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, JeepersBeepers.MOD_ID, ModSurfaceRules.makeRules());
    }
}
