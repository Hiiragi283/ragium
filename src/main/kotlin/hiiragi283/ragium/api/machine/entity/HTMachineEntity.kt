package hiiragi283.ragium.api.machine.entity

import hiiragi283.ragium.api.inventory.HTDelegatedInventory
import hiiragi283.ragium.api.machine.*
import hiiragi283.ragium.api.machine.entity.HTMachineEntity.Factory
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockController
import hiiragi283.ragium.api.util.HTDynamicPropertyDelegate
import hiiragi283.ragium.common.block.entity.HTMetaMachineBlockEntity
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
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
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

abstract class HTMachineEntity(val machineType: HTMachineType, val tier: HTMachineTier) :
    HTDelegatedInventory.Simple,
    ExtendedScreenHandlerFactory<HTMachinePacket>,
    PropertyDelegateHolder,
    SidedStorageBlockEntity {
    companion object {
        const val MAX_PROPERTIES = 4
    }

    constructor(type: HTMachineConvertible, tier: HTMachineTier) : this(type.asMachine(), tier)

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
        nbt.putString("machine_type", machineType.id.toString())
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

    override fun getPropertyDelegate(): PropertyDelegate = HTDynamicPropertyDelegate(MAX_PROPERTIES, ::getProperty, ::setProperty)

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
            fun of(factory: (HTMachineTier) -> HTMachineEntity): Factory =
                Factory { _: HTMachineType, tier: HTMachineTier -> factory(tier) }
        }

        fun create(machineType: HTMachineType, tier: HTMachineTier): HTMachineEntity
    }

    //    SidedStorageBlockEntity    //

    final override fun getItemStorage(side: Direction?): Storage<ItemVariant> = InventoryStorage.of(this, side)
}
