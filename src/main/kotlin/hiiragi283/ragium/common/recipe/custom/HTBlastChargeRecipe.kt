package hiiragi283.ragium.common.recipe.custom

import hiiragi283.ragium.api.extension.createItemStack
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.CustomRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.Tags

class HTBlastChargeRecipe(category: CraftingBookCategory) : CustomRecipe(category) {
    override fun matches(input: CraftingInput, level: Level): Boolean {
        var isCharge = false
        for (index: Int in (0 until input.size())) {
            val stackIn: ItemStack = input.getItem(index)
            if (stackIn.isEmpty) continue
            if (stackIn.`is`(RagiumItems.BLAST_CHARGE) && !isCharge) {
                isCharge = true
            } else {
                if (!stackIn.`is`(Tags.Items.GUNPOWDERS)) return false
            }
        }
        return isCharge
    }

    override fun assemble(input: CraftingInput, registries: HolderLookup.Provider): ItemStack {
        var power = 1f
        for (index: Int in (0 until input.size())) {
            val stackIn: ItemStack = input.getItem(index)
            if (stackIn.isEmpty) continue
            if (stackIn.has(RagiumDataComponents.BLAST_POWER)) {
                power = stackIn.get(RagiumDataComponents.BLAST_POWER)!!
            } else if (stackIn.`is`(Tags.Items.GUNPOWDERS)) {
                power += 1f
            }
        }
        return createItemStack(RagiumItems.BLAST_CHARGE) {
            set(RagiumDataComponents.BLAST_POWER, power)
        }
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = width * height > 2

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.BLAST_CHARGE.get()
}
