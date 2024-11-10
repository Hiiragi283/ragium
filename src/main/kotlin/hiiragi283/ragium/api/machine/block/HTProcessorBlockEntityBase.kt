package hiiragi283.ragium.api.machine.block

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockController
import hiiragi283.ragium.api.machine.property.HTMachinePropertyKeys
import hiiragi283.ragium.api.recipe.HTMachineRecipeProcessor
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTStorageBuilder
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTStorageSide
import hiiragi283.ragium.common.advancement.HTBuiltMachineCriterion
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.screen.HTChemicalMachineScreenHandler
import hiiragi283.ragium.common.screen.HTLargeMachineScreenHandler
import hiiragi283.ragium.common.screen.HTSimpleMachineScreenHandler
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

abstract class HTProcessorBlockEntityBase(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(type, pos, state),
    HTFluidSyncable {
    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        if (tier.canProcess(world)) {
            if (processRecipe(world, pos)) {
                key.entry.ifPresent(HTMachinePropertyKeys.SOUND) {
                    world.playSound(null, pos, it, SoundCategory.BLOCKS)
                }
                tier.consumerEnergy(world)
            }
        }
    }

    abstract fun processRecipe(world: World, pos: BlockPos): Boolean

    protected abstract val inventory: SidedInventory

    final override fun asInventory(): SidedInventory = inventory

    protected abstract val fluidStorage: HTMachineFluidStorage

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        asInventory()
        fluidStorage.writeNbt(nbt, wrapperLookup)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        fluidStorage.readNbt(nbt, wrapperLookup)
    }

    final override fun interactWithFluidStorage(player: PlayerEntity): Boolean = fluidStorage.interactByPlayer(player)

    //    HTFluidSyncable    //

    final override fun sendPacket(player: ServerPlayerEntity, sender: (ServerPlayerEntity, Int, FluidVariant, Long) -> Unit) {
        fluidStorage.sendPacket(player, sender)
    }

    //    SidedStorageBlockEntity    //

    final override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidStorage.createWrapped()

    //    Simple    //

    class Simple(pos: BlockPos, state: BlockState) :
        HTProcessorBlockEntityBase(RagiumBlockEntityTypes.SIMPLE_PROCESSOR, pos, state) {
        override var key: HTMachineKey = RagiumMachineKeys.ALLOY_FURNACE

        constructor(pos: BlockPos, state: BlockState, key: HTMachineKey, tier: HTMachineTier) : this(pos, state) {
            this.key = key
            this.tier = tier
        }

        override val inventory: SidedInventory = HTStorageBuilder(5)
            .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
            .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
            .set(2, HTStorageIO.INTERNAL, HTStorageSide.NONE)
            .set(3, HTStorageIO.OUTPUT, HTStorageSide.ANY)
            .set(4, HTStorageIO.OUTPUT, HTStorageSide.ANY)
            .buildSided()

        override val fluidStorage: HTMachineFluidStorage = HTStorageBuilder(2)
            .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
            .set(1, HTStorageIO.OUTPUT, HTStorageSide.ANY)
            .buildMachineFluidStorage()

        private val processor = HTMachineRecipeProcessor(
            inventory,
            intArrayOf(0, 1),
            intArrayOf(3, 4),
            2,
            fluidStorage,
            intArrayOf(0),
            intArrayOf(1),
        )

        override fun processRecipe(world: World, pos: BlockPos): Boolean = processor.process(world, key, tier)

        override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
            HTSimpleMachineScreenHandler(syncId, playerInventory, packet, createContext())
    }

    //    Chemical    //

    class Chemical(pos: BlockPos, state: BlockState) :
        HTProcessorBlockEntityBase(RagiumBlockEntityTypes.CHEMICAL_PROCESSOR, pos, state) {
        override var key: HTMachineKey = RagiumMachineKeys.CHEMICAL_REACTOR

        constructor(pos: BlockPos, state: BlockState, key: HTMachineKey, tier: HTMachineTier) : this(pos, state) {
            this.key = key
            this.tier = tier
        }

        override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
            HTChemicalMachineScreenHandler(syncId, playerInventory, packet, createContext())

        override fun processRecipe(world: World, pos: BlockPos): Boolean = processor.process(world, key, tier)

        override val inventory: SidedInventory = HTStorageBuilder(5)
            .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
            .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
            .set(2, HTStorageIO.INTERNAL, HTStorageSide.NONE)
            .set(3, HTStorageIO.OUTPUT, HTStorageSide.ANY)
            .set(4, HTStorageIO.OUTPUT, HTStorageSide.ANY)
            .buildSided()

        override val fluidStorage: HTMachineFluidStorage = HTStorageBuilder(4)
            .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
            .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
            .set(2, HTStorageIO.OUTPUT, HTStorageSide.ANY)
            .set(3, HTStorageIO.OUTPUT, HTStorageSide.ANY)
            .buildMachineFluidStorage()

        val processor = HTMachineRecipeProcessor(
            inventory,
            intArrayOf(0, 1),
            intArrayOf(3, 4),
            2,
            fluidStorage,
            intArrayOf(0, 1),
            intArrayOf(2, 3),
        )
    }

    //    Large    //

    abstract class Large(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
        HTProcessorBlockEntityBase(type, pos, state),
        HTMultiblockController {
        final override var showPreview: Boolean = false

        override fun onSucceeded(
            state: BlockState,
            world: World,
            pos: BlockPos,
            player: PlayerEntity,
        ) {
            super.onSucceeded(state, world, pos, player)
            HTBuiltMachineCriterion.trigger(player, key, tier)
        }

        final override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
            HTLargeMachineScreenHandler(syncId, playerInventory, packet, createContext())

        final override fun processRecipe(world: World, pos: BlockPos): Boolean = processor.process(world, key, tier)

        final override val inventory: SidedInventory = HTStorageBuilder(7)
            .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
            .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
            .set(2, HTStorageIO.INPUT, HTStorageSide.ANY)
            .set(3, HTStorageIO.INTERNAL, HTStorageSide.NONE)
            .set(4, HTStorageIO.OUTPUT, HTStorageSide.ANY)
            .set(5, HTStorageIO.OUTPUT, HTStorageSide.ANY)
            .set(6, HTStorageIO.OUTPUT, HTStorageSide.ANY)
            .buildSided()

        final override val fluidStorage: HTMachineFluidStorage = HTStorageBuilder(4)
            .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
            .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
            .set(2, HTStorageIO.OUTPUT, HTStorageSide.ANY)
            .set(3, HTStorageIO.OUTPUT, HTStorageSide.ANY)
            .buildMachineFluidStorage()

        private val processor = HTMachineRecipeProcessor(
            inventory,
            intArrayOf(0, 1, 2),
            intArrayOf(4, 5, 6),
            3,
            fluidStorage,
            intArrayOf(0, 1),
            intArrayOf(2, 3),
        )
    }
}
