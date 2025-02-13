package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.property.get
import hiiragi283.ragium.common.block.HTEntityBlock
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class HTMachineBlock(val key: HTMachineKey, properties: Properties) : HTEntityBlock.Horizontal(properties) {
    override fun getDescriptionId(): String = key.translationKey

    override fun appendHoverText(
        stack: ItemStack,
        context: Item.TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag,
    ) {
        key.appendTooltip(tooltipComponents::add)
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? =
        key.getProperty()[HTMachinePropertyKeys.MACHINE_FACTORY]?.invoke(pos, state)
}
