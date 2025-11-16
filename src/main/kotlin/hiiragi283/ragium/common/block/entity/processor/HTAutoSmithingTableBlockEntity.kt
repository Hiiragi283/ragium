package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.storage.item.getItemStack
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.recipe.manager.VanillaRecipeCache
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SmithingRecipe
import net.minecraft.world.item.crafting.SmithingRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTAutoSmithingTableBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Cached<SmithingRecipeInput, SmithingRecipe>(
        VanillaRecipeCache(RecipeType.SMITHING),
        RagiumBlocks.AUTO_SMITHING_TABLE,
        pos,
        state,
    ) {
    lateinit var inputSlots: List<HTItemStackSlot>
        private set
    lateinit var outputSlot: HTItemStackSlot
        private set

    override fun initializeItemHandler(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlots = (1..3).map { i: Int ->
            builder.addSlot(
                HTSlotInfo.INPUT,
                HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(i), HTSlotHelper.getSlotPosY(1), 1),
            )
        }
        // output
        outputSlot = singleOutput(builder, listener)
    }

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SmithingRecipeInput = SmithingRecipeInput(
        inputSlots[0].getItemStack(),
        inputSlots[1].getItemStack(),
        inputSlots[2].getItemStack(),
    )

    override fun canProgressRecipe(level: ServerLevel, input: SmithingRecipeInput, recipe: SmithingRecipe): Boolean = outputSlot.insert(
        recipe.assemble(input, level.registryAccess()).toImmutable(),
        HTStorageAction.SIMULATE,
        HTStorageAccess.INTERNAL,
    ) == null

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SmithingRecipeInput,
        recipe: SmithingRecipe,
    ) {
        // 実際にアウトプットに搬出する
        outputSlot.insert(recipe.assemble(input, level.registryAccess()).toImmutable(), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // 実際にインプットを減らす
        for (slot: HTItemStackSlot in inputSlots) {
            slot.extract(1, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        }
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.SMITHING_TABLE_USE, SoundSource.BLOCKS, 0.5f, 0.5f)
    }
}
