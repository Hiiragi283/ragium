package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTDelegatedInventory
import hiiragi283.ragium.api.inventory.HTSimpleInventory
import hiiragi283.ragium.api.machine.HTMachineEntity.Factory
import hiiragi283.ragium.common.block.entity.HTMetaMachineBlockEntity
import hiiragi283.ragium.common.block.entity.HTMultiblockController
import hiiragi283.ragium.common.util.HTDynamicPropertyDelegate
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.PropertyDelegate
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class HTMachineEntity(val machineType: HTMachineType, val tier: HTMachineTier) :
    HTDelegatedInventory.Simple,
    NamedScreenHandlerFactory,
    PropertyDelegateHolder {
    lateinit var parentBE: HTMetaMachineBlockEntity
        private set

    fun setParentBE(parentBE: HTMetaMachineBlockEntity) {
        check(parentBE.machineEntity == this)
        this.parentBE = parentBE
    }

    protected open fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {}

    fun writeToNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        writeNbt(nbt, registryLookup)
        nbt.putString("machine_type", machineType.id.toString())
        nbt.putString("tier", tier.asString())
    }

    open fun readNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {}

    fun readFromNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        readNbt(nbt, registryLookup)
        RagiumAPI.log { info(nbt.toString()) }
    }

    open fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult = (this as? HTMultiblockController)
        ?.onUseController(state, world, pos, player)
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

    //    NamedScreenHandlerFactory    //

    override fun getDisplayName(): Text = tier.createPrefixedText(machineType)

    //    PropertyDelegateHolder    //

    var ticks: Int = 0

    open val tickRate: Int
        get() = tier.tickRate

    override fun getPropertyDelegate(): PropertyDelegate = HTDynamicPropertyDelegate(2, ::getProperty)

    open fun getProperty(index: Int): Int = when (index) {
        0 -> ticks
        1 -> tickRate
        else -> throw IndexOutOfBoundsException(index)
    }

    //    Factory    //

    fun interface Factory {
        companion object {
            @JvmStatic
            fun of(factory: (HTMachineTier) -> HTMachineEntity): Factory =
                Factory { _: HTMachineType, tier: HTMachineTier -> factory(tier) }
        }

        fun create(machineType: HTMachineType, tier: HTMachineTier): HTMachineEntity
    }

    //    Default    //

    class Default(machineType: HTMachineType, tier: HTMachineTier) : HTMachineEntity(machineType, tier) {
        override val parent: HTSimpleInventory = HTSimpleInventory(0)

        override fun createMenu(syncId: Int, playerInventory: PlayerInventory?, player: PlayerEntity?): ScreenHandler? = null
    }
}
