package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.data.recipe.HTIngredientCreator
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.registry.HTSimpleHolderLikeDelegate
import hiiragi283.core.api.registry.getHolderDataMap
import hiiragi283.core.api.resource.IdToValue
import hiiragi283.core.common.recipe.HTLookupRecipeCache
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item

/**
 * @see hiiragi283.core.common.recipe.HTVanillaRecipeTypes
 */
object RagiumFakeRecipeTypes {
    @JvmField
    val DUPLICATING: HTRecipeType.Fake<HTItemAndFluidRecipeInput, RagiumDuplicatingRecipe> = DuplicatingType

    private data object DuplicatingType : HTRecipeType.Fake<HTItemAndFluidRecipeInput, RagiumDuplicatingRecipe> {
        override fun getId(): ResourceLocation = RagiumAPI.id(RagiumConst.DUPLICATING)

        override fun createCache(): HTRecipeCache<HTItemAndFluidRecipeInput, RagiumDuplicatingRecipe> = HTLookupRecipeCache.forRecipe(this)

        override fun getAllRecipes(context: HTRecipeLookup.Context): Sequence<IdToValue<RagiumDuplicatingRecipe>> = context.access
            .registryOrThrow(Registries.ITEM)
            .asLookup()
            .getHolderDataMap(RagiumDataMapTypes.DUPLICATION_COST)
            .asSequence()
            .map { (holder: HTSimpleHolderLikeDelegate<Item>, matterValue: Int) ->
                holder.getId() to RagiumDuplicatingRecipe(HTIngredientCreator.create(holder.get()), matterValue)
            }
    }
}
