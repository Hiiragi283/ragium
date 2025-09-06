package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.HTMultiItemToObjRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTCombineItemToItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiItemRecipeInput
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.common.storage.holder.HTSimpleItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
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

class HTAlloySmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Cached<HTMultiItemRecipeInput, HTCombineItemToItemRecipe>(
        RagiumRecipeTypes.ALLOYING.get(),
        HTMachineVariant.ALLOY_SMELTER,
        pos,
        state,
    ) {
    private lateinit var inputSlots: List<HTItemStackSlot>
    private lateinit var outputSlot: HTItemStackSlot

    override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
        // input
        inputSlots = listOf(
            HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(1), HTSlotHelper.getSlotPosY(0)),
            HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0)),
            HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(3), HTSlotHelper.getSlotPosY(0)),
        )
        // output
        outputSlot = HTItemStackSlot.output(listener, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(1))
        return HTSimpleItemSlotHolder(this, inputSlots, listOf(outputSlot))
    }

    override fun openGui(player: Player, title: Component): InteractionResult =
        RagiumMenuTypes.ALLOY_SMELTER.openMenu(player, title, this, ::writeExtraContainerData)

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTMultiItemRecipeInput = HTMultiItemRecipeInput.fromSlots(inputSlots)

    override fun canProgressRecipe(level: ServerLevel, input: HTMultiItemRecipeInput, recipe: HTCombineItemToItemRecipe): Boolean =
        outputSlot.insertItem(recipe.assemble(input, level.registryAccess()), true, HTStorageAccess.INTERNAl).isEmpty

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTMultiItemRecipeInput,
        recipe: HTCombineItemToItemRecipe,
    ) {
        // 実際にアウトプットに搬出する
        outputSlot.insertItem(recipe.assemble(input, level.registryAccess()), false, HTStorageAccess.INTERNAl)
        // 実際にインプットを減らす
        val ingredients: List<HTItemIngredient> = recipe.ingredients
        HTMultiItemToObjRecipe.getMatchingSlots(ingredients, input.items).forEachIndexed { index: Int, slot: Int ->
            inputSlots[slot].shrinkStack(ingredients[index], false)
        }
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5f, 0.5f)
    }
}
