package hiiragi283.ragium.common.item.block

import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.common.block.HTCrateBlock
import hiiragi283.ragium.common.variant.HTCrateVariant
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack

class HTCrateBlockItem(variant: HTCrateVariant, properties: Properties) :
    HTVariantBlockItem<HTCrateVariant, HTCrateBlock>(variant, properties) {
    override fun onDestroyed(itemEntity: ItemEntity, damageSource: DamageSource) {
        val stack: ItemStack = itemEntity.item
        val stackIn: ImmutableItemStack = stack.get(RagiumDataComponents.ITEM_CONTENT) ?: return
        itemEntity.spawnAtLocation(stackIn.stack)
    }
}
