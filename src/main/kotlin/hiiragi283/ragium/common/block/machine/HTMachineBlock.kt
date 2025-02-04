package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.api.extension.machineKey
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.property.get
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class HTMachineBlock(properties: Properties) : HTEntityBlock.Horizontal(properties) {
    override fun getDescriptionId(): String = machineKey?.translationKey ?: super.descriptionId

    override fun appendHoverText(
        stack: ItemStack,
        context: Item.TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag,
    ) {
        machineKey?.appendTooltip(tooltipComponents::add)
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
        val machineKey: HTMachineKey = machineKey ?: return null
        return machineKey.getProperty()[HTMachinePropertyKeys.MACHINE_FACTORY]?.invoke(pos, state)
    }
}
