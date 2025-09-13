package hiiragi283.ragium.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import io.wispforest.accessories.api.client.AccessoryRenderer
import io.wispforest.accessories.api.client.SimpleAccessoryRenderer
import io.wispforest.accessories.api.slot.SlotReference
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.HumanoidModel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

class HTBundleAccessoryRenderer : SimpleAccessoryRenderer {
    override fun <M : LivingEntity> align(
        stack: ItemStack,
        reference: SlotReference,
        model: EntityModel<M>,
        matrices: PoseStack,
    ) {
        if (model is HumanoidModel<M>) {
            AccessoryRenderer.transformToModelPart(matrices, model.rightLeg, 1, 0.75, 0.0)
            matrices.rotateAround(Axis.YN.rotationDegrees(90f), 0f, 0f, 0f)
        }
    }
}
