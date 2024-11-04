package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.machineTier
import hiiragi283.ragium.api.extension.machineTypeOrNull
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

@Deprecated("May be removed")
class HTMetaMachineBlockItem(block: Block, settings: Settings) : BlockItem(block, settings) {
    override fun getName(stack: ItemStack): Text =
        stack.machineTypeOrNull?.let(stack.machineTier::createPrefixedText) ?: super.getName(stack)
}
