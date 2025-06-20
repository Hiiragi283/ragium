package hiiragi283.ragium.api.recipe

import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe

interface HTUniversalRecipe : Recipe<HTUniversalRecipeInput> {
    override fun assemble(input: HTUniversalRecipeInput, registries: HolderLookup.Provider): ItemStack =
        throw UnsupportedOperationException()

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = throw UnsupportedOperationException()
}
