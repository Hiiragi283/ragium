package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTSimpleInventory
import hiiragi283.ragium.api.machine.HTMachineEntity
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumComponentTypes
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder
import net.minecraft.block.BlockState
import net.minecraft.component.ComponentMap
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.PropertyDelegate
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTMetaMachineBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntityBase(RagiumBlockEntityTypes.META_MACHINE, pos, state),
    NamedScreenHandlerFactory,
    PropertyDelegateHolder {
    var machineEntity: HTMachineEntity = HTMachineEntity.Default(HTMachineType.DEFAULT, HTMachineTier.PRIMITIVE)
        private set
    val machineType: HTMachineType
        get() = machineEntity.machineType
    val tier: HTMachineTier
        get() = machineEntity.tier

    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, registryLookup)
        machineEntity.writeToNbt(nbt, registryLookup)
    }

    override fun readNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, registryLookup)
        val machineType: HTMachineType = RagiumAPI
            .getInstance()
            .machineTypeRegistry
            .get(Identifier.of(nbt.getString("machine_type")))
            ?: HTMachineType.DEFAULT
        val tier: HTMachineTier = HTMachineTier.entries
            .firstOrNull { it.asString() == nbt.getString("tier") }
            ?: HTMachineTier.PRIMITIVE
        initMachineEntity(machineType, tier)
        machineEntity.readFromNbt(nbt, registryLookup)
    }

    override fun readComponents(components: ComponentsAccess) {
        super.readComponents(components)
        val machineType: HTMachineType =
            components.getOrDefault(RagiumComponentTypes.MACHINE_TYPE, HTMachineType.DEFAULT)
        val tier: HTMachineTier = components.getOrDefault(RagiumComponentTypes.MACHINE_TIER, HTMachineTier.PRIMITIVE)
        initMachineEntity(machineType, tier)
    }

    override fun addComponents(builder: ComponentMap.Builder) {
        super.addComponents(builder)
        builder.add(RagiumComponentTypes.MACHINE_TYPE, machineType)
        builder.add(RagiumComponentTypes.MACHINE_TIER, tier)
    }

    private fun initMachineEntity(machineType: HTMachineType, tier: HTMachineTier) {
        this.machineEntity =
            machineType.getOrDefault(HTMachinePropertyKeys.MACHINE_FACTORY).create(machineType, tier)
        this.machineEntity.setParentBE(this)
    }

    //    HTBlockEntityBase    //

    override fun asInventory(): HTSimpleInventory = machineEntity.parent

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult = machineEntity.onUse(state, world, pos, player, hit)

    override var ticks: Int
        get() = machineEntity.ticks
        set(value) {
            machineEntity.ticks = value
        }

    override val tickRate: Int
        get() = machineEntity.tickRate

    override fun tickEach(
        world: World,
        pos: BlockPos,
        state: BlockState,
        ticks: Int,
    ) {
        machineEntity.tickEach(world, pos, state, ticks)
    }

    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        machineEntity.tickSecond(world, pos, state)
    }

    //    NamedScreenHandlerFactory    //

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory?, player: PlayerEntity?): ScreenHandler? =
        machineEntity.createMenu(syncId, playerInventory, player)

    override fun getDisplayName(): Text = machineEntity.displayName

    //    PropertyDelegateHolder    //

    override fun getPropertyDelegate(): PropertyDelegate = machineEntity.propertyDelegate
}
