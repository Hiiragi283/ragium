package hiiragi283.ragium.api.machine

import net.minecraft.world.level.block.Block

sealed class HTMachineException(val showInLog: Boolean, message: String) : RuntimeException(message) {
    class Custom(showInLog: Boolean, message: String) : HTMachineException(showInLog, message)

    //    Block    //

    class AroundBlock(showInLog: Boolean, block: Block, count: Int = 1) :
        HTMachineException(showInLog, "Require ${count}x ${block.name.string} around the machine!")

    //    Energy    //

    class FindFuel(showInLog: Boolean) : HTMachineException(showInLog, "Failed to find fuel!")

    class ConsumeFuel(showInLog: Boolean) : HTMachineException(showInLog, "Failed to consume fuel!")

    class ConsumeEnergy(showInLog: Boolean) : HTMachineException(showInLog, "Failed to consume energy!")

    class GenerateEnergy(showInLog: Boolean) : HTMachineException(showInLog, "Failed to generate energy!")

    //    Fluid    //

    class CalculateAmount(showInLog: Boolean) : HTMachineException(showInLog, "Failed to calculate required fluid amount!")

    class FindFluid(showInLog: Boolean) : HTMachineException(showInLog, "Failed to find fluid!")

    class FluidInteract(showInLog: Boolean) : HTMachineException(showInLog, "Failed to interact with the fluid storage!")

    class InsertFluid(showInLog: Boolean) : HTMachineException(showInLog, "Failed to insert fluid into the storage!")

    class MaxFluid(showInLog: Boolean) : HTMachineException(showInLog, "The fluid storage is already full!")

    //    Item    //

    class NoMatchingRecipe(message: String) : HTMachineException(false, message)

    class MergeResult(showInLog: Boolean) : HTMachineException(showInLog, "Failed to merge results into outputs!")
}
