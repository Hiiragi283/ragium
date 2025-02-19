package hiiragi283.ragium.common.item.tool

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.restDamage
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class HTCraftingToolItem(properties: Properties) : Item(properties.durability(RagiumAPI.getInstance().getForgeHammerDurability())) {
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
