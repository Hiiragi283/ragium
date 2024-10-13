package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.itemSettings
import hiiragi283.ragium.api.extension.machineTierOrNull
import hiiragi283.ragium.api.extension.machineTypeOrNull
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.block.HTMetaMachineBlock
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

object HTMetaMachineBlockItem : BlockItem(HTMetaMachineBlock, itemSettings()) {
    override fun getName(stack: ItemStack): Text = stack.machineTypeOrNull?.let { type: HTMachineType ->
        stack.machineTierOrNull?.createPrefixedText(type)
    } ?: super.getName(stack)
}
