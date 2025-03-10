package hiiragi283.ragium.api.block

import hiiragi283.ragium.api.machine.HTMachineType
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

abstract class HTMachineBlock(val machineType: HTMachineType, properties: Properties) : HTEntityBlock(properties) {
    override fun getDescriptionId(): String = machineType.translationKey

    override fun appendHoverText(
        stack: ItemStack,
        context: Item.TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        tooltips.add(machineType.descriptionText)
    }
}
