package hiiragi283.ragium.api.machine.entity

import hiiragi283.ragium.api.fluid.HTMachineFluidStorage
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipeProcessor
import net.minecraft.block.BlockState
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class HTProcessorMachineEntityBase(type: HTMachineConvertible, tier: HTMachineTier) : HTMachineEntity(type, tier) {
    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        parent.writeNbt(nbt, wrapperLookup)
        fluidStorage.writeNbt(nbt, wrapperLookup)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        parent.readNbt(nbt, wrapperLookup)
        fluidStorage.readNbt(nbt, wrapperLookup)
    }

    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        processor.process(world, pos, machineType, tier, createInput())
    }

    abstract val processor: HTMachineRecipeProcessor

    abstract fun createInput(): HTMachineInput

    abstract val fluidStorage: HTMachineFluidStorage

    protected fun createContext(): ScreenHandlerContext = parentBE.ifPresentWorld { world: World ->
        ScreenHandlerContext.create(world, parentBE.pos)
    } ?: ScreenHandlerContext.EMPTY
}
