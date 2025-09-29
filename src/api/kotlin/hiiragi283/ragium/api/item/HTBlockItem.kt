package hiiragi283.ragium.api.item

import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.block.Block

/**
 * @see [mekanism.common.item.block.ItemBlockMekanism]
 */
open class HTBlockItem<BLOCK : Block>(block: BLOCK, properties: Properties) : BlockItem(block, properties) {
    @Suppress("UNCHECKED_CAST")
    override fun getBlock(): BLOCK = super.getBlock() as BLOCK
}
