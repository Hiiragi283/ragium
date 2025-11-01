package hiiragi283.ragium.client.event

import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.world.inventory.tooltip.TooltipComponent

@JvmInline
value class HTItemTooltipContent(val stack: ImmutableItemStack) : TooltipComponent
