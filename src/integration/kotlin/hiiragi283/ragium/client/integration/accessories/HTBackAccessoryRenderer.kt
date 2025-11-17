package hiiragi283.ragium.client.integration.accessories

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.ragium.client.renderer.scale
import io.wispforest.accessories.api.client.AccessoryRenderer
import io.wispforest.accessories.api.client.Side
import io.wispforest.accessories.api.client.SimpleAccessoryRenderer
import io.wispforest.accessories.api.slot.SlotReference
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.HumanoidModel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

class HTBackAccessoryRenderer : SimpleAccessoryRenderer {
    override fun <M : LivingEntity> align(
        stack: ItemStack,
        reference: SlotReference,
        model: EntityModel<M>,
        matrices: PoseStack,
    ) {
        if (model is HumanoidModel<M>) {
            AccessoryRenderer.transformToFace(matrices, model.body, Side.BACK)
            matrices.scale(2.5f)
        }
    }
}
