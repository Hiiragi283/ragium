package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class HTImitationSpawnerBlockEntity(pos: BlockPos, state: BlockState) :
    ExtendedBlockEntity(RagiumBlockEntityTypes.IMITATION_SPAWNER, pos, state)
