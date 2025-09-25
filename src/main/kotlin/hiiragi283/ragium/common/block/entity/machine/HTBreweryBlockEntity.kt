package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.extension.unsupported
import hiiragi283.ragium.api.recipe.HTChancedItemRecipe
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRecipeHolder
import hiiragi283.ragium.api.recipe.HTSingleInputRecipe
import hiiragi283.ragium.api.recipe.base.HTItemToChancedItemRecipe
import hiiragi283.ragium.common.variant.HTMachineVariant
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistryAccess
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
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
    override fun openGui(player: Player, title: Component): InteractionResult {
        TODO("Not yet implemented")
    }

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput = SingleRecipeInput(inputSlot.getStack())

    //    Recipe Cache    //

    private object BrewingCache : HTRecipeCache<SingleRecipeInput, HTItemToChancedItemRecipe> {
        override fun getFirstHolder(input: SingleRecipeInput, level: Level): HTRecipeHolder<HTItemToChancedItemRecipe>? = when {
            BrewingRecipe.matches(input, level) -> HTRecipeHolder(RagiumAPI.id("brewing"), BrewingRecipe)
            else -> null
        }
    }

    //    Recipe    //

    private object BrewingRecipe : HTItemToChancedItemRecipe, HTSingleInputRecipe {
        override fun getResultItems(input: SingleRecipeInput): List<HTChancedItemRecipe.ChancedResult> {
            val access: RegistryAccess = RagiumAPI.INSTANCE.getRegistryAccess() ?: return listOf()
            // ポーションに変換する
            val stack: ItemStack = RagiumDataMaps.INSTANCE
                .getBrewingEffect(access, input.item().itemHolder)
                ?.toPotion()
                ?: return listOf()
            return HTResultHelper.INSTANCE
                .item(stack)
                .let { HTChancedItemRecipe.ChancedResult(it, 1f) }
                .let(::listOf)
        }

        override fun getIngredientCount(input: SingleRecipeInput): Int = 1

        override fun matches(input: SingleRecipeInput, level: Level): Boolean =
            RagiumDataMaps.INSTANCE.getBrewingEffect(level.registryAccess(), input.item().itemHolder) != null

        override fun test(input: SingleRecipeInput): Boolean = !input.isEmpty

        override fun isIncomplete(): Boolean = false

        override fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack =
            getResultItems(input).getOrNull(0)?.getStackOrNull(registries) ?: ItemStack.EMPTY

        override fun getSerializer(): RecipeSerializer<*> = unsupported()

        override fun getType(): RecipeType<*> = unsupported()
    }
}
