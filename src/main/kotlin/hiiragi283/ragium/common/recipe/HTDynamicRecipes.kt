package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.impl.HTItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemWithFluidToObjRecipeBuilder
import hiiragi283.ragium.api.extension.holdersNotEmpty
import hiiragi283.ragium.api.recipe.base.HTItemToFluidRecipe
import hiiragi283.ragium.api.recipe.base.HTItemWithFluidToItemRecipe
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil
import java.util.stream.Stream

object HTDynamicRecipes {
    @JvmStatic
    fun fluidHolderStream(): Stream<Holder.Reference<Fluid>> = BuiltInRegistries.FLUID
        .holdersNotEmpty()
        .filter { holder: Holder.Reference<Fluid> ->
            val fluid: Fluid = holder.value()
            (fluid as? FlowingFluid)?.isSource(fluid.defaultFluidState()) ?: false
        }
    
    @JvmStatic
    fun fluidStream(): Stream<Fluid> = fluidHolderStream().map(Holder.Reference<Fluid>::value)

    @JvmStatic
    fun bucketFilling(): Iterator<RecipeHolder<HTItemWithFluidToItemRecipe>> = fluidHolderStream()
        .map { holder: Holder.Reference<Fluid> ->
            val fluid: Fluid = holder.value()
            val id: ResourceLocation = holder.key().location().withPrefix("/infusing/bucket/")

            val recipe: HTItemWithFluidToItemRecipe = HTItemWithFluidToObjRecipeBuilder
                .infusing(
                    HTIngredientHelper.item(Tags.Items.BUCKETS_EMPTY),
                    HTIngredientHelper.fluid(fluid, 1000),
                    HTResultHelper.item(FluidUtil.getFilledBucket(FluidStack(fluid, 1000))),
                ).createRecipe()
            RecipeHolder(id, recipe)
        }.iterator()

    @JvmStatic
    fun bucketEmptying(): Iterator<RecipeHolder<HTItemToFluidRecipe>> = fluidHolderStream()
        .map { holder: Holder.Reference<Fluid> ->
            val fluid: Fluid = holder.value()
            val id: ResourceLocation = holder.key().location().withPrefix("/melting/bucket/")

            val recipe: HTItemToFluidRecipe = HTItemToObjRecipeBuilder
                .melting(
                    HTIngredientHelper.item(FluidUtil.getFilledBucket(FluidStack(fluid, 1000)).item),
                    HTResultHelper.fluid(fluid, 1000),
                ).createRecipe()
            RecipeHolder(id, recipe)
        }.iterator()
}
