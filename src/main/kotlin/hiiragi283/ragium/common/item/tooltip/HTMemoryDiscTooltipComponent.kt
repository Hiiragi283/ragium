package hiiragi283.ragium.common.item.tooltip

import hiiragi283.ragium.api.data.HTMemoryDiscData
import net.minecraft.world.inventory.tooltip.TooltipComponent

@JvmRecord
data class HTMemoryDiscTooltipComponent(val data: HTMemoryDiscData) : TooltipComponent
