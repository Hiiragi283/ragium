package hiiragi283.ragium.common.recipe

import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

fun interface HTVanillaResultFactory<INPUT : RecipeInput> {
    fun assemble(input: INPUT, registries: HolderLookup.Provider): ItemStack
}
