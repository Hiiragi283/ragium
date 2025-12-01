package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.recipe.chance.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.processor.base.HTAbstractCrusherBlockEntity
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTPulverizerBlockEntity(pos: BlockPos, state: BlockState) : HTAbstractCrusherBlockEntity(RagiumBlocks.PULVERIZER, pos, state) {
    lateinit var outputSlot: HTBasicItemSlot
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlot = singleInput(builder, listener)
        // output
        outputSlot = singleOutput(builder, listener)
    }

    override fun shouldCheckRecipe(level: ServerLevel, pos: BlockPos): Boolean = outputSlot.getNeeded() > 0

    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: HTItemToChancedItemRecipe): Boolean = outputSlot
        .insert(recipe.assembleItem(input, level.registryAccess()), HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL) == null

    override fun completeOutput(level: ServerLevel, input: SingleRecipeInput, recipe: HTItemToChancedItemRecipe) {
        outputSlot.insert(recipe.assembleItem(input, level.registryAccess()), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
    }
}
