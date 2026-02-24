package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.resource.IdToValue
import hiiragi283.core.common.recipe.HTLookupRecipeCache
import hiiragi283.ragium.api.recipe.HTItemAndItemRecipe
import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.common.block.entity.machine.base.HTItemAndItemBlockEntity
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.mixin.SingleItemRecipeAccessor
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.StonecutterRecipe
import net.minecraft.world.level.block.state.BlockState

class HTStonecutterBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemAndItemBlockEntity(RagiumBlockEntityTypes.AUTO_CHISEL, pos, state) {
    override fun createRecipeComponent(): HTRecipeComponent<*, *> = RecipeComponent(RecipeLookup) {
        playSound(SoundEvents.UI_STONECUTTER_TAKE_RESULT)
    }

    private data object RecipeLookup : HTRecipeLookup.Fake<HTDoubleRecipeInput, WrappedRecipe> {
        override fun createCache(): HTRecipeCache<HTDoubleRecipeInput, WrappedRecipe> = HTLookupRecipeCache.forRecipe(this)

        override fun getAllRecipes(context: HTRecipeLookup.Context): Sequence<IdToValue<WrappedRecipe>> =
            context.getAllRecipes(RecipeType.STONECUTTING).map { holder: RecipeHolder<StonecutterRecipe> ->
                holder.id() to WrappedRecipe(holder.value() as SingleItemRecipeAccessor)
            }
    }

    private class WrappedRecipe(private val accessor: SingleItemRecipeAccessor) : HTItemAndItemRecipe {
        override val time: Int = 5

        override fun testFirstItem(stack: ItemStack): Boolean = accessor.ingredient.test(stack)

        override fun testSecondItem(stack: ItemStack): Boolean = ItemStack.isSameItemSameComponents(accessor.result, stack)

        override fun getRequiredAmount(input: HTDoubleRecipeInput): Pair<Int, Int> = 1 to 0

        override fun assemble(input: HTDoubleRecipeInput, registries: HolderLookup.Provider): ItemStack = accessor.result.copy()
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.machine.autoChisel
}
