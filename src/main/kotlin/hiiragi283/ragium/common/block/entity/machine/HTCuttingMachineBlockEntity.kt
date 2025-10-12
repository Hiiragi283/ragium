package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.extension.partially1
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.HTSingleInputRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.manager.HTRecipeCache
import hiiragi283.ragium.api.recipe.manager.mapRecipe
import hiiragi283.ragium.api.registry.impl.HTDeferredRecipeType
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.ImmutableItemStack
import hiiragi283.ragium.api.storage.item.getItemStack
import hiiragi283.ragium.api.storage.item.toRecipeInput
import hiiragi283.ragium.common.storage.holder.HTSimpleItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.common.variant.HTMachineVariant
import hiiragi283.ragium.impl.recipe.base.HTItemToItemRecipe
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class HTCuttingMachineBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<SingleRecipeInput, HTSingleInputRecipe>(
        HTMachineVariant.CUTTING_MACHINE,
        pos,
        state,
    ) {
    private lateinit var inputSlot: HTItemSlot.Mutable
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

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput = inputSlot.toRecipeInput()

    override fun getMatchedRecipe(input: SingleRecipeInput, level: ServerLevel): HTSingleInputRecipe? =
        recipeCache.getFirstRecipe(input, level)

    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: HTSingleInputRecipe): Boolean = HTStackSlotHelper
        .insertStacks(
            outputSlots,
            ImmutableItemStack.of(recipe.assemble(input, level.registryAccess())),
            HTStorageAction.SIMULATE,
        ).isEmpty()

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: HTSingleInputRecipe,
    ) {
        // 実際にアウトプットに搬出する
        HTStackSlotHelper.insertStacks(
            outputSlots,
            ImmutableItemStack.of(recipe.assemble(input, level.registryAccess())),
            HTStorageAction.EXECUTE,
        )
        // インプットを減らす
        inputSlot.shrinkStack(1, HTStorageAction.EXECUTE)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1f, 1f)
    }

    private inner class SingleItemCache : HTRecipeCache<SingleRecipeInput, HTSingleInputRecipe> {
        override fun getFirstHolder(input: SingleRecipeInput, level: Level): RecipeHolder<HTSingleInputRecipe>? =
            getFirstHolder(RagiumRecipeTypes.SAWMILL, input, level)
                ?: getFirstHolder(RagiumRecipeTypes.STONECUTTER, input, level)
                    ?.mapRecipe(HTItemToItemRecipe::wrapVanilla.partially1(level.registryAccess()))

        private fun <RECIPE : Recipe<SingleRecipeInput>> getFirstHolder(
            recipeType: HTDeferredRecipeType<SingleRecipeInput, RECIPE>,
            input: SingleRecipeInput,
            level: Level,
        ): RecipeHolder<RECIPE>? {
            // 指定されたアイテムと同じものを出力するレシピだけを選ぶ
            var matchedHolder: RecipeHolder<RECIPE>? = null
            for (holder: RecipeHolder<RECIPE> in recipeType.getAllHolders(level.recipeManager)) {
                val recipe: RECIPE = holder.value
                if (!recipe.matches(input, level)) continue
                val result: ItemStack = recipe.assemble(input, level.registryAccess())
                if (ItemStack.isSameItemSameComponents(catalystSlot.getItemStack(), result)) {
                    matchedHolder = holder
                    break
                }
            }
            return matchedHolder
        }
    }
}
