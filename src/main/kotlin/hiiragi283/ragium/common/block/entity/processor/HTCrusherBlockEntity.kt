package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.math.times
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.chance.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.item.toRecipeInput
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.processor.base.HTFluidToChancedItemOutputBlockEntity
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidStackTank
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTCrusherBlockEntity(pos: BlockPos, state: BlockState) :
    HTFluidToChancedItemOutputBlockEntity.Cached<SingleRecipeInput, HTItemToChancedItemRecipe>(
        RagiumRecipeTypes.CRUSHING,
        RagiumBlocks.CRUSHER,
        pos,
        state,
    ) {
    override fun createTank(listener: HTContentListener): HTVariableFluidStackTank = HTVariableFluidStackTank.input(
        listener,
        RagiumConfig.COMMON.crusherTankCapacity,
        canInsert = { HTProcessorHelper.hasLubricantUpgrade(this) },
        filter = RagiumFluidContents.LUBRICANT::isOf,
    )

    //    Ticking    //

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput? = inputSlot.toRecipeInput()

    override fun getRecipeTime(recipe: HTItemToChancedItemRecipe): Int =
        (HTProcessorHelper.getLubricantModifier(this, inputTank) * super.getRecipeTime(recipe)).toInt()

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: HTItemToChancedItemRecipe,
    ) {
        super.completeRecipe(level, pos, state, input, recipe)
        // インプットを減らす
        HTStackSlotHelper.shrinkStack(inputSlot, recipe::getRequiredCount, HTStorageAction.EXECUTE)
        // 潤滑油があれば減らす
        HTProcessorHelper.consumeLubricant(this, inputTank)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 1f, 0.25f)
    }
}
