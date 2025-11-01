package hiiragi283.ragium.api.item

import hiiragi283.ragium.api.tier.HTTierProvider
import net.minecraft.network.chat.Component
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block

/**
 * @see [mekanism.common.item.block.ItemBlockMekanism]
 */
open class HTBlockItem<BLOCK : Block>(block: BLOCK, properties: Properties) : BlockItem(block, properties) {
    @Suppress("UNCHECKED_CAST")
    override fun getBlock(): BLOCK = super.getBlock() as BLOCK

    open fun getTier(): HTTierProvider? = null

    override fun getName(stack: ItemStack): Component {
        var name: Component = super.getName(stack)
        val tier: HTTierProvider? = getTier()
        if (tier != null) {
            name = name.copy().withStyle(tier.getBaseTier().color)
        }
        return name
    }
}
