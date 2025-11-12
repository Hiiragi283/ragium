package hiiragi283.ragium.common.item.food

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.Level

class HTAmbrosiaItem(properties: Properties) : Item(properties.stacksTo(1).rarity(Rarity.EPIC)) {
    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        val copied: ItemStack = stack.copy()
        super.finishUsingItem(stack, level, livingEntity)
        return copied
    }
}
