package hiiragi283.ragium.client.accessory

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.ragium.config.RagiumConfig
import io.wispforest.accessories.api.client.AccessoryRenderer
import io.wispforest.accessories.api.client.SimpleAccessoryRenderer
import io.wispforest.accessories.api.slot.SlotReference
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.HumanoidModel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

class HTGogglesAccessoryRenderer : SimpleAccessoryRenderer {
    override fun <M : LivingEntity> align(
        stack: ItemStack,
        reference: SlotReference,
        model: EntityModel<M>,
        matrices: PoseStack,
    ) {
        if (model is HumanoidModel<M>) {
            val height: Double = RagiumConfig.CLIENT.gogglesRendererHeight.asDouble
            AccessoryRenderer.transformToModelPart(matrices, model.head, 0, height, 1)
        }
    }
}
