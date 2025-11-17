package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.single.HTSingleInputRecipe
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.processor.base.HTSingleItemInputBlockEntity
import hiiragi283.ragium.common.storage.fluid.tank.HTFluidStackTank
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
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
import net.neoforged.neoforge.fluids.FluidType

class HTBreweryBlockEntity(pos: BlockPos, state: BlockState) :
    HTSingleItemInputBlockEntity.Cached<HTSingleInputRecipe>(
        RagiumRecipeTypes.BREWING,
        RagiumBlocks.BREWERY,
        pos,
        state,
    ) {
    lateinit var inputTank: HTFluidStackTank
        private set

    override fun initializeFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        // input
        inputTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidStackTank.input(
                listener,
                RagiumConfig.COMMON.breweryTankCapacity,
                canInsert = RagiumFluidContents.AWKWARD_WATER::isOf,
            ),
        )
    }

    lateinit var outputSlot: HTItemStackSlot
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlot = singleInput(builder, listener)
        // output
        outputSlot = singleOutput(builder, listener)
    }

    override fun shouldCheckRecipe(level: ServerLevel, pos: BlockPos): Boolean = outputSlot.getNeeded() > 0

    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: HTSingleInputRecipe): Boolean {
        val amount: Int = FluidType.BUCKET_VOLUME
        val bool1: Boolean = inputTank.extract(amount, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)?.amount() == amount
        val bool2: Boolean =
            outputSlot.insert(recipe.assembleItem(input, level.registryAccess()), HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL) ==
                null
        return bool1 && bool2
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: HTSingleInputRecipe,
    ) {
        // 実際にアウトプットに搬出する
        outputSlot.insert(recipe.assembleItem(input, level.registryAccess()), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // 実際にインプットを減らす
        HTStackSlotHelper.shrinkStack(inputSlot, recipe::getRequiredCount, HTStorageAction.EXECUTE)
        inputTank.extract(FluidType.BUCKET_VOLUME, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1f, 1f)
    }
}
