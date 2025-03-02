package net.qtpi.jeepersbeepers.block.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.qtpi.jeepersbeepers.block.entity.MignonetteRootBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public class MignonetteRootBlockEntityRenderer implements BlockEntityRenderer<MignonetteRootBlockEntity> {
    private final MignonetteRootBlockModel model;

    public MignonetteRootBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        model = new MignonetteRootBlockModel(context.bakeLayer(MignonetteRootBlockModel.LAYER_LOCATION));
    }

    @Override
    public void render(@NotNull MignonetteRootBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        var random = blockEntity.getLevel().random.fork();
        random.setSeed(blockEntity.getBlockPos().asLong());

        for (int i = 0; i < random.nextInt(4, 10); i++) {
            Vec3 currentPos = new Vec3(0, 0, 0);
            currentPos = currentPos.add(0.5, 1.5, 0.5);
            currentPos = currentPos.add((random.nextFloat() - 0.5f) * 0.5, 0, (random.nextFloat() - 0.5f) * 0.5f);
            int segments = random.nextInt(2, 20);

            float xRot = random.nextFloat() * 180f;
            float zRot = random.nextFloat() * 180f;

            for (int j = 0; j < segments; j++) {
                final float turnFac = 50;
                zRot += random.nextFloat() * turnFac - turnFac / 2;
                xRot += random.nextFloat() * turnFac - turnFac / 2;

                if (currentPos.y > 1.5) {
                    xRot = (float)Mth.lerp(currentPos.y - 1.5f, xRot, 0f);
                    zRot = (float)Mth.lerp(currentPos.y - 1.5f, zRot, 0f);
                }

                float xRotSnapped = snapToAngle(xRot, 22.5f);
                float zRotSnapped = snapToAngle(zRot, 22.5f);

                float currentLength = (float)j / segments + 0.5f;

                currentPos = currentPos.subtract(new Vec3(
                        Mth.cos(xRotSnapped * 0.0174533f) * Mth.sin(zRotSnapped * 0.0174533f),
                        Mth.sin(xRotSnapped * 0.0174533f),
                        Mth.cos(xRotSnapped * 0.0174533f) * Mth.cos(zRotSnapped * 0.0174533f))
                        .scale(0.3125 * currentLength)
                );

                int currentScale = (int)Mth.lerp((double)j / segments, 4, 1);

                poseStack.pushPose();
                poseStack.translate(currentPos.x, currentPos.y, currentPos.z);
                poseStack.mulPose(Axis.YP.rotationDegrees(zRotSnapped + 90));
                poseStack.mulPose(Axis.ZP.rotationDegrees(360f - xRotSnapped + 90));// THIS WORKS DONT TOUCH
                poseStack.scale(currentScale, currentLength, currentScale);
                model.width = (currentScale + 1);
                model.height = currentLength * 2.5f;
                model.random = random;
                model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entitySolid(MignonetteRootBlockModel.TEXTURE)), packedLight, packedOverlay, 1, 1, 1, 1);
                poseStack.popPose();
            }
        }
    }

    public static float snapToAngle(float angle, float snap) {
        double remainder = angle % snap;
        if (remainder > snap / 2) {
            return (int) (angle / snap) * snap + snap;
        } else {
            return (int) (angle / snap) * snap;
        }
    }
}
