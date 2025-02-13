package hiiragi283.ragium.api.machine

sealed class HTMachineException(val showInLog: Boolean, message: String) : RuntimeException(message) {
    class Custom(showInLog: Boolean, message: String) : HTMachineException(showInLog, message)

    //    Energy    //

    class FindFuel(showInLog: Boolean) : HTMachineException(showInLog, "Failed to find fuel!")

    class ConsumeFuel(showInLog: Boolean) : HTMachineException(showInLog, "Failed to consume fuel!")

    class GenerateEnergy(showInLog: Boolean) : HTMachineException(showInLog, "Failed to generate energy!")

    class HandleEnergy(showInLog: Boolean) : HTMachineException(showInLog, "Failed to handle required energy from network!")

    //    Fluid    //

    class ExtractFluid(showInLog: Boolean) : HTMachineException(showInLog, "Failed to extract fluid into the storage!")

    //    Item    //

    class NoMatchingRecipe(showInLog: Boolean) : HTMachineException(showInLog, "Failed to find matching recipe!")

    class ConsumeInput(showInLog: Boolean) : HTMachineException(showInLog, "Failed to consume recipe inputs!")

    class MergeResult(showInLog: Boolean) : HTMachineException(showInLog, "Failed to merge results into outputs!")
}
