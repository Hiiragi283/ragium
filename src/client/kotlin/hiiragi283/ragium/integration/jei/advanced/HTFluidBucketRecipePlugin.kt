package hiiragi283.ragium.integration.jei.advanced

import hiiragi283.lib.fluid.createBucket
import hiiragi283.lib.integration.jei.getIngredient
import hiiragi283.lib.item.toTemplateOrNull
import hiiragi283.lib.recipe.viewer.HTDoubleToSingleViewerRecipe
import hiiragi283.lib.recipe.viewer.HTItemSeparatingViewerRecipe
import hiiragi283.ragium.common.recipe.RTBucketCentrifugingRecipe
import hiiragi283.ragium.common.recipe.RTBucketFillingRecipe
import hiiragi283.ragium.integration.jei.RagiumJeiRecipeTypes
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.neoforge.NeoForgeTypes
import mezz.jei.api.recipe.IFocus
import mezz.jei.api.recipe.RecipeIngredientRole
import mezz.jei.api.recipe.advanced.IRecipeManagerPlugin
import mezz.jei.api.recipe.types.IRecipeType
import net.minecraft.world.item.BucketItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.display.SlotDisplay
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.display.FluidStackSlotDisplay
import net.neoforged.neoforge.transfer.fluid.FluidUtil

data object HTFluidBucketRecipePlugin : IRecipeManagerPlugin {
    private fun isBucket(stack: ItemStack): Boolean = stack.item is BucketItem || stack.`is`(Tags.Items.BUCKETS)

    private fun isFilledBucket(stack: ItemStack): Boolean = isBucket(stack) && !stack.`is`(Tags.Items.BUCKETS_EMPTY)

    override fun <V : Any> getRecipeTypes(focus: IFocus<V>): List<IRecipeType<*>> = when {
        focus.getIngredient(VanillaTypes.ITEM_STACK)?.takeIf(::isFilledBucket) != null ->
            when (focus.role) {
                RecipeIngredientRole.INPUT -> listOf(RagiumJeiRecipeTypes.CENTRIFUGING)
                RecipeIngredientRole.OUTPUT -> listOf(RagiumJeiRecipeTypes.FILLING)
                else -> listOf()
            }

        focus.getIngredient(NeoForgeTypes.FLUID_STACK)?.takeIf { !it.createBucket().isEmpty } != null ->
            when (focus.role) {
                RecipeIngredientRole.INPUT -> listOf(RagiumJeiRecipeTypes.FILLING)
                RecipeIngredientRole.OUTPUT -> listOf(RagiumJeiRecipeTypes.CENTRIFUGING)
                else -> listOf()
            }

        else -> listOf()
    }

    override fun <T : Any, V : Any> getRecipes(recipeType: IRecipeType<T>, focus: IFocus<V>): List<T> {
        // 液体入りバケツを選択中の場合
        val filledBucket: ItemStack? = focus.getIngredient(VanillaTypes.ITEM_STACK)?.takeIf(::isFilledBucket)
        if (filledBucket != null) {
            val filledBucketDisplay: SlotDisplay =
                filledBucket.toTemplateOrNull()?.let(SlotDisplay::ItemStackSlotDisplay) ?: SlotDisplay.Empty.INSTANCE
            val fluidIn: FluidStack = FluidUtil.getFirstStackContained(filledBucket)
            val remainder: ItemStackTemplate? = filledBucket.craftingRemainder
            if (!fluidIn.isEmpty && remainder != null) {
                return when (focus.role) {
                    // Filled Bucket -> Empty Bucket + Fluid
                    RecipeIngredientRole.INPUT if recipeType == RagiumJeiRecipeTypes.CENTRIFUGING ->
                        HTItemSeparatingViewerRecipe(
                            null,
                            filledBucketDisplay,
                            listOf(SlotDisplay.ItemStackSlotDisplay(remainder)),
                            FluidStackSlotDisplay(fluidIn),
                            RTBucketCentrifugingRecipe.progressData,
                        )

                    // Empty Bucket + Fluid -> Filled Bucket
                    RecipeIngredientRole.OUTPUT if recipeType == RagiumJeiRecipeTypes.FILLING ->
                        HTDoubleToSingleViewerRecipe(
                            null,
                            SlotDisplay.ItemStackSlotDisplay(remainder),
                            FluidStackSlotDisplay(fluidIn),
                            filledBucketDisplay,
                            RTBucketFillingRecipe.progressData,
                        )

                    else -> null
                }.let(::listOfNotNull).filterIsInstance(recipeType.recipeClass)
            }
        }
        // 液体を選択中の場合
        val fluid: FluidStack = focus.getIngredient(NeoForgeTypes.FLUID_STACK) ?: return listOf()
        val bucket: ItemStackTemplate = fluid.createBucket().toTemplateOrNull() ?: return listOf()
        val remainder: ItemStackTemplate = bucket.craftingRemainder ?: return listOf()
        return when (focus.role) {
            RecipeIngredientRole.INPUT if recipeType == RagiumJeiRecipeTypes.FILLING ->
                HTDoubleToSingleViewerRecipe(
                    null,
                    SlotDisplay.ItemStackSlotDisplay(remainder),
                    FluidStackSlotDisplay(fluid),
                    SlotDisplay.ItemStackSlotDisplay(bucket),
                    RTBucketFillingRecipe.progressData,
                )

            RecipeIngredientRole.OUTPUT if recipeType == RagiumJeiRecipeTypes.CENTRIFUGING ->
                HTItemSeparatingViewerRecipe(
                    null,
                    SlotDisplay.ItemStackSlotDisplay(bucket),
                    listOf(SlotDisplay.ItemStackSlotDisplay(remainder)),
                    FluidStackSlotDisplay(fluid),
                    RTBucketCentrifugingRecipe.progressData,
                )

            else -> null
        }.let(::listOfNotNull).filterIsInstance(recipeType.recipeClass)
    }

    override fun <T : Any> getRecipes(recipeType: IRecipeType<T>): List<T> = listOf()
}
