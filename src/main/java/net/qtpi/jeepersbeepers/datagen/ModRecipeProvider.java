package net.qtpi.jeepersbeepers.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.qtpi.jeepersbeepers.registry.BlockRegistry;
import net.qtpi.jeepersbeepers.registry.ItemRegistry;

import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BlockRegistry.BEEPER_HIVE, 1)
                .pattern("SSS")
                .pattern("HHH")
                .pattern("SSS")
                .define('S', ItemTags.PLANKS)
                .define('H', ItemRegistry.SPICY_HONEYCOMB)
                .unlockedBy("spicy_honeycomb", has(ItemRegistry.SPICY_HONEYCOMB))
                .save(consumer);
    }
}
