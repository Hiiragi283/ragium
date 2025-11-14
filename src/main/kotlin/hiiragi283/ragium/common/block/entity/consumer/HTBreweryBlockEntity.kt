package hiiragi283.ragium.common.block.entity.consumer

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.registry.HTBrewingEffect
import hiiragi283.ragium.api.recipe.HTSingleInputRecipe
import hiiragi283.ragium.api.recipe.base.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.result.HTChancedItemResult
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.item.toRecipeInput
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidStackTank
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistryAccess
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTBreweryBlockEntity(pos: BlockPos, state: BlockState) :
    HTFluidToChancedItemOutputBlockEntity<SingleRecipeInput, HTItemToChancedItemRecipe>(
        RagiumBlocks.BREWERY,
        pos,
        state,
    ) {
    override fun createTank(listener: HTContentListener): HTVariableFluidStackTank =
        HTVariableFluidStackTank.input(listener, RagiumConfig.COMMON.breweryTankCapacity, canInsert = HTFluidContent.WATER::isOf)

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput = inputSlot.toRecipeInput()

    override fun getMatchedRecipe(input: SingleRecipeInput, level: ServerLevel): HTItemToChancedItemRecipe? = when {
        BrewingRecipe.matches(input, level) -> BrewingRecipe
        else -> null
    }

    //    Recipe    //

    private object BrewingRecipe : HTItemToChancedItemRecipe, HTSingleInputRecipe {
        override fun getResultItems(input: SingleRecipeInput): List<HTChancedItemResult> {
            // ポーションに変換する
            val stack: ItemStack = findFirstPotion(input.item())
            if (stack.isEmpty) return listOf()
            return HTResultHelper
                .item(stack)
                .let(::HTChancedItemResult)
                .let(::listOf)
        }

        override fun getRequiredCount(stack: ImmutableItemStack): Int = 1

        override fun test(input: SingleRecipeInput): Boolean = !findFirstPotion(input.item()).isEmpty

        private fun findFirstPotion(stack: ItemStack): ItemStack {
            val access: RegistryAccess = RagiumPlatform.INSTANCE.getRegistryAccess() ?: return ItemStack.EMPTY

            return HTBrewingEffect.getPotion(access.lookupOrThrow(RagiumAPI.BREWING_EFFECT_KEY), stack)
        }

        override fun isIncomplete(): Boolean = false

        override fun assembleItem(input: SingleRecipeInput, registries: HolderLookup.Provider): ImmutableItemStack? =
            getResultItems(input).getOrNull(0)?.getStackOrNull(registries)

        override fun getSerializer(): RecipeSerializer<*> = throw UnsupportedOperationException()

        override fun getType(): RecipeType<*> = throw UnsupportedOperationException()
    }
}
