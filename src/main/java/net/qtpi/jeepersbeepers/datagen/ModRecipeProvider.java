package net.qtpi.jeepersbeepers.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.BlockFamilies;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.qtpi.jeepersbeepers.registry.BlockRegistry;
import net.qtpi.jeepersbeepers.registry.CompatibilityTagRegistry;
import net.qtpi.jeepersbeepers.registry.ItemRegistry;
import net.qtpi.jeepersbeepers.registry.TagRegistry;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {

        planksFromLog(consumer, BlockRegistry.MIGNONETTE_PLANKS, TagRegistry.Items.MIGNONETTE_LOGS, 4);
        generateRecipes(consumer, BlockRegistry.MIGNONETTE_FAMILY);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BlockRegistry.BUTTERDEW_LANTERN, 1)
                .pattern("B")
                .pattern("T")
                .define('B', BlockRegistry.CARVED_BUTTERDEW_SQUASH)
                .define('T', Items.TORCH)
                .unlockedBy("carved_butterdew_squash", has(BlockRegistry.CARVED_BUTTERDEW_SQUASH))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BlockRegistry.BUTTEWDEW_LANTEWN, 1)
                .pattern("B")
                .pattern("T")
                .define('B', BlockRegistry.CAWVED_BUTTEWDEW_SQUASH)
                .define('T', Items.TORCH)
                .unlockedBy("cawved_buttewdew_squash", has(BlockRegistry.CAWVED_BUTTEWDEW_SQUASH))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BlockRegistry.BEEPER_HIVE, 1)
                .pattern("SSS")
                .pattern("HHH")
                .pattern("SSS")
                .define('S', ItemTags.PLANKS)
                .define('H', ItemRegistry.SPICY_HONEYCOMB)
                .unlockedBy("spicy_honeycomb", has(ItemRegistry.SPICY_HONEYCOMB))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.BEEKEEPER_HAT, 1)
                .pattern(" L ")
                .pattern("SSS")
                .pattern("LFL")
                .define('L', Items.LEATHER)
                .define('S', Items.STRING)
                .define('F', ItemRegistry.BEEPER_FLUFF)
                .unlockedBy("beeper_fluff", has(ItemRegistry.BEEPER_FLUFF))
                .unlockedBy("leather", has(Items.LEATHER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.BEEKEEPER_TUNIC, 1)
                .pattern("L L")
                .pattern("LFL")
                .pattern("LFL")
                .define('L', Items.LEATHER)
                .define('F', ItemRegistry.BEEPER_FLUFF)
                .unlockedBy("beeper_fluff", has(ItemRegistry.BEEPER_FLUFF))
                .unlockedBy("leather", has(Items.LEATHER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.BEEKEEPER_PANTS, 1)
                .pattern("LLL")
                .pattern("L L")
                .pattern("F F")
                .define('L', Items.LEATHER)
                .define('F', ItemRegistry.BEEPER_FLUFF)
                .unlockedBy("beeper_fluff", has(ItemRegistry.BEEPER_FLUFF))
                .unlockedBy("leather", has(Items.LEATHER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.BEEKEEPER_BOOTS, 1)
                .pattern("F F")
                .pattern("L L")
                .define('L', Items.LEATHER)
                .define('F', ItemRegistry.BEEPER_FLUFF)
                .unlockedBy("beeper_fluff", has(ItemRegistry.BEEPER_FLUFF))
                .unlockedBy("leather", has(Items.LEATHER))
                .save(consumer);
    }
}
