package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.util.RagiumConstantValues
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.MenuProvider
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage

/**
 * エンチャント可能な[HTTickAwareBlockEntity]
 */
abstract class HTMachineBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTTickAwareBlockEntity(type, pos, state),
    MenuProvider {
    //    Storage    //

    protected abstract val inventory: HTItemHandler

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        writer.write(RagiumConstantValues.INVENTORY, inventory)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        reader.read(RagiumConstantValues.INVENTORY, inventory)
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        super.onRemove(state, level, pos, newState, movedByPiston)
        inventory.dropStacksAt(level, pos)
    }

    //    Ticking    //

    override val maxTicks: Int = 200

    /**
     * このブロックエンティティがtick当たりで消費する電力の値
     * @see [requiredEnergy]
     */
    protected abstract val energyUsage: Int

    /**
     * このブロックエンティティが稼働するたびに消費する電力の値
     */
    protected val requiredEnergy: Int get() = energyUsage * maxTicks

    override fun onServerTick(level: ServerLevel, pos: BlockPos, state: BlockState): TriState {
        // 一定間隔で実行する
        if (!canProcess()) return TriState.DEFAULT
        // 自動搬出する
        exportItems(level, pos)
        exportFluids(level, pos)
        // 処理を行う
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

    //    MenuProvider    //

    final override fun getDisplayName(): Component = blockState.block.name
}
