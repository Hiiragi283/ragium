package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.extension.getMachineKey
import hiiragi283.ragium.api.inventory.HTDelegatedInventory
import hiiragi283.ragium.api.inventory.HTSimpleInventory
import hiiragi283.ragium.api.machine.*
import hiiragi283.ragium.api.machine.entity.HTMachineEntity
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.component.ComponentMap
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

@Deprecated("May be removed")
class HTMetaMachineBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntityBase(RagiumBlockEntityTypes.META_MACHINE, pos, state),
    ExtendedScreenHandlerFactory<HTMachinePacket>,
    SidedStorageBlockEntity {
    var machineEntity: HTMachineEntity<*>? = null
        private set

    lateinit var key: HTMachineKey
    lateinit var tier: HTMachineTier

    val definition: HTMachineDefinition?
        get() = machineEntity?.definition
    val machineType: HTMachineType?
        get() = machineEntity?.machineType

    override fun setWorld(world: World) {
        super.setWorld(world)
        machineEntity?.onWorldUpdated(world)
    }

    override fun markDirty() {
        super.markDirty()
        asInventory()?.markDirty()
    }

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        machineEntity?.writeToNbt(nbt, wrapperLookup)
        super.writeNbt(nbt, wrapperLookup)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        key = nbt.getMachineKey("machine_type")
        tier = HTMachineTier.entries
            .firstOrNull { it.asString() == nbt.getString("tier") }
            ?: HTMachineTier.PRIMITIVE

        machineEntity?.readFromNbt(nbt, wrapperLookup)
        super.readNbt(nbt, wrapperLookup)
    }

    override fun readComponents(components: ComponentsAccess) {
        super.readComponents(components)
        val machineType: HTMachineType = components.get(HTMachineType.COMPONENT_TYPE) ?: return
        val tier: HTMachineTier = components.getOrDefault(HTMachineTier.COMPONENT_TYPE, HTMachineTier.PRIMITIVE)
        initMachineEntity(machineType, tier)
    }

    override fun addComponents(builder: ComponentMap.Builder) {
        super.addComponents(builder)
        machineEntity?.let {
            builder.add(HTMachineType.COMPONENT_TYPE, it.machineType)
            builder.add(HTMachineTier.COMPONENT_TYPE, it.tier)
        }
    }

    private fun initMachineEntity(machineType: HTMachineType, tier: HTMachineTier) {
        // this.machineEntity = machineType.asProperties()[HTMachinePropertyKeys.MACHINE_FACTORY]?.create(machineType, tier)
        this.machineEntity?.setParentBE(this)
    }

    //    HTBlockEntityBase    //

    override fun asInventory(): HTSimpleInventory? = (machineEntity as? HTDelegatedInventory<*>)?.parent

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult = machineEntity?.onUse(state, world, pos, player, hit) ?: ActionResult.PASS

    override var ticks: Int
        get() = machineEntity?.ticks ?: 0
        set(value) {
            machineEntity?.ticks = value
        }

    override val tickRate: Int
        get() = machineEntity?.tickRate ?: 200

    override fun tickEach(
        world: World,
        pos: BlockPos,
        state: BlockState,
        ticks: Int,
    ) {
        machineEntity?.tickEach(world, pos, state, ticks)
    }

    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        machineEntity?.tickSecond(world, pos, state)
    }

    //    NamedScreenHandlerFactory    //

    private fun asScreenFactory(): NamedScreenHandlerFactory? = machineEntity as? NamedScreenHandlerFactory

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? =
        asScreenFactory()?.createMenu(syncId, playerInventory, player)

    override fun getDisplayName(): Text = asScreenFactory()?.displayName ?: Text.literal("Deprecated :)")

    override fun getScreenOpeningData(player: ServerPlayerEntity): HTMachinePacket = checkNotNull(machineEntity?.packet)

    //    SidedStorageBlockEntity    //

    override fun getItemStorage(side: Direction?): Storage<ItemVariant>? = machineEntity?.getItemStorage(side)

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant>? = machineEntity?.getFluidStorage(side)
}
