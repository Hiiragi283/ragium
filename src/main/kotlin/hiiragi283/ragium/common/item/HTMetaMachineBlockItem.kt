package hiiragi283.ragium.common.item

import hiiragi283.ragium.common.block.HTMetaMachineBlock
import hiiragi283.ragium.common.util.itemSettings
import hiiragi283.ragium.common.util.machineTier
import hiiragi283.ragium.common.util.machineType
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

object HTMetaMachineBlockItem : BlockItem(HTMetaMachineBlock, itemSettings()) {
    override fun getName(stack: ItemStack): Text = stack.machineTier.createPrefixedText(stack.machineType)
}
