package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.impl.HTAlloyingRecipe
import hiiragi283.ragium.api.recipe.impl.HTCompressingRecipe
import hiiragi283.ragium.api.recipe.impl.HTCrushingRecipe
import hiiragi283.ragium.api.recipe.impl.HTEnchantingRecipe
import hiiragi283.ragium.api.recipe.impl.HTExtractingRecipe
import hiiragi283.ragium.api.recipe.impl.HTMeltingRecipe
import hiiragi283.ragium.api.recipe.impl.HTPulverizingRecipe
import hiiragi283.ragium.api.recipe.impl.HTRefiningRecipe
import hiiragi283.ragium.api.recipe.impl.HTSawmillRecipe
import hiiragi283.ragium.api.recipe.impl.HTSimulatingRecipe
import hiiragi283.ragium.api.registry.impl.HTDeferredRecipeSerializer
import net.minecraft.world.item.crafting.Recipe

object RagiumRecipeSerializers {
    @JvmField
    val SAWMILL: HTDeferredRecipeSerializer<HTSawmillRecipe> = create("sawmill")

    // Machine
    @JvmField
    val ALLOYING: HTDeferredRecipeSerializer<HTAlloyingRecipe> = create(RagiumConst.ALLOYING)

    @JvmField
    val COMPRESSING: HTDeferredRecipeSerializer<HTCompressingRecipe> = create(RagiumConst.COMPRESSING)

    @JvmField
    val CRUSHING: HTDeferredRecipeSerializer<HTCrushingRecipe> = create(RagiumConst.CRUSHING)

    @JvmField
    val ENCHANTING: HTDeferredRecipeSerializer<HTEnchantingRecipe> = create(RagiumConst.ENCHANTING)

    @JvmField
    val EXTRACTING: HTDeferredRecipeSerializer<HTExtractingRecipe> = create(RagiumConst.EXTRACTING)

    @JvmField
    val FLUID_TRANSFORM: HTDeferredRecipeSerializer<HTRefiningRecipe> = create(RagiumConst.FLUID_TRANSFORM)

    @JvmField
    val MELTING: HTDeferredRecipeSerializer<HTMeltingRecipe> = create(RagiumConst.MELTING)

    @JvmField
    val PULVERIZING: HTDeferredRecipeSerializer<HTPulverizingRecipe> = create("pulverizing")

    @JvmField
    val SIMULATING: HTDeferredRecipeSerializer<HTSimulatingRecipe> = create(RagiumConst.SIMULATING)

    @JvmStatic
    private fun <RECIPE : Recipe<*>> create(path: String): HTDeferredRecipeSerializer<RECIPE> =
        HTDeferredRecipeSerializer(RagiumAPI.id(path))
}
