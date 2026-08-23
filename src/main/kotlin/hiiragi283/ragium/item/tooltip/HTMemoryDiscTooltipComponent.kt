package hiiragi283.ragium.item.tooltip

import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.ItemStackTemplate

@JvmRecord
data class HTMemoryDiscTooltipComponent(val data: ItemStackTemplate) : TooltipComponent
