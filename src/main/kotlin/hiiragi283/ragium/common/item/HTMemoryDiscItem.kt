package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.data.RagiumDataComponents
import hiiragi283.ragium.common.item.tooltip.HTMemoryDiscTooltipComponent
import java.util.Optional
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class HTMemoryDiscItem(properties: Properties) : Item(properties) {
    override fun getTooltipImage(stack: ItemStack): Optional<TooltipComponent> = stack.get(RagiumDataComponents.MEMORY_DISC_DATA)
        ?.let(::HTMemoryDiscTooltipComponent)
        .let { Optional.ofNullable(it) }

    override fun isFoil(stack: ItemStack): Boolean = stack.has(RagiumDataComponents.MEMORY_DISC_DATA) || super.isFoil(stack)
}
