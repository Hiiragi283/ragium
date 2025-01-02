package hiiragi283.ragium.common.item

import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class HTAmbrosiaItem(settings: Settings) : Item(settings) {
    override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
        stack.increment(1)
        return super.finishUsing(stack, world, user)
    }
}
