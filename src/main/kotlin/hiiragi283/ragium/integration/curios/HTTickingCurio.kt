package hiiragi283.ragium.integration.curios

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import top.theillusivec4.curios.api.SlotContext
import top.theillusivec4.curios.api.type.capability.ICurio

class HTTickingCurio(private val stack: ItemStack) : ICurio {
    override fun getStack(): ItemStack = stack

    override fun curioTick(slotContext: SlotContext) {
        val user: LivingEntity = slotContext.entity
        stack.inventoryTick(user.level(), user, slotContext.index, false)
    }
}
