package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.tier.HTBaseTier
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.item.ItemStack

class HTIridescentPowderItem(properties: Properties) : HTTierBasedItem(HTBaseTier.CREATIVE, properties) {
    override fun isFoil(stack: ItemStack): Boolean = true

    override fun canBeHurtBy(stack: ItemStack, source: DamageSource): Boolean = false
}
