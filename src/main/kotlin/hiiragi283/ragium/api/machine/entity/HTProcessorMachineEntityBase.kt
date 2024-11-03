package hiiragi283.ragium.api.machine.entity

import hiiragi283.ragium.api.fluid.HTMachineFluidStorage
import hiiragi283.ragium.api.fluid.HTSingleFluidStorage
import hiiragi283.ragium.api.inventory.HTDelegatedInventory
import hiiragi283.ragium.api.inventory.HTSidedInventory
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachinePacket
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipeProcessor
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorageUtil
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtOps
import net.minecraft.registry.RegistryWrapper
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

abstract class HTProcessorMachineEntityBase(typeSize: HTMachineType.Size, type: HTMachineConvertible, tier: HTMachineTier) :
    HTMachineEntity<HTMachineType.Processor>(type.asProcessor(), tier),
    ExtendedScreenHandlerFactory<HTMachinePacket>,
    HTDelegatedInventory.Sided,
    HTFluidSyncable {
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
            .parse(NbtOps.INSTANCE, nbt.getCompound("fluid_storages"))
            .result()
            .ifPresent { fluidStorage = HTMachineFluidStorage.fromParts(tier, it) }
    }

    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        processor.process(world, pos, machineType, tier, createInput())
    }

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean =
        fluidStorage.parts.any { FluidStorageUtil.interactWithFluidStorage(it, player, Hand.MAIN_HAND) }

    abstract fun createInput(): HTMachineInput

    //    SidedStorageBlockEntity    //

    final override val parent: HTSidedInventory = typeSize.createInventory()

    var fluidStorage: HTMachineFluidStorage = HTMachineFluidStorage.create(tier, typeSize)
        protected set(value) {
            field = value
            processor.fluidStorage = value
        }

    val processor: HTMachineRecipeProcessor = HTMachineRecipeProcessor.of(parent, typeSize).apply {
        this.fluidStorage = this@HTProcessorMachineEntityBase.fluidStorage
    }

    final override fun getItemStorage(side: Direction?): Storage<ItemVariant> = parent.wrapStorage(side)

    final override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidStorage.createWrapped()

    //    HTFluidSyncable    //

    override fun sendPacket(player: ServerPlayerEntity, sender: (ServerPlayerEntity, Int, FluidVariant, Long) -> Unit) {
        fluidStorage.parts.forEachIndexed { index: Int, storage: HTSingleFluidStorage ->
            sender(player, index, storage.variant, storage.amount)
        }
    }

    //    ExtendedScreenHandlerFactory    //

    override fun getDisplayName(): Text = tier.createPrefixedText(machineType)

    override fun getScreenOpeningData(player: ServerPlayerEntity): HTMachinePacket = packet
}
