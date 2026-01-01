package hiiragi283.ragium.common.block.entity.processing

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.fluid.insert
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.util.HTStackSlotHelper
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.config.RagiumFluidConfigType
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTPyrolyzerBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Cached<HTPyrolyzingRecipe>(RagiumRecipeTypes.PYROLYZING, RagiumBlockEntityTypes.PYROLYZER, pos, state) {
    lateinit var outputTank: HTBasicFluidTank
        private set

    override fun initializeFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        outputTank =
            builder.addSlot(HTSlotInfo.OUTPUT, HTVariableFluidTank.output(listener, getTankCapacity(RagiumFluidConfigType.FIRST_OUTPUT)))
    }

    lateinit var inputSlot: HTBasicItemSlot
        private set
    lateinit var outputSlots: List<HTBasicItemSlot>
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
        outputSlots = List(4) { builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener)) }
    }

    //    Processing    //

    override fun buildRecipeInput(builder: HTRecipeInput.Builder) {
        builder.items += inputSlot.getItemStack()
    }

    override fun shouldCheckRecipe(level: ServerLevel, pos: BlockPos): Boolean =
        outputSlots.any { it.getNeeded() > 0 } || outputTank.getNeeded() > 0

    override fun getRecipeTime(recipe: HTPyrolyzingRecipe): Int = recipe.time

    override fun canProgressRecipe(level: ServerLevel, input: HTRecipeInput, recipe: HTPyrolyzingRecipe): Boolean {
        val access: RegistryAccess = level.registryAccess()
        val bool1: Boolean = outputTank.insert(recipe.getResultFluid(access), HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL).isEmpty

        val remainder: Int = HTStackSlotHelper.insertStacks(
            outputSlots,
            recipe.assemble(input, access),
            HTStorageAction.SIMULATE,
            HTStorageAccess.INTERNAL,
        )
        return bool1 && remainder == 0
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTRecipeInput,
        recipe: HTPyrolyzingRecipe,
    ) {
        val access: RegistryAccess = level.registryAccess()
        // 実際にアウトプットに搬出する
        outputTank.insert(recipe.getResultFluid(access), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        HTStackSlotHelper.insertStacks(outputSlots, recipe.assemble(input, access), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // インプットを減らす
        inputSlot.extract(recipe.ingredient.getRequiredAmount(), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // SEを鳴らす
        playSound(SoundEvents.BLAZE_AMBIENT)
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.pyrolyzer
}
