package hiiragi283.ragium.common.item

import hiiragi283.ragium.common.block.HTBaseMachineBlock
import hiiragi283.ragium.common.util.machineType
import hiiragi283.ragium.common.util.tier
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

class HTMachineBlockItem(block: HTBaseMachineBlock, settings: Settings) :
    BlockItem(
        block,
        settings.machineType(block.machineType).tier(block.tier),
    ) {
    override fun getName(): Text = block.name

    override fun getName(stack: ItemStack): Text = block.name
}
