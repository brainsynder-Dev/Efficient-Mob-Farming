package org.bsdevelopment.mobfarming.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.bsdevelopment.mobfarming.blocks.entity.MobBlenderBase;
import org.bsdevelopment.mobfarming.items.ModItems;

@OnlyIn(Dist.CLIENT)
public class MobBlenderRender implements BlockEntityRenderer<MobBlenderBase> {
    private final Minecraft minecraft;

    public MobBlenderRender (BlockEntityRendererProvider.Context context) {
        minecraft = Minecraft.getInstance();
    }

    @Override
    public void render(MobBlenderBase tileEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        int packedLight = LevelRenderer.getLightColor(tileEntity.getLevel(), tileEntity.getBlockPos());

        float ticks = tileEntity.animationTicks + (tileEntity.animationTicks - tileEntity.prevAnimationTicks)  * partialTicks;

        {
            poseStack.pushPose();

            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.scale(2, 2, 2);

            if (tileEntity.active) poseStack.mulPose(Axis.YN.rotationDegrees(ticks));
            minecraft.getItemRenderer().renderStatic(ModItems.BLENDER_BLADE1.get().getDefaultInstance(), ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, tileEntity.getLevel(), 0);

            poseStack.popPose();
        }
        {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.scale(2, 2, 2);
            if (tileEntity.active) poseStack.mulPose(Axis.YP.rotationDegrees(ticks));
            minecraft.getItemRenderer().renderStatic(ModItems.BLENDER_BLADE2.get().getDefaultInstance(), ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, tileEntity.getLevel(), 0);

            poseStack.popPose();
        }
    }
}
