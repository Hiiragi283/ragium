package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.block.attribute.getFluidAttribute
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.generator.base.HTProgressGeneratorBlockEntity
import hiiragi283.ragium.common.storage.fluid.tank.HTFluidStackTank
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack

class HTCombustionGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTProgressGeneratorBlockEntity<HTMultiRecipeInput, HTCombustionGeneratorBlockEntity.CombustionRecipe>(
        RagiumBlocks.COMBUSTION_GENERATOR,
        pos,
        state,
    ) {
    lateinit var coolantTank: HTFluidStackTank
        private set
    lateinit var fuelTank: HTFluidStackTank
        private set

    override fun initializeFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        // inputs
        coolantTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidStackTank.input(
                listener,
                blockHolder.getFluidAttribute().getFirstInputTank(),
                canInsert = { stack: ImmutableFluidStack -> stack.getData(RagiumDataMaps.COOLANT) != null }
            ),
        )
        fuelTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidStackTank.input(
                listener, 
                blockHolder.getFluidAttribute().getSecondInputTank(),
                canInsert = { stack: ImmutableFluidStack -> stack.getData(RagiumDataMaps.COMBUSTION_FUEL) != null }
            ),
        )
    }

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTMultiRecipeInput? = HTMultiRecipeInput.create {
        fluids += coolantTank.getStack()
        fluids += fuelTank.getStack()
    }

    override fun getMatchedRecipe(input: HTMultiRecipeInput, level: ServerLevel): CombustionRecipe? {
        val access: RegistryAccess = level.registryAccess()
        // Coolant
        val coolantStack: FluidStack = input.getFluid(0)
        val coolant: Int = RagiumDataMaps.INSTANCE.getCoolantAmount(access, coolantStack.fluidHolder)
        if (coolantStack.amount < coolant) return null
        // Fuel
        val fuelStack: FluidStack = input.getFluid(1)
        if (fuelStack.amount < 100) return null
        val energy: Int = RagiumDataMaps.INSTANCE.getEnergyFromCombustion(access, fuelStack.fluidHolder)
        if (energy <= 0) return null
        return CombustionRecipe(coolant, energy)
    }

    override fun getEnergyToGenerate(recipe: CombustionRecipe): Int = recipe.energy

    override fun onGenerationUpdated(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTMultiRecipeInput,
        recipe: CombustionRecipe,
    ) {
        // インプットを減らす
        HTStackSlotHelper.shrinkStack(coolantTank, { recipe.coolant }, HTStorageAction.EXECUTE)
        HTStackSlotHelper.shrinkStack(fuelTank, { 100 }, HTStorageAction.EXECUTE)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1f, 0.5f)
    }

    @JvmRecord
    data class CombustionRecipe(val coolant: Int, val energy: Int)
}
