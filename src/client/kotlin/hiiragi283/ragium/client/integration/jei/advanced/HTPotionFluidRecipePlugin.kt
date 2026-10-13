package hiiragi283.ragium.client.integration.jei.advanced

import hiiragi283.lib.integration.jei.HTJeiRecipeHelper
import hiiragi283.lib.integration.jei.HTJeiRecipeHolder
import hiiragi283.lib.integration.jei.getIngredient
import hiiragi283.lib.item.alchemy.HTBottleType
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.client.integration.jei.RagiumJeiRecipeTypes
import hiiragi283.ragium.common.recipe.RTPotionBottleCentrifugingRecipe
import hiiragi283.ragium.common.recipe.RTPotionBottleFillingRecipe
import mezz.jei.api.neoforge.NeoForgeTypes
import mezz.jei.api.recipe.IFocus
import mezz.jei.api.recipe.RecipeIngredientRole
import mezz.jei.api.recipe.advanced.IRecipeManagerPlugin
import mezz.jei.api.recipe.types.IRecipeType
import net.minecraft.core.component.DataComponentPatch
import net.neoforged.neoforge.fluids.FluidStack

data object HTPotionFluidRecipePlugin : IRecipeManagerPlugin {
    private fun getPotionFluid(focus: IFocus<*>): FluidStack? = focus
        .getIngredient(NeoForgeTypes.FLUID_STACK)
        ?.takeIf(HTPotionHelper::hasAnyEffect)

    override fun <V : Any> getRecipeTypes(focus: IFocus<V>): List<IRecipeType<*>> = when {
        getPotionFluid(focus) == null -> listOf()

        else -> when (focus.role) {
            RecipeIngredientRole.INPUT -> listOf(RagiumJeiRecipeTypes.FILLING)
            RecipeIngredientRole.OUTPUT -> listOf(RagiumJeiRecipeTypes.CENTRIFUGING)
            else -> listOf()
        }
    }

    override fun <T : Any, V : Any> getRecipes(recipeType: IRecipeType<T>, focus: IFocus<V>): List<T> {
        val stackFocused: FluidStack =
            getPotionFluid(focus)?.copyWithAmount(HTPotionHelper.BOTTLE_AMOUNT) ?: return listOf()
        val potionPatch: DataComponentPatch = HTPotionHelper.createPotionPatch(stackFocused)
        return when (focus.role) {
            // 液体ポーションをポーション瓶に入れる
            RecipeIngredientRole.INPUT if recipeType == RagiumJeiRecipeTypes.FILLING ->
                HTBottleType.entries.map { bottleType: HTBottleType ->
                    RagiumRecipeBuilders.filling {
                        itemIngredient { items { +bottleType.emptyItem } }
                        fluidIngredient = HTJeiRecipeHelper.fakeFluid(stackFocused)
                        result {
                            bottleType.filledItem
                                .asTemplate(patch = potionPatch)
                                .getOrThrow()
                                .let(::from)
                        }
                        progressData = RTPotionBottleFillingRecipe.PROGRESS_DATA
                    }.buildSynthetic { _, recipe -> HTJeiRecipeHolder(recipe) }
                }

            // ポーション瓶から液体ポーションを取り出す
            RecipeIngredientRole.OUTPUT if recipeType == RagiumJeiRecipeTypes.CENTRIFUGING ->
                HTBottleType.entries.map { bottleType: HTBottleType ->
                    RagiumRecipeBuilders.centrifuging {
                        +HTJeiRecipeHelper.fakeItem(bottleType.filledItem.asStackOrEmpty(patch = potionPatch))
                        itemResult { +bottleType.emptyItem }
                        fluidResult { from(stackFocused) }
                        progressData = RTPotionBottleCentrifugingRecipe.PROGRESS_DATA
                    }.buildSynthetic { _, recipe -> HTJeiRecipeHolder(recipe) }
                }

            else -> listOf()
        }.filterIsInstance(recipeType.recipeClass)
    }

    override fun <T : Any> getRecipes(recipeType: IRecipeType<T>): List<T> = listOf()
}
