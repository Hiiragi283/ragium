package hiiragi283.ragium.integration.curios

import net.minecraft.world.item.ItemStack
import top.theillusivec4.curios.api.SlotContext
import top.theillusivec4.curios.api.type.capability.ICurio

class HTTickingCurio(private val stack: ItemStack) : ICurio {
    override fun getStack(): ItemStack = stack

    override fun curioTick(slotContext: SlotContext) {
        with(slotContext.entity) {
            stack.inventoryTick(level(), this, slotContext.index, false)
        }
    }
}
