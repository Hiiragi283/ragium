package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.recipe.HTSingleInputRecipe
import hiiragi283.ragium.api.recipe.base.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.manager.HTRecipeCache
import hiiragi283.ragium.api.recipe.result.HTChancedItemResult
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.toRecipeInput
import hiiragi283.ragium.common.storage.fluid.HTVariableFluidStackTank
import hiiragi283.ragium.common.variant.HTMachineVariant
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistryAccess
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class HTBreweryBlockEntity(pos: BlockPos, state: BlockState) :
    HTChancedItemOutputBlockEntity<SingleRecipeInput, HTItemToChancedItemRecipe>(
        BrewingCache,
        HTMachineVariant.BREWERY,
        pos,
        state,
    ) {
    override fun createTank(listener: HTContentListener): HTFluidTank =
        HTVariableFluidStackTank.input(listener, RagiumConfig.COMMON.breweryTankCapacity, canInsert = HTFluidContent.WATER::isOf)

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput = inputSlot.toRecipeInput()

    //    Recipe Cache    //

    private object BrewingCache : HTRecipeCache<SingleRecipeInput, HTItemToChancedItemRecipe> {
        override fun getFirstHolder(input: SingleRecipeInput, level: Level): RecipeHolder<HTItemToChancedItemRecipe>? = when {
            BrewingRecipe.matches(input, level) -> RecipeHolder(RagiumAPI.id("brewing"), BrewingRecipe)
            else -> null
        }
    }

    //    Recipe    //

    private object BrewingRecipe : HTItemToChancedItemRecipe, HTSingleInputRecipe {
        override fun getResultItems(input: SingleRecipeInput): List<HTChancedItemResult> {
            val access: RegistryAccess = RagiumPlatform.INSTANCE.getRegistryAccess() ?: return listOf()
            // ポーションに変換する
            val stack: ItemStack = RagiumDataMaps.INSTANCE
                .getBrewingEffect(access, input.item().itemHolder)
                ?.toPotion()
                ?: return listOf()
            return HTResultHelper.INSTANCE
                .item(stack)
                .let(::HTChancedItemResult)
                .let(::listOf)
        }

        override fun getRequiredCount(stack: ItemStack): Int = 1

        override fun test(input: SingleRecipeInput): Boolean {
            val access: RegistryAccess = RagiumPlatform.INSTANCE.getRegistryAccess() ?: return false
            return RagiumDataMaps.INSTANCE.getBrewingEffect(access, input.item().itemHolder) != null
        }

        override fun isIncomplete(): Boolean = false

        override fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack =
            getResultItems(input).getOrNull(0)?.getStackOrNull(registries) ?: ItemStack.EMPTY

        override fun getSerializer(): RecipeSerializer<*> = throw UnsupportedOperationException()

        override fun getType(): RecipeType<*> = throw UnsupportedOperationException()
    }
}
