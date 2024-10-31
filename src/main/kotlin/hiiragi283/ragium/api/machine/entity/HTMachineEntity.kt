package hiiragi283.ragium.api.machine.entity

import hiiragi283.ragium.api.inventory.HTDelegatedInventory
import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.machine.HTMachinePacket
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.entity.HTMachineEntity.Factory
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockController
import hiiragi283.ragium.api.util.HTDynamicPropertyDelegate
import hiiragi283.ragium.common.block.entity.HTMetaMachineBlockEntity
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorageUtil
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.PropertyDelegate
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

abstract class HTMachineEntity<T : HTMachineType>(val machineType: T, val tier: HTMachineTier) :
    HTDelegatedInventory.Simple,
    ExtendedScreenHandlerFactory<HTMachinePacket>,
    SidedStorageBlockEntity {
    companion object {
        const val MAX_PROPERTIES = 4
    }

    val definition = HTMachineDefinition(machineType, tier)
    lateinit var parentBE: HTMetaMachineBlockEntity
        private set
    val world: World?
        get() = parentBE.world
    val pos: BlockPos
        get() = parentBE.pos
    val packet: HTMachinePacket
        get() = HTMachinePacket(machineType, tier, parentBE.pos)

    fun setParentBE(parentBE: HTMetaMachineBlockEntity) {
        check(parentBE.machineEntity == this)
        this.parentBE = parentBE
    }

    protected open fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {}

    fun writeToNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        writeNbt(nbt, wrapperLookup)
        nbt.putString("machine_type", machineType.key.id.toString())
        nbt.putString("tier", tier.asString())
    }

    open fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {}

    fun readFromNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        readNbt(nbt, wrapperLookup)
        // RagiumAPI.log { info(nbt.toString()) }
    }

    open fun onWorldUpdated(world: World) {}

    open fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        // Validate multiblock
        if (this is HTMultiblockController) {
            return onUseController(state, world, pos, player, this)
        }
        // Insert fluid from holding stack
        FluidStorage.SIDED.find(world, pos, null)?.let { storage: Storage<FluidVariant> ->
            if (FluidStorageUtil.interactWithFluidStorage(storage, player, Hand.MAIN_HAND)) {
                return ActionResult.success(world.isClient)
            }
        }
        // open machine screen
        return when (world.isClient) {
            true -> ActionResult.SUCCESS
            else -> {
                player.openHandledScreen(state.createScreenHandlerFactory(world, pos))
                ActionResult.CONSUME
            }
        }
    }

    open fun tickEach(
        world: World,
        pos: BlockPos,
        state: BlockState,
        ticks: Int,
    ) {}

    open fun tickSecond(world: World, pos: BlockPos, state: BlockState) {}

    //    ExtendedScreenHandlerFactory    //

    override fun getDisplayName(): Text = tier.createPrefixedText(machineType)

    override fun getScreenOpeningData(player: ServerPlayerEntity): HTMachinePacket = packet

    protected fun createContext(): ScreenHandlerContext = parentBE.ifPresentWorld { world: World ->
        ScreenHandlerContext.create(world, parentBE.pos)
    } ?: ScreenHandlerContext.EMPTY

    //    PropertyDelegateHolder    //

    var ticks: Int = 0

    open val tickRate: Int
        get() = tier.tickRate

    val property: PropertyDelegate = HTDynamicPropertyDelegate(MAX_PROPERTIES, ::getProperty, ::setProperty)

    protected open fun getProperty(index: Int): Int = when (index) {
        0 -> ticks
        1 -> tickRate
        else -> -1
    }

    protected open fun setProperty(index: Int, value: Int) {}

    //    Factory    //

    fun interface Factory {
        companion object {
            @JvmStatic
            fun ofStatic(factory: (HTMachineTier) -> HTMachineEntity<*>?): Factory =
                Factory { _: HTMachineType, tier: HTMachineTier -> factory(tier) }

            @JvmStatic
            fun ofGenerator(factory: (HTMachineType.Generator, HTMachineTier) -> HTMachineEntity<*>?): Factory =
                Factory { machineType: HTMachineType, tier: HTMachineTier ->
                    machineType.asGeneratorOrNull()?.let { factory(it, tier) }
                }

            @JvmStatic
            fun ofProcessor(factory: (HTMachineType.Processor, HTMachineTier) -> HTMachineEntity<*>?): Factory =
                Factory { machineType: HTMachineType, tier: HTMachineTier ->
                    machineType.asProcessorOrNull()?.let { factory(it, tier) }
                }
        }

        fun create(machineType: HTMachineType, tier: HTMachineTier): HTMachineEntity<*>?
    }

    //    SidedStorageBlockEntity    //

    final override fun getItemStorage(side: Direction?): Storage<ItemVariant> = InventoryStorage.of(this, side)
}
