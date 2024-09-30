package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.common.block.entity.HTBlockEntityBase
import hiiragi283.ragium.common.block.entity.HTTieredMachine
import hiiragi283.ragium.common.inventory.*
import hiiragi283.ragium.common.machine.HTMachineConvertible
import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.machine.HTMachineTypeRegistry
import hiiragi283.ragium.common.screen.HTMachineScreenHandler
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.PropertyDelegate
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

abstract class HTMachineBlockEntityBase :
    HTBlockEntityBase,
    HTDelegatedInventory,
    HTTieredMachine,
    NamedScreenHandlerFactory,
    PropertyDelegateHolder {
    override var machineType: HTMachineType = HTMachineType.Default
        protected set
    override var tier: HTMachineTier = HTMachineTier.PRIMITIVE
        protected set

    @Deprecated("")
    constructor(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : super(type, pos, state)

    constructor(
        type: BlockEntityType<*>,
        pos: BlockPos,
        state: BlockState,
        machineType: HTMachineConvertible,
        tier: HTMachineTier,
    ) : this(
        type,
        pos,
        state,
    ) {
        updateProperties(machineType.asMachine(), tier)
    }

    private fun updateProperties(machineType: HTMachineType, tier: HTMachineTier) {
        validateMachineType(machineType)
        this.machineType = machineType
        this.tier = tier
    }

    abstract fun validateMachineType(machineType: HTMachineType)

    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, registryLookup)
        nbt.putString("machine_type", machineType.id.toString())
        nbt.putString("tier", tier.asString())
    }

    override fun readNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, registryLookup)
        val machineType: HTMachineType =
            HTMachineTypeRegistry.get(Identifier.of(nbt.getString("machine_type"))) ?: return
        val tier: HTMachineTier =
            HTMachineTier.entries.firstOrNull { it.asString() == nbt.getString("tier") } ?: return
        updateProperties(machineType, tier)
    }

    //    HTBlockEntityBase    //

    override val tickRate: Int
        get() = tier.tickRate

    //    HTDelegatedInventory    //

    final override val parent: HTSidedInventory = HTSidedStorageBuilder(7)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(2, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(3, HTStorageIO.INTERNAL, HTStorageSide.NONE)
        .set(4, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .set(5, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .set(6, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildSided()

    override fun markDirty() {
        super<HTBlockEntityBase>.markDirty()
    }

    //    NamedScreenHandlerFactory    //

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? =
        HTMachineScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, pos))

    override fun getDisplayName(): Text = tier.createPrefixedText(machineType)

    //    PropertyDelegateHolder    //

    override fun getPropertyDelegate(): PropertyDelegate = object : PropertyDelegate {
        override fun get(index: Int): Int = when (index) {
            0 -> ticks
            1 -> tickRate
            else -> throw IndexOutOfBoundsException(index)
        }

        override fun set(index: Int, value: Int) {
        }

        override fun size(): Int = 2
    }
}
