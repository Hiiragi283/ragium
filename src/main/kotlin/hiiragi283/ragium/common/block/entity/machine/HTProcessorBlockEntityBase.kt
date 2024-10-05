package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipeProcessor
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class HTProcessorBlockEntityBase : HTMachineBlockEntityBase {
    @Deprecated("")
    constructor(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : super(type, pos, state)

    constructor(
        type: BlockEntityType<*>,
        pos: BlockPos,
        state: BlockState,
        machineType: HTMachineConvertible,
        tier: HTMachineTier,
    ) : super(
        type,
        pos,
        state,
        machineType,
        tier,
    )

    override fun validateMachineType(machineType: HTMachineType) {
        check(machineType.isProcessor())
    }

    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        HTMachineRecipeProcessor(parent).process(world, pos, machineType.asProcessor()!!, tier)
    }

    class Simple : HTProcessorBlockEntityBase {
        @Deprecated("")
        constructor(pos: BlockPos, state: BlockState) :
            super(RagiumBlockEntityTypes.PROCESSOR_MACHINE, pos, state)

        constructor(pos: BlockPos, state: BlockState, machineType: HTMachineType, tier: HTMachineTier) :
            super(RagiumBlockEntityTypes.PROCESSOR_MACHINE, pos, state, machineType, tier)
    }
}
