package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
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
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTCrusherBlockEntity(pos: BlockPos, state: BlockState) :
    HTSingleItemInputBlockEntity<HTItemToChancedItemRecipe>(
        RagiumRecipeTypes.CRUSHING.get(),
        HTMachineVariant.CRUSHER,
        pos,
        state,
    ) {
    private lateinit var outputSlots: List<HTItemSlot>

    override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
        // input
        inputSlot = HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0))
        // outputs
        outputSlots = listOf(
            HTItemStackSlot.output(listener, HTSlotHelper.getSlotPosX(5), HTSlotHelper.getSlotPosY(0.5)),
            HTItemStackSlot.output(listener, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(0.5)),
            HTItemStackSlot.output(listener, HTSlotHelper.getSlotPosX(5), HTSlotHelper.getSlotPosY(1.5)),
            HTItemStackSlot.output(listener, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(1.5)),
        )
        return HTSimpleItemSlotHolder(this, listOf(inputSlot), outputSlots)
    }

    override fun openGui(player: Player, title: Component): InteractionResult =
        RagiumMenuTypes.CRUSHER.openMenu(player, title, this, ::writeExtraContainerData)

    //    Ticking    //

    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: HTItemToChancedItemRecipe): Boolean {
        // アウトプットに搬出できるか判定する
        for (stackIn: ItemStack in recipe.getPreviewItems(input, level.registryAccess())) {
            var remainder: ItemStack = stackIn
            for (slot: HTItemSlot in outputSlots) {
                remainder = slot.insertItem(stackIn, true, HTStorageAccess.INTERNAl)
                if (remainder.isEmpty) break
            }
            if (!remainder.isEmpty) return false
        }
        return true
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: HTItemToChancedItemRecipe,
    ) {
        // 実際にアウトプットに搬出する
        for ((result: HTItemResult, chance: Float) in recipe.getResultItems(input)) {
            if (chance > level.random.nextFloat()) {
                var remainder: ItemStack = result.getStackOrNull(level.registryAccess()) ?: continue
                for (slot: HTItemSlot in outputSlots) {
                    remainder = slot.insertItem(remainder, false, HTStorageAccess.INTERNAl)
                    if (remainder.isEmpty) break
                }
            }
        }
        // インプットを減らす
        inputSlot.shrinkStack(recipe.getIngredientCount(input), false)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 1f, 0.25f)
    }
}
