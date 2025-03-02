package net.qtpi.jeepersbeepers.block.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.qtpi.jeepersbeepers.JeepersBeepers;
import org.jetbrains.annotations.NotNull;

public class MignonetteRootBlockModel extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(JeepersBeepers.MOD_ID, "main"), "mignonette_root_model");
    public static final ResourceLocation TEXTURE = new ResourceLocation(JeepersBeepers.MOD_ID, "textures/block/mignonette_log.png");

    public final ModelPart main;

    public MignonetteRootBlockModel(ModelPart root) {
        super(RenderType::entitySolid);
        main = root;
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1, 0, -1, 2, 5, 2),
                PartPose.rotation(0, 0, 0));
        return LayerDefinition.create(mesh, 16, 16);
    }

    float width;
    float height;
    RandomSource random;
    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        ModelPart.Polygon[] polygons = main.getChild("main").cubes.get(0).polygons;
        if (width == 4) {
            width = 5;
        }
        for (ModelPart.Polygon polygon : polygons) {
            float xOffset = (random.nextInt(10) % 16) / 16f;
            polygon.vertices[1] = polygon.vertices[1].remap(width / 16f + xOffset, height / 16f);
            polygon.vertices[2] = polygon.vertices[2].remap(width / 16f + xOffset, 0.0f);
            polygon.vertices[3] = polygon.vertices[3].remap(0.0f + xOffset, 0.0f);
            polygon.vertices[0] = polygon.vertices[0].remap(0.0f + xOffset, height / 16f);
        }
        main.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
