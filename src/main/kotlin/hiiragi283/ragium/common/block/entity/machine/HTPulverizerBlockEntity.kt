package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
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
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTPulverizerBlockEntity(pos: BlockPos, state: BlockState) :
    HTSingleItemInputBlockEntity<HTItemToChancedItemRecipe>(
        RagiumRecipeTypes.CRUSHING.get(),
        HTMachineVariant.PULVERIZER,
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
        RagiumMenuTypes.PULVERIZER.openMenu(player, title, this, ::writeExtraContainerData)

    override fun createSound(random: RandomSource, pos: BlockPos): SoundInstance = createSound(SoundEvents.GRINDSTONE_USE, random, pos)

    //    Ticking    //

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: HTItemToChancedItemRecipe,
    ) {
        // 実際にアウトプットに搬出する
        insertToOutput(recipe.assemble(input, level.registryAccess()), false)
        // インプットを減らす
        inputSlot.shrinkStack(recipe.getIngredientCount(input), false)
    }
}
