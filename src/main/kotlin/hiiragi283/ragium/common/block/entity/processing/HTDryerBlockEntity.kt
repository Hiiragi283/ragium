package hiiragi283.ragium.common.block.entity.processing

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.fluid.getFluidStack
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.ragium.common.recipe.HTDryingRecipe
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.config.RagiumFluidConfigType
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTDryerBlockEntity(pos: BlockPos, state: BlockState) :
    HTAbstractComplexBlockEntity<HTDryingRecipe>(
        RagiumRecipeTypes.DRYING,
        RagiumBlockEntityTypes.DRYER,
        pos,
        state,
    ) {
    lateinit var inputTank: HTBasicFluidTank
        private set

    override fun initializeFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        inputTank =
            builder.addSlot(HTSlotInfo.INPUT, HTVariableFluidTank.input(listener, getTankCapacity(RagiumFluidConfigType.FIRST_INPUT)))
        super.initializeFluidTanks(builder, listener)
    }

    //    Processing    //

    override fun buildRecipeInput(builder: HTRecipeInput.Builder) {
        super.buildRecipeInput(builder)
        builder.fluids += inputTank.getFluidStack()
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTRecipeInput,
        recipe: HTDryingRecipe,
    ) {
        super.completeRecipe(level, pos, state, input, recipe)
        // インプットを減らす
        recipe.ingredient.map(
            { inputSlot.extract(it.getRequiredAmount(), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL) },
            { inputTank.extract(it.getRequiredAmount(), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL) },
        )
        // SEを鳴らす
        playSound(SoundEvents.SPONGE_ABSORB)
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.dryer
}
