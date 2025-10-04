package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.input.HTMultiItemRecipeInput
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.storage.holder.HTSimpleItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.util.HTIngredientHelper
import hiiragi283.ragium.common.variant.HTMachineVariant
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.state.BlockState

class HTSimulatorBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Cached<HTMultiItemRecipeInput, HTItemWithCatalystToItemRecipe>(
        RagiumRecipeTypes.SIMULATING.get(),
        HTMachineVariant.SIMULATOR,
        pos,
        state,
    ) {
    private lateinit var inputSlot: HTItemSlot.Mutable
    private lateinit var catalystSlot: HTItemSlot
    private lateinit var outputSlot: HTItemSlot

    override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
        // input
        inputSlot = HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0))
        // catalyst
        catalystSlot = HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2))
        // output
        outputSlot = HTItemStackSlot.output(listener, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(1))
        return HTSimpleItemSlotHolder(this, listOf(inputSlot), listOf(outputSlot), catalystSlot)
    }

    override fun openGui(player: Player, title: Component): InteractionResult =
        RagiumMenuTypes.SIMULATOR.openMenu(player, name, this, ::writeExtraContainerData)

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTMultiItemRecipeInput =
        HTMultiItemRecipeInput.fromSlots(inputSlot, catalystSlot)

    override fun canProgressRecipe(level: ServerLevel, input: HTMultiItemRecipeInput, recipe: HTItemWithCatalystToItemRecipe): Boolean =
        outputSlot.insertItem(recipe.assemble(input, level.registryAccess()), true, HTStorageAccess.INTERNAl).isEmpty

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTMultiItemRecipeInput,
        recipe: HTItemWithCatalystToItemRecipe,
    ) {
        // 実際にアウトプットに搬出する
        outputSlot.insertItem(recipe.assemble(input, level.registryAccess()), false, HTStorageAccess.INTERNAl)
        // 実際にインプットを減らす
        HTIngredientHelper.shrinkStack(inputSlot, recipe.ingredient, false)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 0.5f, 1f)
    }
}
