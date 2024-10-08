package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.event.HTMachineEvents
import hiiragi283.ragium.api.inventory.HTDelegatedInventory
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.util.HTDynamicPropertyDelegate
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.PropertyDelegate
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

abstract class HTMachineBlockEntityBase :
    HTBlockEntityBase,
    HTDelegatedInventory.Simple,
    NamedScreenHandlerFactory,
    PropertyDelegateHolder {
    var machineType: HTMachineType = HTMachineType.DEFAULT
        protected set
    var tier: HTMachineTier = HTMachineTier.PRIMITIVE
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
        HTMachineEvents.UPDATE_PROPERTIES.invoker().onUpdate(machineType, tier, this)
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
            RagiumAPI.getInstance().machineTypeRegistry.get(Identifier.of(nbt.getString("machine_type"))) ?: return
        val tier: HTMachineTier =
            HTMachineTier.entries.firstOrNull { it.asString() == nbt.getString("tier") } ?: return
        updateProperties(machineType, tier)
    }

    override fun markDirty() {
        super<HTBlockEntityBase>.markDirty()
    }

    //    HTBlockEntityBase    //

    override val tickRate: Int
        get() = tier.tickRate

    //    NamedScreenHandlerFactory    //

    override fun getDisplayName(): Text = tier.createPrefixedText(machineType)

    //    PropertyDelegateHolder    //

    override fun getPropertyDelegate(): PropertyDelegate = HTDynamicPropertyDelegate(2, ::getProperty)

    open fun getProperty(index: Int): Int = when (index) {
        0 -> ticks
        1 -> tickRate
        else -> throw IndexOutOfBoundsException(index)
    }
}
