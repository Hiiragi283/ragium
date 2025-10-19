package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.base.HTCombineItemToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemWithFluidToChancedItemRecipe
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.recipe.input.HTMultiItemRecipeInput
import hiiragi283.ragium.api.recipe.manager.HTRecipeType
import hiiragi283.ragium.api.recipe.manager.toFindable
import hiiragi283.ragium.api.recipe.manager.withPrefix
import hiiragi283.ragium.api.registry.impl.HTDeferredRecipeType
import hiiragi283.ragium.api.text.RagiumTranslation
import net.minecraft.network.chat.Component
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleItemRecipe
import net.minecraft.world.item.crafting.SingleRecipeInput

object RagiumRecipeTypes {
    @JvmField
    val SAWMILL: HTDeferredRecipeType<SingleRecipeInput, SingleItemRecipe> = create(RagiumConst.SAWMILL)

    @JvmField
    val CUTTING: HTRecipeType<SingleRecipeInput, SingleItemRecipe> = object :
        HTRecipeType<SingleRecipeInput, SingleItemRecipe> {
        override fun getAllHolders(manager: RecipeManager): Sequence<RecipeHolder<out SingleItemRecipe>> = buildList {
            addAll(SAWMILL.getAllHolders(manager))
            addAll(
                RecipeType.STONECUTTING
                    .toFindable()
                    .getAllHolders(manager)
                    .map { holder -> holder.withPrefix("/") },
            )
        }.asSequence()

        override fun getText(): Component = RagiumTranslation.RECIPE_CUTTING.getText()
    }

    // Machine
    @JvmField
    val ALLOYING: HTDeferredRecipeType<HTMultiItemRecipeInput, HTCombineItemToItemRecipe> = create(RagiumConst.ALLOYING)

    @JvmField
    val COMPRESSING: HTDeferredRecipeType<SingleRecipeInput, HTSingleInputRecipe> = create(RagiumConst.COMPRESSING)

    @JvmField
    val CRUSHING: HTDeferredRecipeType<SingleRecipeInput, HTItemToChancedItemRecipe> = create(RagiumConst.CRUSHING)

    @JvmField
    val ENCHANTING: HTDeferredRecipeType<HTMultiItemRecipeInput, HTCombineItemToItemRecipe> = create(RagiumConst.ENCHANTING)

    @JvmField
    val EXTRACTING: HTDeferredRecipeType<SingleRecipeInput, HTSingleInputRecipe> = create(RagiumConst.EXTRACTING)

    @JvmField
    val FLUID_TRANSFORM: HTDeferredRecipeType<HTItemWithFluidRecipeInput, HTFluidTransformRecipe> = create(RagiumConst.FLUID_TRANSFORM)

    @JvmField
    val MELTING: HTDeferredRecipeType<SingleRecipeInput, HTSingleInputFluidRecipe> = create(RagiumConst.MELTING)

    @JvmField
    val PLANTING: HTDeferredRecipeType<HTItemWithFluidRecipeInput, HTItemWithFluidToChancedItemRecipe> = create(RagiumConst.PLANTING)

    @JvmField
    val SIMULATING: HTDeferredRecipeType<HTMultiItemRecipeInput, HTItemWithCatalystToItemRecipe> = create(RagiumConst.SIMULATING)

    @JvmField
    val WASHING: HTDeferredRecipeType<HTItemWithFluidRecipeInput, HTItemWithFluidToChancedItemRecipe> = create(RagiumConst.WASHING)

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> create(path: String): HTDeferredRecipeType<INPUT, RECIPE> =
        HTDeferredRecipeType(RagiumAPI.id(path))
}
