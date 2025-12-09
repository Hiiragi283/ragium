package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack

fun interface HTVanillaResultFactory {
    fun assemble(input: HTRecipeInput, registries: HolderLookup.Provider): ItemStack
}
