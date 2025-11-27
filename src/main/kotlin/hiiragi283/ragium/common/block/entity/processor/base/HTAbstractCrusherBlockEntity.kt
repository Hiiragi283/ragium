package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.math.times
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.chance.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidStackTank
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import org.apache.commons.lang3.math.Fraction

abstract class HTAbstractCrusherBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTSingleItemInputBlockEntity.CachedWithTank<HTItemToChancedItemRecipe>(
        RagiumRecipeTypes.CRUSHING,
        blockHolder,
        pos,
        state,
    ) {
    final override fun createTank(listener: HTContentListener): HTVariableFluidStackTank = HTVariableFluidStackTank.input(
        listener,
        RagiumConfig.COMMON.crusherTankCapacity,
        canInsert = { hasUpgrade(RagiumItems.EFFICIENT_CRUSH_UPGRADE) },
        filter = RagiumFluidContents.LUBRICANT::isOf,
    )

    //    Ticking    //

    final override fun getRecipeTime(recipe: HTItemToChancedItemRecipe): Int {
        val bool1: Boolean = hasUpgrade(RagiumItems.EFFICIENT_CRUSH_UPGRADE)
        val bool2: Boolean = inputTank
            .extract(
                RagiumConst.LUBRICANT_CONSUME,
                HTStorageAction.SIMULATE,
                HTStorageAccess.INTERNAL,
            )?.amount() == RagiumConst.LUBRICANT_CONSUME
        val modifier: Fraction = when (bool1 && bool2) {
            true -> Fraction.THREE_QUARTERS
            false -> Fraction.ONE
        }
        return (modifier * super.getRecipeTime(recipe)).toInt()
    }

    final override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: HTItemToChancedItemRecipe,
    ) {
        // 実際にアウトプットに搬出する
        completeOutput(level, input, recipe)
        // インプットを減らす
        HTStackSlotHelper.shrinkStack(inputSlot, recipe::getRequiredCount, HTStorageAction.EXECUTE)
        // 潤滑油があれば減らす
        if (hasUpgrade(RagiumItems.EFFICIENT_CRUSH_UPGRADE)) {
            inputTank.extract(RagiumConst.LUBRICANT_CONSUME, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        }
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 0.25f, 1f)
    }

    protected abstract fun completeOutput(level: ServerLevel, input: SingleRecipeInput, recipe: HTItemToChancedItemRecipe)
}
