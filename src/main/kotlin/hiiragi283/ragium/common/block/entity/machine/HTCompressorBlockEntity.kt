package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.common.machine.HTMachineType
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

class HTCompressorBlockEntity(pos: BlockPos, state: BlockState) :
    HTSingleMachineBlockEntity(HTMachineType.Single.COMPRESSOR, pos, state)
