package hiiragi283.ragium.common.item.food

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class HTAmbrosiaItem(properties: Properties) : Item(properties.stacksTo(1)) {
    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        stack.grow(1)
        return super.finishUsingItem(stack, level, livingEntity)
    }
}
