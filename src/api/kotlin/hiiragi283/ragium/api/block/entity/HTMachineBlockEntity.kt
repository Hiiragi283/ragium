package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.HTStorageIO
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage

/**
 * エンチャント可能な[HTTickAwareBlockEntity]
 */
abstract class HTMachineBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTTickAwareBlockEntity(type, pos, state) {
    //    Ticking    //

    override fun onServerTick(level: ServerLevel, pos: BlockPos, state: BlockState): TriState {
        val network: IEnergyStorage = this.network ?: return TriState.FALSE
        return onServerTick(level, pos, state, network)
    }

    /**
     * [IEnergyStorage]を引数に加えた[onServerTick]の拡張メソッド
     */
    protected abstract fun onServerTick(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
    ): TriState

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
        this.externalNetwork = HTStorageIO.INPUT.wrapEnergyStorage(network)
    }

    override fun getEnergyStorage(direction: Direction?): IEnergyStorage? = externalNetwork
}
