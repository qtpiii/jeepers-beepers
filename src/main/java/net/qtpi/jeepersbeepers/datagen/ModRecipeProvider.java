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
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Blocks;
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
        generateRecipes(consumer, BlockRegistry.LOAM_BRICK_FAMILY);
        generateRecipes(consumer, BlockRegistry.SHALE_FAMILY);

        oneToOneConversionRecipe(consumer, ItemRegistry.DRAGONFRUIT_SEEDS, ItemRegistry.DRAGONFRUIT, "dragonfruit_seeds", 4);
        oneToOneConversionRecipe(consumer, ItemRegistry.BUTTERDEW_SQUASH_SEEDS, ItemRegistry.BUTTERDEW_SQUASH_SLICE, "butterdew_squash_seeds", 2);

        simpleCookingRecipe(consumer, "smelting", RecipeSerializer.SMELTING_RECIPE, 200,
                ItemRegistry.BUTTERDEW_SQUASH_SLICE, ItemRegistry.ROASTED_BUTTERDEW_SQUASH_SLICE, 0.1f);
        simpleCookingRecipe(consumer, "smoking", RecipeSerializer.SMOKING_RECIPE, 100,
                ItemRegistry.BUTTERDEW_SQUASH_SLICE, ItemRegistry.ROASTED_BUTTERDEW_SQUASH_SLICE, 0.1f);
        simpleCookingRecipe(consumer, "campfire_cooking", RecipeSerializer.CAMPFIRE_COOKING_RECIPE, 600,
                ItemRegistry.BUTTERDEW_SQUASH_SLICE, ItemRegistry.ROASTED_BUTTERDEW_SQUASH_SLICE, 0.1f);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ItemRegistry.GINGERBREAD_COOKIE, 8)
                .requires(Items.WHEAT, 2)
                .requires(ItemRegistry.GINGER, 1)
                .requires(Items.SUGAR, 1)
                .unlockedBy("ginger", has(ItemRegistry.GINGER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BlockRegistry.SPICY_HONEY_LAMP, 1)
                .pattern("HHH")
                .pattern("HBH")
                .pattern("NTN")
                .define('H', ItemRegistry.SPICY_HONEYCOMB)
                .define('B', ItemRegistry.SPICY_HONEY_BOTTLE)
                .define('T', Items.TORCH)
                .define('N', Items.IRON_NUGGET)
                .unlockedBy("spicy_honey_bottle", has(ItemRegistry.SPICY_HONEY_BOTTLE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BlockRegistry.HONEY_LAMP, 1)
                .pattern("HHH")
                .pattern("HBH")
                .pattern("NTN")
                .define('H', Items.HONEYCOMB)
                .define('B', Items.HONEY_BOTTLE)
                .define('T', Items.TORCH)
                .define('N', Items.IRON_NUGGET)
                .unlockedBy("honey_bottle", has(Items.HONEY_BOTTLE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BlockRegistry.SPICY_HONEY_BLOCK, 4)
                .pattern("HH")
                .pattern("HH")
                .define('H', ItemRegistry.SPICY_HONEY_BOTTLE)
                .unlockedBy("spicy_honey_bottle", has(ItemRegistry.SPICY_HONEY_BOTTLE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BlockRegistry.SPICY_HONEYCOMB_BLOCK, 4)
                .pattern("HH")
                .pattern("HH")
                .define('H', ItemRegistry.SPICY_HONEYCOMB)
                .unlockedBy("spicy_honeycomb", has(ItemRegistry.SPICY_HONEYCOMB))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BlockRegistry.BEEPER_FLUFF_BLOCK, 1)
                .pattern("FF")
                .pattern("FF")
                .define('F', ItemRegistry.BEEPER_FLUFF)
                .unlockedBy("beeper_fluff", has(ItemRegistry.BEEPER_FLUFF))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BlockRegistry.LOAM_BRICKS, 4)
                .pattern("LL")
                .pattern("LL")
                .define('L', BlockRegistry.LOAM)
                .unlockedBy("loam", has(BlockRegistry.LOAM))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BlockRegistry.LOAM, 4)
                .pattern("SC")
                .pattern("CS")
                .define('S', Blocks.SAND)
                .define('C', Blocks.CLAY)
                .unlockedBy("loam", has(BlockRegistry.LOAM))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, BlockRegistry.BUTTERDEW_SQUASH, 4)
                .pattern("SS")
                .pattern("SS")
                .define('S', ItemRegistry.BUTTERDEW_SQUASH_SLICE)
                .unlockedBy("butterdew_squash_slice", has(ItemRegistry.BUTTERDEW_SQUASH_SLICE))
                .save(consumer);

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
