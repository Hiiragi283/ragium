package hiiragi283.ragium.api.recipe

import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConstants
import net.minecraft.world.item.crafting.Recipe

data object RagiumRecipeTypes {
    @JvmStatic
    val allTypes: Set<HTRecipeType<*>> field: MutableSet<HTRecipeType<*>> = mutableSetOf()

    @JvmStatic
    private fun <T : Recipe<*>> create(name: String): HTRecipeType<T> = HTRecipeType<T>(RagiumAPI.id(name)).also(allTypes::add)

    // Mechanical

    // Heat
    @JvmField
    val MELTING: HTRecipeType<RTMeltingRecipe> = create(RagiumConstants.MELTING)

    @JvmField
    val SMELTING: HTRecipeType<RTSmeltingRecipe> = create(HTConstants.SMELTING)

    // Chemical

    // Bio

    // Electronics

    // Arcane
}
