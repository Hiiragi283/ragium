package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.recipe.HTMultiRecipeCache
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
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTSmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTSingleItemInputBlockEntity<AbstractCookingRecipe>(
        HTMultiRecipeCache(RecipeType.SMELTING, RecipeType.SMOKING, RecipeType.BLASTING),
        HTMachineVariant.SMELTER,
        pos,
        state,
    ) {
    private lateinit var outputSlot: HTItemSlot

    override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
        // input
        inputSlot = HTItemStackSlot.atManualOut(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0))
        // output
        outputSlot = HTItemStackSlot.at(listener, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(1))
        return HTSimpleItemSlotHolder(this, listOf(inputSlot), listOf(outputSlot))
    }

    override fun openGui(player: Player, title: Component): InteractionResult =
        RagiumMenuTypes.SMELTER.openMenu(player, title, this, ::writeExtraContainerData)

    override fun createSound(random: RandomSource, pos: BlockPos): SoundInstance =
        createSound(SoundEvents.FURNACE_FIRE_CRACKLE, random, pos, 0.5f)

    //    Ticking    //

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: AbstractCookingRecipe,
    ) {
        // 実際にアウトプットに搬出する
        insertToOutput(recipe.assemble(input, level.registryAccess()), false)
        // インプットを減らす
        inputSlot.shrinkStack(1, false)
    }
}
