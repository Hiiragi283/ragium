package hiiragi283.ragium.client.event

import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.world.inventory.tooltip.TooltipComponent

@JvmInline
value class HTItemTooltipContent private constructor(val stack: ImmutableItemStack) : TooltipComponent {
    companion object {
        @JvmStatic
        fun of(stack: ImmutableItemStack): HTItemTooltipContent? = stack.takeIf(ImmutableItemStack::isNotEmpty)?.let(::HTItemTooltipContent)
    }
}
