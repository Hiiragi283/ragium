package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage

class HTInfuserBlockEntity(pos: BlockPos, state: BlockState) : HTMachineBlockEntity(RagiumBlockEntityTypes.INFUSER, pos, state) {
    override fun writeNbt(writer: HTNbtCodec.Writer) {
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
    }

    override fun onServerTick(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
    ): TriState = TriState.DEFAULT
}
