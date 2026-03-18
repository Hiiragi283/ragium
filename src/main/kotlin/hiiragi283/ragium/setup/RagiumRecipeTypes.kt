package hiiragi283.ragium.setup

import hiiragi283.core.api.data.recipe.HTIngredientCreator
import hiiragi283.core.api.recipe.HTItemToChancedRecipe
import hiiragi283.core.api.recipe.HTItemToItemRecipe
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.input.HTShapelessRecipeInput
import hiiragi283.core.api.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.core.api.registry.HTSimpleHolderLike
import hiiragi283.core.api.registry.getDataSequence
import hiiragi283.core.api.resource.IdToValue
import hiiragi283.core.common.recipe.HTLookupRecipeCache
import hiiragi283.core.common.registry.HTDeferredRecipeType
import hiiragi283.core.common.registry.register.HTDeferredRecipeTypeRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.api.recipe.HTEnchantingRecipe
import hiiragi283.ragium.api.recipe.HTItemOrFluidRecipe
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTAssemblingRecipe
import hiiragi283.ragium.common.recipe.HTElectrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import hiiragi283.ragium.common.recipe.RagiumDuplicatingRecipe
import hiiragi283.ragium.common.recipe.input.HTChemicalRecipeInput
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.SingleRecipeInput

object RagiumRecipeTypes {
    @JvmField
    val REGISTER = HTDeferredRecipeTypeRegister(RagiumAPI.MOD_ID)

    // Machine - Basic
    @JvmField
    val ALLOYING: HTDeferredRecipeType<HTShapelessRecipeInput, HTAlloyingRecipe> =
        REGISTER.registerType(RagiumConst.ALLOYING)

    @JvmField
    val ASSEMBLING: HTDeferredRecipeType<HTShapelessRecipeInput, HTAssemblingRecipe> =
        REGISTER.registerType(RagiumConst.ASSEMBLING)

    @JvmField
    val COMPRESSING: HTDeferredRecipeType<SingleRecipeInput, HTItemToItemRecipe.Serializable> =
        REGISTER.registerType(RagiumConst.COMPRESSING)

    @JvmField
    val CUTTING: HTDeferredRecipeType<SingleRecipeInput, HTItemToChancedRecipe.Serializable> =
        REGISTER.registerType(RagiumConst.CUTTING)

    @JvmField
    val PLANTING: HTDeferredRecipeType<SingleRecipeInput, HTItemToChancedRecipe.Serializable> =
        REGISTER.registerType(RagiumConst.PLANTING)

    // Machine - Advanced
    @JvmField
    val FREEZING: HTDeferredRecipeType<HTItemAndFluidRecipeInput, HTFreezingRecipe> =
        REGISTER.registerType(RagiumConst.FREEZING)

    @JvmField
    val MELTING: HTDeferredRecipeType<SingleRecipeInput, HTMeltingRecipe> = REGISTER.registerType(RagiumConst.MELTING)

    @JvmField
    val PYROLYZING: HTDeferredRecipeType<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe.Serializable> =
        REGISTER.registerType(RagiumConst.PYROLYZING)

    @JvmField
    val REFINING: HTDeferredRecipeType<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe.Serializable> =
        REGISTER.registerType(RagiumConst.REFINING)

    // Machine - Elite
    @JvmField
    val ELECTROLYZING: HTDeferredRecipeType<HTSingleFluidRecipeInput, HTElectrolyzingRecipe> =
        REGISTER.registerType(RagiumConst.ELECTROLYZING)

    @JvmField
    val MIXING: HTDeferredRecipeType<HTChemicalRecipeInput, HTMixingRecipe> =
        REGISTER.registerType(RagiumConst.MIXING)

    @JvmField
    val WASHING: HTDeferredRecipeType<HTItemAndFluidRecipeInput, HTWashingRecipe> =
        REGISTER.registerType(RagiumConst.WASHING)

    // Machine - Ultimate
    @JvmField
    val DUPLICATING: HTRecipeType.Fake<HTItemAndFluidRecipeInput, RagiumDuplicatingRecipe> =
        object : HTRecipeType.Fake<HTItemAndFluidRecipeInput, RagiumDuplicatingRecipe> {
            override fun getId(): ResourceLocation = RagiumAPI.id(RagiumConst.DUPLICATING)

            override fun createCache(): HTRecipeCache<HTItemAndFluidRecipeInput, RagiumDuplicatingRecipe> =
                HTLookupRecipeCache.forRecipe(this)

            override fun getAllRecipes(context: HTRecipeLookup.Context): Sequence<IdToValue<RagiumDuplicatingRecipe>> = context.access
                .lookupOrThrow(Registries.ITEM)
                .getDataSequence(RagiumDataMapTypes.DUPLICATION_COST)
                .map { (holder: HTSimpleHolderLike<Item>, matterValue: Int) ->
                    holder.getId() to RagiumDuplicatingRecipe(HTIngredientCreator.create(holder.get()), matterValue)
                }
        }

    @JvmField
    val ENCHANTING: HTDeferredRecipeType<HTEnchantingRecipe.Input, HTEnchantingRecipe> =
        REGISTER.registerType(RagiumConst.ENCHANTING)
}
