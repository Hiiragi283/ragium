package hiiragi283.ragium.api.item

import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipProvider
import java.util.function.Consumer

fun interface HTTooltipProvider : TooltipProvider {
    fun addToTooltip(context: Item.TooltipContext, consumer: (Component) -> Unit, flag: TooltipFlag)

    override fun addToTooltip(context: Item.TooltipContext, tooltipAdder: Consumer<Component>, tooltipFlag: TooltipFlag) {
        addToTooltip(context, tooltipAdder::accept, tooltipFlag)
    }
}
