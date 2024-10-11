package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.util.itemSettings
import hiiragi283.ragium.common.block.HTMetaMachineBlock
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

object HTMetaMachineBlockItem : BlockItem(HTMetaMachineBlock, itemSettings()) {
    override fun getName(stack: ItemStack): Text = stack.get(RagiumComponentTypes.MACHINE_TYPE)?.let { type: HTMachineType ->
        stack.get(RagiumComponentTypes.MACHINE_TIER)?.createPrefixedText(type)
    } ?: super.getName(stack)
}
