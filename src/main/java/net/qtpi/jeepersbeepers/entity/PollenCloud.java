package net.qtpi.jeepersbeepers.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.qtpi.jeepersbeepers.JeepersBeepers;
import net.qtpi.jeepersbeepers.registry.EntityRegistry;
import net.qtpi.jeepersbeepers.registry.ParticleRegistry;
import net.qtpi.jeepersbeepers.registry.TagRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

// handles pollenation
public class PollenCloud extends Entity {
    public ArrayList<TagKey<Block>> sourceCropTags;
    public BlockPos sourceCropPos;

    public PollenCloud(EntityType<?> entityType, Level level) {
        super(EntityRegistry.POLLEN_CLOUD, level);
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compound) {

    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compound) {

    }

    @Override
    public void tick() {
        super.tick();
        for (int i = 0; i < 20; i++) {
            level().addParticle(ParticleRegistry.BEEPER_SNEEZE_POOF, getX(), getY(), getZ(), (random.nextDouble() * 2 - 1) * 0.5, (random.nextDouble() * 2 - 1) * 0.2, (random.nextDouble() * 2 - 1) * 0.5);
        }

        if (sourceCropTags == null || sourceCropPos == null) {
            discard();
            return;
        }

        // pollenation algo begins here
        ArrayList<BlockPos> viableBlockPositions = new ArrayList<>();
        ArrayList<ArrayList<TagKey<Block>>> viableBlockMatchingTags = new ArrayList<>();
        final int size = 5;
        for (int x = -size; x <= size; x++) {
            for (int y = -size; y <= size; y++) {
                for (int z = -size; z <= size; z++) {
                    BlockPos currentPos = new BlockPos(x, y, z);
                    currentPos = currentPos.offset(this.blockPosition());
                    BlockState currentBlock = level().getBlockState(currentPos);
                    ArrayList<TagKey<Block>> currentBlockTags = currentBlock.getTags().collect(Collectors.toCollection(ArrayList::new));

                    if (!currentBlockTags.contains(TagRegistry.Blocks.BEEPER_CAN_POLLINATE)) continue;

                    ArrayList<TagKey<Block>> matchingTags = new ArrayList<>();
                    for (TagKey<Block> tag : currentBlockTags) {
                        if (sourceCropTags.contains(tag)) {
                            matchingTags.add(tag);
                        }
                    }

                    if (!matchingTags.isEmpty()) {
                        viableBlockMatchingTags.add(matchingTags);
                        viableBlockPositions.add(currentPos);
                        //tryPlaceHybrid(matchingTags, currentPos);
                    }
                }
            }
        }

        for (int i = 0; i < viableBlockPositions.size(); i++) {
            tryPlaceHybrid(viableBlockMatchingTags.get(i), viableBlockPositions.get(i));
        }

        discard();
    }

    private void tryPlaceHybrid(ArrayList<TagKey<Block>> matchingTags, BlockPos viableBlockPos) {
        Vec3i[] dirs = new Vec3i[]{
                new Vec3i(1, 0, 1),
                new Vec3i(0, 0, 1),
                new Vec3i(-1, 0, 1),
                new Vec3i(-1, 0, 0),
                new Vec3i(-1, 0, -1),
                new Vec3i(0, 0, -1),
                new Vec3i(1, 0, -1),
                new Vec3i(1, 0, 0)
        };

        ArrayList<Vec3i> dirsToList = (ArrayList<Vec3i>)Arrays.stream(dirs).sorted().collect(Collectors.toList());
        Collections.shuffle(dirsToList);

        BlockState sourceCrop = level().getBlockState(sourceCropPos).getBlock().defaultBlockState();
        if (sourceCrop.is(Blocks.ATTACHED_MELON_STEM)) {
            sourceCrop = Blocks.MELON_STEM.defaultBlockState();
        }
        if (sourceCrop.is(Blocks.ATTACHED_PUMPKIN_STEM)) {
            sourceCrop = Blocks.PUMPKIN_STEM.defaultBlockState();
        }
        BlockState viableCrop = level().getBlockState(viableBlockPos).getBlock().defaultBlockState();
        if (viableCrop.is(Blocks.ATTACHED_MELON_STEM)) {
            viableCrop = Blocks.MELON_STEM.defaultBlockState();
        }
        if (viableCrop.is(Blocks.ATTACHED_PUMPKIN_STEM)) {
            viableCrop = Blocks.PUMPKIN_STEM.defaultBlockState();
        }

        BlockState cropToPlace;
        double chance;
        if (viableCrop.is(sourceCrop.getBlock())) {
            cropToPlace = sourceCrop;
            chance = 0.10;//%
        }
        else {
            chance = 1.00;//%
            cropToPlace = TagRegistry.Blocks.pickHybridFromTags(matchingTags, random);
        }

        for (Vec3i dir : dirsToList) {
            BlockPos viableBlockPosOffset = viableBlockPos.offset(dir);

            if (level().getBlockState(viableBlockPosOffset).getBlock() != Blocks.AIR) continue;

            if (level().getBlockState(viableBlockPosOffset.below()).getBlock() == Blocks.FARMLAND && random.nextDouble() < chance) {
                getServer().getLevel(level().dimension()).setBlockAndUpdate(viableBlockPosOffset, cropToPlace);
                break;
            }
        }
    }
}
