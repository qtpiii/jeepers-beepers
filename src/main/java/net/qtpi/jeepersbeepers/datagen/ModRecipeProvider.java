package net.qtpi.jeepersbeepers.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.BlockFamilies;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.qtpi.jeepersbeepers.registry.BlockRegistry;
import net.qtpi.jeepersbeepers.registry.ItemRegistry;
import net.qtpi.jeepersbeepers.registry.TagRegistry;

import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {

        planksFromLog(consumer, BlockRegistry.MIGNONETTE_PLANKS, TagRegistry.Items.MIGNONETTE_LOGS, 4);
        stairBuilder(BlockRegistry.MIGNONETTE_STAIRS, Ingredient.of(BlockRegistry.MIGNONETTE_PLANKS));
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, BlockRegistry.MIGNONETTE_SLAB, Ingredient.of(BlockRegistry.MIGNONETTE_PLANKS));
        buttonBuilder(BlockRegistry.MIGNONETTE_BUTTON, Ingredient.of(BlockRegistry.MIGNONETTE_PLANKS));
        pressurePlateBuilder(RecipeCategory.REDSTONE, BlockRegistry.MIGNONETTE_PRESSURE_PLATE, Ingredient.of(BlockRegistry.MIGNONETTE_PLANKS));
        fenceBuilder(BlockRegistry.MIGNONETTE_FENCE, Ingredient.of(BlockRegistry.MIGNONETTE_PLANKS));
        fenceGateBuilder(BlockRegistry.MIGNONETTE_FENCE_GATE, Ingredient.of(BlockRegistry.MIGNONETTE_PLANKS));
        doorBuilder(BlockRegistry.MIGNONETTE_DOOR, Ingredient.of(BlockRegistry.MIGNONETTE_PLANKS));
        trapdoorBuilder(BlockRegistry.MIGNONETTE_TRAPDOOR, Ingredient.of(BlockRegistry.MIGNONETTE_PLANKS));

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
