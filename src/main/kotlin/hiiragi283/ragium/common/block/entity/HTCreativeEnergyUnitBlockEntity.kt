package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.IEnergyStorage

class HTCreativeEnergyUnitBlockEntity(pos: BlockPos, state: BlockState) : HTBlockEntity(RagiumBlockEntityTypes.CEU, pos, state) {
    private val energyStorage: IEnergyStorage = object : IEnergyStorage {
        override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int = toReceive

        override fun extractEnergy(toExtract: Int, simulate: Boolean): Int = toExtract

        override fun getEnergyStored(): Int = 0

        override fun getMaxEnergyStored(): Int = Int.MAX_VALUE

        override fun canExtract(): Boolean = true

        override fun canReceive(): Boolean = true
    }

    override fun writeNbt(writer: HTNbtCodec.Writer) {}

    override fun readNbt(reader: HTNbtCodec.Reader) {}

    override fun getEnergyStorage(direction: Direction?): IEnergyStorage = energyStorage
}
