package hiiragi283.ragium.api.material

import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

class HTTagPrefixedBlockItem(block: HTTagPrefixedBlock, settings: Settings) : BlockItem(block, settings) {
    val prefix: HTTagPrefix = block.prefix
    val key: HTMaterialKey = block.key

    override fun getName(): Text = prefix.getText(key)

    override fun getName(stack: ItemStack): Text = prefix.getText(key)
}
