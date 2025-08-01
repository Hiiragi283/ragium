package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.energy.HTEnergyFilter
import hiiragi283.ragium.api.storage.energy.HTFilteredEnergyStorage
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.MenuProvider
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.items.ItemHandlerHelper
import kotlin.collections.forEach

abstract class HTMachineBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTTickAwareBlockEntity(type, pos, state),
    MenuProvider {
    //    Storage    //

    protected abstract val inventory: HTItemHandler

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        writer.write(RagiumConst.INVENTORY, inventory)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        reader.read(RagiumConst.INVENTORY, inventory)
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
        val network: IEnergyStorage = RagiumAPI.Companion
            .getInstance()
            .getEnergyNetworkManager()
            .getNetwork(level) ?: return
        this.network = network
        this.externalNetwork = wrapNetworkToExternal(network)
    }

    final override fun getEnergyStorage(direction: Direction?): IEnergyStorage? = externalNetwork

    protected open fun wrapNetworkToExternal(network: IEnergyStorage): IEnergyStorage =
        HTFilteredEnergyStorage(network, HTEnergyFilter.RECEIVE_ONLY)

    //    Menu    //

    final override fun getDisplayName(): Component = blockState.block.name
}
