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
    @JvmField
    val ASSEMBLING: HTRecipeType<RTAssemblingRecipe> = create(RagiumConstants.ASSEMBLING)

    @JvmField
    val CRUSHING: HTRecipeType<RTCrushingRecipe> = create(RagiumConstants.CRUSHING)

    // Heat
    @JvmField
    val FREEZING: HTRecipeType<RTFreezingRecipe> = create(RagiumConstants.FREEZING)

    @JvmField
    val MELTING: HTRecipeType<RTMeltingRecipe> = create(RagiumConstants.MELTING)

    @JvmField
    val SMELTING: HTRecipeType<RTSmeltingRecipe> = create(HTConstants.SMELTING)

    // Chemical

    // Bio
    @JvmField
    val BREWING: HTRecipeType<RTBrewingRecipe> = create(RagiumConstants.BREWING)

    // Electronics

    // Arcane
}
