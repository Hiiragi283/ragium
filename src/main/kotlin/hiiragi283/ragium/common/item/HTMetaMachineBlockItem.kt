package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.machineTierOrNull
import hiiragi283.ragium.api.extension.machineTypeOrNull
import hiiragi283.ragium.api.machine.HTMachineType
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

class HTMetaMachineBlockItem(block: Block, settings: Settings) : BlockItem(block, settings) {
    override fun getName(stack: ItemStack): Text = stack.machineTypeOrNull?.let { type: HTMachineType ->
        stack.machineTierOrNull?.createPrefixedText(type)
    } ?: super.getName(stack)
}
