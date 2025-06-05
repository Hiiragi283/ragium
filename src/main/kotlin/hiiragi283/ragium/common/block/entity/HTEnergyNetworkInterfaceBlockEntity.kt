package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.IEnergyStorage

class HTEnergyNetworkInterfaceBlockEntity(pos: BlockPos, state: BlockState) : HTBlockEntity(RagiumBlockEntityTypes.ENI, pos, state) {
    private var network: IEnergyStorage? = null

    override fun writeNbt(writer: HTNbtCodec.Writer) {}

    override fun readNbt(reader: HTNbtCodec.Reader) {}

    override fun afterLevelInit(level: Level) {
        network = RagiumAPI.getInstance().getEnergyNetworkManager().getNetwork(level)
    }

    override fun getEnergyStorage(direction: Direction?): IEnergyStorage? = network
}
