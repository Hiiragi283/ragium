package hiiragi283.ragium.common.block.entity.machine.electric

import hiiragi283.ragium.common.block.entity.machine.HTSingleMachineBlockEntity
import hiiragi283.ragium.common.machine.HTMachineType
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

class HTChemicalReactorBlockEntity(pos: BlockPos, state: BlockState) :
    HTSingleMachineBlockEntity(HTMachineType.Single.MIXER, pos, state)
