package hiiragi283.ragium.api.item

import hiiragi283.ragium.api.extension.restDamage
import hiiragi283.ragium.api.tag.RagiumBlockTags
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier

class HTForgeHammerItem(tier: Tier, properties: Properties) : DiggerItem(tier, RagiumBlockTags.MINEABLE_WITH_HAMMER, properties) {
    override fun hasCraftingRemainingItem(stack: ItemStack): Boolean = stack.restDamage > 0

    override fun getCraftingRemainingItem(stack: ItemStack): ItemStack {
        if (hasCraftingRemainingItem(stack)) {
            val copied: ItemStack = stack.copy()
            copied.damageValue++
            return copied
        }
        return super.getCraftingRemainingItem(stack)
    }
}
