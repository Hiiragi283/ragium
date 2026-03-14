package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.builder.HTRecipeBuilder
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTSimpleFluidHolderLike
import hiiragi283.core.api.registry.HTSimpleItemHolderLike
import hiiragi283.core.api.util.wrapOptional
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTTankInteractingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid

class HTTankInteractingRecipeBuilder : HTRecipeBuilder(RagiumConst.TANK_INTERACTION) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTTankInteractingRecipeBuilder.() -> Unit) {
            HTTankInteractingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    lateinit var emptyContainer: HTSimpleItemHolderLike
    lateinit var filledContainer: HTSimpleItemHolderLike
    lateinit var fluid: HTSimpleFluidHolderLike
    var amount: Int = -1
    var fluidTag: TagKey<Fluid>? = null

    fun setFluid(content: HTFluidContent) {
        fluid = content
        fluidTag = content.fluidTag
    }

    override fun getPrimalId(): ResourceLocation = filledContainer.getId()

    override fun createRecipe(): HTTankInteractingRecipe = HTTankInteractingRecipe(
        emptyContainer,
        filledContainer,
        fluid,
        amount,
        fluidTag.wrapOptional(),
    )
}
