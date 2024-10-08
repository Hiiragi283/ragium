package hiiragi283.ragium.api.machine

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

data class HTMachineCondition(val condition: Condition, val succeeded: Succeeded, val failed: Failed = Failed.EMPTY) {
    companion object {
        @JvmField
        val EMPTY = HTMachineCondition(
            Condition.FALSE,
            Succeeded.EMPTY,
            Failed.EMPTY,
        )
    }

    //    Condition    //

    fun interface Condition {
        fun match(
            world: World,
            pos: BlockPos,
            machineType: HTMachineType,
            tier: HTMachineTier,
        ): Boolean

        companion object {
            @JvmField
            val FALSE = Condition { _: World, _: BlockPos, _: HTMachineType, _: HTMachineTier -> false }
        }
    }

    //    Succeeded    //

    fun interface Succeeded {
        fun onSucceeded(
            world: World,
            pos: BlockPos,
            machineType: HTMachineType,
            tier: HTMachineTier,
        )

        companion object {
            @JvmField
            val EMPTY = Succeeded { _: World, _: BlockPos, _: HTMachineType, _: HTMachineTier -> }
        }
    }

    //    Failed    //

    fun interface Failed {
        fun onFailed(
            world: World,
            pos: BlockPos,
            machineType: HTMachineType,
            tier: HTMachineTier,
        )

        companion object {
            @JvmField
            val EMPTY = Failed { _: World, _: BlockPos, _: HTMachineType, _: HTMachineTier -> }
        }
    }
}
