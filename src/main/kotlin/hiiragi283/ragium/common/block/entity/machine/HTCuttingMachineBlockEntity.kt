package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.extension.recipeAccess
import hiiragi283.ragium.api.extension.unsupported
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.HTSingleInputRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.manager.HTRecipeCache
import hiiragi283.ragium.api.recipe.manager.HTRecipeHolder
import hiiragi283.ragium.api.registry.impl.HTDeferredRecipeType
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.storage.holder.HTSimpleItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.variant.HTMachineVariant
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.crafting.StonecutterRecipe
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class HTCuttingMachineBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<SingleRecipeInput, HTSingleInputRecipe>(
        HTMachineVariant.CUTTING_MACHINE,
        pos,
        state,
    ) {
    private lateinit var inputSlot: HTItemSlot
    private lateinit var catalystSlot: HTItemSlot
    private lateinit var outputSlots: List<HTItemSlot>

    override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
        // input
        inputSlot = HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0))
        // catalyst
        catalystSlot = HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2))
        // outputs
        outputSlots = listOf(
            HTItemStackSlot.output(listener, HTSlotHelper.getSlotPosX(5), HTSlotHelper.getSlotPosY(0.5)),
            HTItemStackSlot.output(listener, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(0.5)),
            HTItemStackSlot.output(listener, HTSlotHelper.getSlotPosX(5), HTSlotHelper.getSlotPosY(1.5)),
            HTItemStackSlot.output(listener, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(1.5)),
        )
        return HTSimpleItemSlotHolder(this, listOf(inputSlot), outputSlots, catalystSlot)
    }

    override fun openGui(player: Player, title: Component): InteractionResult =
        RagiumMenuTypes.CUTTING_MACHINE.openMenu(player, title, this, ::writeExtraContainerData)

    //    Ticking    //

    private val recipeCache = SingleItemCache()

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput = SingleRecipeInput(inputSlot.getStack())

    override fun getMatchedRecipe(input: SingleRecipeInput, level: ServerLevel): HTSingleInputRecipe? =
        recipeCache.getFirstRecipe(input, level)

    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: HTSingleInputRecipe): Boolean {
        var remainder: ItemStack = recipe.assemble(input, level.registryAccess())
        for (slot: HTItemSlot in outputSlots) {
            remainder = slot.insertItem(remainder, true, HTStorageAccess.INTERNAl)
            if (remainder.isEmpty) break
        }
        return remainder.isEmpty
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: HTSingleInputRecipe,
    ) {
        // 実際にアウトプットに搬出する
        var remainder: ItemStack = recipe.assemble(input, level.registryAccess())
        for (slot: HTItemSlot in outputSlots) {
            remainder = slot.insertItem(remainder, false, HTStorageAccess.INTERNAl)
            if (remainder.isEmpty) break
        }
        // インプットを減らす
        inputSlot.shrinkStack(1, false)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1f, 1f)
    }

    private inner class SingleItemCache : HTRecipeCache<SingleRecipeInput, HTSingleInputRecipe> {
        override fun getFirstHolder(input: SingleRecipeInput, level: Level): HTRecipeHolder<HTSingleInputRecipe>? =
            getFirstHolder(RagiumRecipeTypes.SAWMILL, input, level)
                ?: getFirstHolder(RagiumRecipeTypes.STONECUTTER, input, level)
                    ?.mapRecipe(::StoneCuttingRecipe)

        private fun <RECIPE : Recipe<SingleRecipeInput>> getFirstHolder(
            recipeType: HTDeferredRecipeType<SingleRecipeInput, RECIPE>,
            input: SingleRecipeInput,
            level: Level,
        ): HTRecipeHolder<RECIPE>? {
            // 指定されたアイテムと同じものを出力するレシピだけを選ぶ
            var matchedHolder: HTRecipeHolder<RECIPE>? = null
            for (holder: HTRecipeHolder<RECIPE> in recipeType.getAllHolders(level.recipeAccess)) {
                val recipe: RECIPE = holder.recipe
                if (!recipe.matches(input, level)) continue
                val result: ItemStack = recipe.assemble(input, level.registryAccess())
                if (ItemStack.isSameItemSameComponents(catalystSlot.getStack(), result)) {
                    matchedHolder = holder
                    break
                }
            }
            return matchedHolder
        }
    }

    private class StoneCuttingRecipe(private val recipe: StonecutterRecipe) : HTSingleInputRecipe {
        private val ingredient: Ingredient get() = recipe.ingredients[0]

        override fun getRequiredCount(stack: ItemStack): Int = when {
            ingredient.test(stack) -> 1
            else -> 0
        }

        override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())

        override fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack = recipe.assemble(input, registries)

        override fun getSerializer(): RecipeSerializer<*> = unsupported()

        override fun getType(): RecipeType<*> = unsupported()

        override fun isIncomplete(): Boolean = ingredient.items.isEmpty()
    }
}
