package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTDelegatedInventory
import hiiragi283.ragium.api.machine.HTMachineEntityNew.Factory
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockController
import hiiragi283.ragium.api.util.HTDynamicPropertyDelegate
import hiiragi283.ragium.common.block.entity.HTMetaMachineBlockEntity
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedSlottedStorage
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.PropertyDelegate
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

abstract class HTMachineEntityNew(val definition: HTMachineDefinition) :
    HTDelegatedInventory.Simple,
    ExtendedScreenHandlerFactory<HTMachinePacket>,
    PropertyDelegateHolder,
    SidedStorageBlockEntity {
    companion object {
        const val MAX_PROPERTIES = 4
    }

    constructor(type: HTMachineType, tier: HTMachineTier) : this(HTMachineDefinition(type, tier))

    constructor(type: HTMachineConvertible, tier: HTMachineTier) : this(HTMachineDefinition(type, tier))

    val type: HTMachineType = definition.type
    val tier: HTMachineTier = definition.tier

    lateinit var parentBE: HTMetaMachineBlockEntity
        private set

    val world: World?
        get() = parentBE.world
    val pos: BlockPos
        get() = parentBE.pos
    val packet: HTMachinePacket
        get() = HTMachinePacket(type, tier, parentBE.pos)

    fun setParentBE(parentBE: HTMetaMachineBlockEntity) {
        check(parentBE.machineEntity == this)
        this.parentBE = parentBE
    }

    protected open fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {}

    fun writeToNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        writeNbt(nbt, registryLookup)
        nbt.putString("machine_type", type.id.toString())
        nbt.putString("tier", tier.asString())
    }

    open fun readNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {}

    fun readFromNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        readNbt(nbt, registryLookup)
        RagiumAPI.log { info(nbt.toString()) }
    }

    open fun onWorldUpdated(world: World) {}

    open fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult = (this as? HTMultiblockController)
        ?.onUseController(state, world, pos, player, this)
        ?: when (world.isClient) {
            true -> ActionResult.SUCCESS
            else -> {
                player.openHandledScreen(state.createScreenHandlerFactory(world, pos))
                ActionResult.CONSUME
            }
        }

    open fun tickEach(
        world: World,
        pos: BlockPos,
        state: BlockState,
        ticks: Int,
    ) {
    }

    open fun tickSecond(world: World, pos: BlockPos, state: BlockState) {}

    //    ExtendedScreenHandlerFactory    //

    override fun getDisplayName(): Text = tier.createPrefixedText(type)

    override fun getScreenOpeningData(player: ServerPlayerEntity): HTMachinePacket = packet

    //    PropertyDelegateHolder    //

    var ticks: Int = 0

    open val tickRate: Int
        get() = tier.tickRate

    override fun getPropertyDelegate(): PropertyDelegate = HTDynamicPropertyDelegate(MAX_PROPERTIES, ::getProperty, ::setProperty)

    protected open fun getProperty(index: Int): Int = when (index) {
        0 -> ticks
        1 -> tickRate
        else -> -1
    }

    protected open fun setProperty(index: Int, value: Int) {}

    //    SidedStorageBlockEntity    //

    override fun getItemStorage(side: Direction?): Storage<ItemVariant> = InventoryStorage.of(parent, side)

    protected val fluidStorages: List<SingleFluidStorage> = listOf(
        // inputs
        object : SingleFluidStorage() {
            override fun getCapacity(variant: FluidVariant): Long = FluidConstants.BUCKET * tier.tankMultiplier

            override fun canExtract(variant: FluidVariant): Boolean = false
        },
        object : SingleFluidStorage() {
            override fun getCapacity(variant: FluidVariant): Long = FluidConstants.BUCKET * tier.tankMultiplier

            override fun canExtract(variant: FluidVariant): Boolean = false
        },
        // outputs
        object : SingleFluidStorage() {
            override fun getCapacity(variant: FluidVariant): Long = FluidConstants.BUCKET * tier.tankMultiplier

            override fun canInsert(variant: FluidVariant): Boolean = false
        },
        object : SingleFluidStorage() {
            override fun getCapacity(variant: FluidVariant): Long = FluidConstants.BUCKET * tier.tankMultiplier

            override fun canInsert(variant: FluidVariant): Boolean = false
        },
    )

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = CombinedSlottedStorage(fluidStorages)

    //    Factory    //

    fun interface Factory {
        companion object {
            @JvmStatic
            fun of(factory: (HTMachineTier) -> HTMachineEntityNew): Factory =
                Factory { _: HTMachineType, tier: HTMachineTier -> factory(tier) }
        }

        fun create(machineType: HTMachineType, tier: HTMachineTier): HTMachineEntityNew
    }
}
