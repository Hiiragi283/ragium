package hiiragi283.ragium.common.item.block

import hiiragi283.ragium.api.block.attribute.getAttributeTier
import hiiragi283.ragium.api.item.HTDescriptionBlockItem
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.attachments.HTAttachedItems
import hiiragi283.ragium.common.block.storage.HTCrateBlock
import hiiragi283.ragium.common.tier.HTCrateTier
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack

class HTCrateBlockItem(block: HTCrateBlock, properties: Properties) : HTDescriptionBlockItem<HTCrateBlock>(block, properties) {
    override fun getTier(): HTCrateTier? = block.getAttributeTier<HTCrateTier>()

    override fun onDestroyed(itemEntity: ItemEntity, damageSource: DamageSource) {
        val stack: ItemStack = itemEntity.item
        val contents: HTAttachedItems = stack.get(RagiumDataComponents.ITEM) ?: return
        contents
            .asSequence()
            .filterNotNull()
            .map(ImmutableItemStack::unwrap)
            .forEach(itemEntity::spawnAtLocation)
    }
}
