package hiiragi283.ragium.common.item.block

import hiiragi283.ragium.api.block.attribute.getAttributeTier
import hiiragi283.ragium.api.item.HTBlockItem
import hiiragi283.ragium.api.item.component.HTItemContents
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.common.block.HTCrateBlock
import hiiragi283.ragium.common.tier.HTCrateTier
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack

class HTCrateBlockItem(block: HTCrateBlock, properties: Properties) : HTBlockItem<HTCrateBlock>(block, properties) {
    override fun getTier(): HTCrateTier = block.getAttributeTier<HTCrateTier>()

    override fun onDestroyed(itemEntity: ItemEntity, damageSource: DamageSource) {
        val stack: ItemStack = itemEntity.item
        val contents: HTItemContents = stack.get(RagiumDataComponents.ITEM_CONTENT) ?: return
        contents.filterNotNull().map(ImmutableItemStack::stack).forEach(itemEntity::spawnAtLocation)
    }
}
