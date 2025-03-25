package hiiragi283.ragium.common.item

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class HTIceCreamItem(properties: Properties) : Item(properties) {
    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        if (!level.isClientSide) {
            livingEntity.extinguishFire()
        }
        return super.finishUsingItem(stack, level, livingEntity)
    }
}
