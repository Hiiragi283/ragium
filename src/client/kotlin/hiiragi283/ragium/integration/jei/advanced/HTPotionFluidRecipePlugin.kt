package hiiragi283.ragium.integration.jei.advanced

import hiiragi283.lib.integration.jei.getIngredient
import hiiragi283.lib.item.alchemy.HTBottleType
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.recipe.viewer.HTDoubleToSingleViewerRecipe
import hiiragi283.lib.recipe.viewer.HTItemSeparatingViewerRecipe
import hiiragi283.ragium.common.recipe.RTPotionBottleCentrifugingRecipe
import hiiragi283.ragium.common.recipe.RTPotionBottleFillingRecipe
import hiiragi283.ragium.integration.jei.RagiumJeiRecipeTypes
import mezz.jei.api.neoforge.NeoForgeTypes
import mezz.jei.api.recipe.IFocus
import mezz.jei.api.recipe.RecipeIngredientRole
import mezz.jei.api.recipe.advanced.IRecipeManagerPlugin
import mezz.jei.api.recipe.types.IRecipeType
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.crafting.display.SlotDisplay
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.display.FluidStackSlotDisplay

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
                    HTDoubleToSingleViewerRecipe(
                        null,
                        SlotDisplay.ItemSlotDisplay(bottleType.emptyItem),
                        FluidStackSlotDisplay(stackFocused),
                        bottleType.filledItem
                            .asTemplate(patch = potionPatch)
                            .getOrThrow()
                            .let(SlotDisplay::ItemStackSlotDisplay),
                        RTPotionBottleFillingRecipe.PROGRESS_DATA,
                    )
                }

            // ポーション瓶から液体ポーションを取り出す
            RecipeIngredientRole.OUTPUT if recipeType == RagiumJeiRecipeTypes.CENTRIFUGING ->
                HTBottleType.entries.map { bottleType: HTBottleType ->
                    HTItemSeparatingViewerRecipe(
                        null,
                        SlotDisplay.ItemStackSlotDisplay(bottleType.filledItem.asTemplate(patch = potionPatch).getOrThrow()),
                        listOf(SlotDisplay.ItemSlotDisplay(bottleType.emptyItem)),
                        FluidStackSlotDisplay(stackFocused),
                        RTPotionBottleCentrifugingRecipe.PROGRESS_DATA,
                    )
                }

            else -> listOf()
        }.filterIsInstance(recipeType.recipeClass)
    }

    override fun <T : Any> getRecipes(recipeType: IRecipeType<T>): List<T> = listOf()
}
