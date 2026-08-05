package hiiragi283.ragium.common.item.tooltip

import hiiragi283.core.api.storage.item.HTItemResourceType
import net.minecraft.world.inventory.tooltip.TooltipComponent

@JvmRecord
data class HTMemoryDiscTooltipComponent(val data: HTItemResourceType) : TooltipComponent
