package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTHandlerBlockEntity
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.energy.HTEnergyFilter
import hiiragi283.ragium.api.storage.energy.HTFilteredEnergyStorage
import hiiragi283.ragium.api.storage.item.HTFilteredItemHandler
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.inventory.HTSlotConfigurationMenu
import hiiragi283.ragium.common.storage.HTTransferIOCache
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.items.ItemHandlerHelper
import kotlin.collections.forEach

abstract class HTMachineBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTTickAwareBlockEntity(type, pos, state),
    HTHandlerBlockEntity,
    MenuProvider {
    //    Storage    //

    protected abstract val inventory: HTItemHandler
    val transferIOCache = HTTransferIOCache()

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        writer.write(RagiumConst.INVENTORY, inventory)
        writer.write(RagiumConst.TRANSFER_IO, transferIOCache)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        reader.read(RagiumConst.INVENTORY, inventory)
        reader.read(RagiumConst.TRANSFER_IO, transferIOCache)
    }

    override fun onRightClickedWithWrench(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): ItemInteractionResult {
        if (!level.isClientSide) {
            player.openMenu(
                object : MenuProvider {
                    override fun getDisplayName(): Component = Component.literal("Slot Configuration")

                    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTSlotConfigurationMenu =
                        HTSlotConfigurationMenu(containerId, playerInventory, pos, createDefinition())
                },
                pos,
            )
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide)
    }

    final override fun dropInventory(consumer: (ItemStack) -> Unit) {
        super.dropInventory(consumer)
        inventory.getStackView().forEach(consumer)
    }

    final override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int =
        ItemHandlerHelper.calcRedstoneFromInventory(inventory)

    //    Ticking    //

    /**
     * このブロックエンティティがtick当たりで消費する電力の値
     */
    protected abstract val energyUsage: Int

    protected var requiredEnergy: Int = 0
    protected var usedEnergy: Int = 0

    protected fun doProgress(network: IEnergyStorage): Boolean {
        if (usedEnergy < requiredEnergy) {
            usedEnergy += handleEnergy(network)
        }
        if (usedEnergy < requiredEnergy) return false
        usedEnergy -= requiredEnergy
        return true
    }

    protected open fun handleEnergy(network: IEnergyStorage): Int = network.extractEnergy(energyUsage, false)

    final override fun serverTickPre(level: ServerLevel, pos: BlockPos, state: BlockState): TriState {
        val network: IEnergyStorage = this.network ?: return TriState.FALSE
        return serverTickPre(level, pos, state, network)
    }

    protected abstract fun serverTickPre(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
    ): TriState

    override val containerData: ContainerData = object : ContainerData {
        override fun get(index: Int): Int = when (index) {
            0 -> usedEnergy
            1 -> requiredEnergy
            else -> -1
        }

        override fun set(index: Int, value: Int) {
            when (index) {
                0 -> usedEnergy = value
                1 -> requiredEnergy = value
            }
        }

        override fun getCount(): Int = 2
    }

    //    HTHandlerBlockEntity    //

    protected var network: IEnergyStorage? = null
        private set
    private var externalNetwork: IEnergyStorage? = null

    override fun afterLevelInit(level: Level) {
        val network: IEnergyStorage = RagiumAPI
            .getInstance()
            .getEnergyNetworkManager()
            .getNetwork(level) ?: return
        this.network = network
        this.externalNetwork = wrapNetworkToExternal(network)
    }

    override fun getItemHandler(direction: Direction?): HTFilteredItemHandler = HTFilteredItemHandler(inventory, direction, transferIOCache)

    final override fun getEnergyStorage(direction: Direction?): IEnergyStorage? = externalNetwork

    protected open fun wrapNetworkToExternal(network: IEnergyStorage): IEnergyStorage =
        HTFilteredEnergyStorage(network, HTEnergyFilter.RECEIVE_ONLY)

    //    Menu    //

    final override fun getDisplayName(): Component = blockState.block.name

    //    Extension    //

    protected fun insertToOutput(output: ItemStack, simulate: Boolean): ItemStack =
        ItemHandlerHelper.insertItem(inventory.toFilteredReverse(), output, simulate)
}
