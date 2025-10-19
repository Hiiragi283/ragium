package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.HTChancedItemRecipe
import hiiragi283.ragium.api.recipe.manager.HTRecipeCache
import hiiragi283.ragium.api.recipe.manager.HTRecipeFinder
import hiiragi283.ragium.api.recipe.manager.createCache
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.fluid.HTFluidInteractable
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.storage.holder.HTSimpleFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTSimpleItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemStackSlot
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.common.variant.HTMachineVariant
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

abstract class HTChancedItemOutputBlockEntity<INPUT : RecipeInput, RECIPE : HTChancedItemRecipe<INPUT>>(
    variant: HTMachineVariant,
    pos: BlockPos,
    state: BlockState,
) : HTProcessorBlockEntity<INPUT, RECIPE>(variant, pos, state),
    HTFluidInteractable {
    protected lateinit var inputTank: HTFluidTank
        private set

    final override fun initializeFluidHandler(listener: HTContentListener): HTFluidTankHolder {
        // input
        inputTank = createTank(listener)
        return HTSimpleFluidTankHolder.input(this, inputTank)
    }

    protected abstract fun createTank(listener: HTContentListener): HTFluidTank

    protected lateinit var inputSlot: HTItemSlot.Mutable
        private set
    protected lateinit var outputSlots: List<HTItemSlot>
        private set

    final override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
        // input
        inputSlot = HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0))
        // outputs
        outputSlots = listOf(
            HTOutputItemStackSlot.create(listener, HTSlotHelper.getSlotPosX(5), HTSlotHelper.getSlotPosY(0.5)),
            HTOutputItemStackSlot.create(listener, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(0.5)),
            HTOutputItemStackSlot.create(listener, HTSlotHelper.getSlotPosX(5), HTSlotHelper.getSlotPosY(1.5)),
            HTOutputItemStackSlot.create(listener, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(1.5)),
        )
        return HTSimpleItemSlotHolder(this, listOf(inputSlot), outputSlots)
    }

    override fun openGui(player: Player, title: Component): InteractionResult =
        RagiumMenuTypes.CHANCED_ITEM_OUTPUT.openMenu(player, title, this, ::writeExtraContainerData)

    override fun canProgressRecipe(level: ServerLevel, input: INPUT, recipe: RECIPE): Boolean {
        // アウトプットに搬出できるか判定する
        for (stackIn: ItemStack in recipe.getPreviewItems(input, level.registryAccess())) {
            if (!HTStackSlotHelper
                    .insertStacks(
                        outputSlots,
                        stackIn.toImmutable(),
                        HTStorageAction.SIMULATE,
                    ).isEmpty()
            ) {
                return false
            }
        }
        return true
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: INPUT,
        recipe: RECIPE,
    ) {
        // 実際にアウトプットに搬出する
        for ((result: HTItemResult, chance: Float) in recipe.getResultItems(input)) {
            if (chance > level.random.nextFloat()) {
                val stackIn: ImmutableItemStack = result.getStackOrNull(level.registryAccess())?.toImmutable() ?: continue
                HTStackSlotHelper.insertStacks(outputSlots, stackIn, HTStorageAction.EXECUTE)
            }
        }
    }

    //    HTFluidInteractable    //

    final override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult =
        interactWith(player, hand, inputTank)

    //    Cached    //

    abstract class Cached<INPUT : RecipeInput, RECIPE : HTChancedItemRecipe<INPUT>>(
        private val cache: HTRecipeCache<INPUT, RECIPE>,
        variant: HTMachineVariant,
        pos: BlockPos,
        state: BlockState,
    ) : HTChancedItemOutputBlockEntity<INPUT, RECIPE>(variant, pos, state) {
        constructor(
            finder: HTRecipeFinder<INPUT, RECIPE>,
            variant: HTMachineVariant,
            pos: BlockPos,
            state: BlockState,
        ) : this(finder.createCache(), variant, pos, state)

        final override fun getMatchedRecipe(input: INPUT, level: ServerLevel): RECIPE? = cache.getFirstRecipe(input, level)
    }
}
