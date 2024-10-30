package hiiragi283.ragium.api.machine.entity

import hiiragi283.ragium.api.fluid.HTMachineFluidStorage
import hiiragi283.ragium.api.fluid.HTSingleFluidStorage
import hiiragi283.ragium.api.inventory.HTSimpleInventory
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipeProcessor
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.minecraft.block.BlockState
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtOps
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

abstract class HTProcessorMachineEntityBase(typeSize: HTMachineType.Size, type: HTMachineConvertible, tier: HTMachineTier) :
    HTMachineEntity<HTMachineType.Processor>(type.asProcessor(), tier) {
    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        parent.writeNbt(nbt, wrapperLookup)
        HTSingleFluidStorage.CODEC
            .listOf()
            .encodeStart(NbtOps.INSTANCE, fluidStorage.parts)
            .result()
            .ifPresent { nbt.put("fluid_storages", it) }
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        parent.readNbt(nbt, wrapperLookup)
        HTSingleFluidStorage.CODEC
            .listOf()
            .parse(NbtOps.INSTANCE, nbt.getCompound("fluid_storage"))
            .result()
            .ifPresent { fluidStorage = HTMachineFluidStorage.fromParts(it) }
    }

    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        processor.process(world, pos, machineType, tier, createInput())
    }

    abstract fun createInput(): HTMachineInput

    final override val parent: HTSimpleInventory = typeSize.createInventory()

    var fluidStorage: HTMachineFluidStorage = HTMachineFluidStorage.create(typeSize)
        protected set

    val processor: HTMachineRecipeProcessor = HTMachineRecipeProcessor.of(parent, fluidStorage)

    final override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidStorage.createWrapped()
}
