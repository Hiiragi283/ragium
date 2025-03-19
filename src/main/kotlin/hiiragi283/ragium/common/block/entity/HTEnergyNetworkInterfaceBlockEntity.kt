package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.block.entity.HTHandlerBlockEntity
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.IEnergyStorage

class HTEnergyNetworkInterfaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntity(RagiumBlockEntityTypes.ENI, pos, state),
    HTHandlerBlockEntity {
    private var network: IEnergyStorage? = null

    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {}

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {}

    override fun afterLevelInit(level: Level) {
        network = RagiumAPI.getInstance().getEnergyNetwork(level)
    }

    override fun getEnergyStorage(direction: Direction?): IEnergyStorage? = network
}
