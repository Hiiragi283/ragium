package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.HTMultiItemToObjRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTCombineItemToItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiItemRecipeInput
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.common.storage.holder.HTSimpleItemSlotHolder
import hiiragi283.ragium.common.storage.item.HTItemStackSlot
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.util.variant.HTMachineVariant
import net.minecraft.client.resources.sounds.SoundInstance
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.state.BlockState

class HTAlloySmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<HTMultiItemRecipeInput, HTCombineItemToItemRecipe>(
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
            HTItemStackSlot.atManualOut(listener, HTSlotHelper.getSlotPosX(1), HTSlotHelper.getSlotPosY(0)),
            HTItemStackSlot.atManualOut(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0)),
            HTItemStackSlot.atManualOut(listener, HTSlotHelper.getSlotPosX(3), HTSlotHelper.getSlotPosY(0)),
        )
        // output
        outputSlot = HTItemStackSlot.at(listener, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(1))
        return HTSimpleItemSlotHolder(this, inputSlots, listOf(outputSlot))
    }

    override fun openGui(player: Player, title: Component): InteractionResult =
        RagiumMenuTypes.ALLOY_SMELTER.openMenu(player, title, this, ::writeExtraContainerData)

    override fun createSound(random: RandomSource, pos: BlockPos): SoundInstance =
        createSound(SoundEvents.BLASTFURNACE_FIRE_CRACKLE, random, pos)

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTMultiItemRecipeInput = HTMultiItemRecipeInput.fromSlots(inputSlots)

    override fun canProgressRecipe(level: ServerLevel, input: HTMultiItemRecipeInput, recipe: HTCombineItemToItemRecipe): Boolean =
        insertToOutput(recipe.assemble(input, level.registryAccess()), true).isEmpty

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTMultiItemRecipeInput,
        recipe: HTCombineItemToItemRecipe,
    ) {
        // 実際にアウトプットに搬出する
        insertToOutput(recipe.assemble(input, level.registryAccess()), false)
        // 実際にインプットを減らす
        val ingredients: List<HTItemIngredient> = recipe.ingredients
        HTMultiItemToObjRecipe.getMatchingSlots(ingredients, input.items).forEachIndexed { index: Int, slot: Int ->
            inputSlots[slot].shrinkStack(ingredients[index], false)
        }
    }
}
