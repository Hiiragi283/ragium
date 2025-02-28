package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.storage.HTStorageIO

sealed class HTMachineException(message: String) : RuntimeException(message) {
    class Custom(message: String) : HTMachineException(message)

    class InvalidMultiblock : HTMachineException("Invalid Multiblock!")

    class MissingSlot(storageIO: HTStorageIO, index: Int) :
        HTMachineException("Missing ${storageIO.serializedName} slot for index: $index!")

    class MissingTank(storageIO: HTStorageIO, index: Int) :
        HTMachineException("Missing ${storageIO.serializedName} tank for index: $index!")

    //    Energy    //

    class ConsumeEnergy : HTMachineException("Failed to extract energy!")

    class GenerateEnergy : HTMachineException("Failed to receive energy!")

    //    Fluid    //

    class ShrinkFluid : HTMachineException("Failed to extract input fluid!")

    class GrowFluid : HTMachineException("Failed to insert output fluid!")

    //    Item    //

    class ShrinkItem : HTMachineException("Failed to extract input item!")

    class GrowItem : HTMachineException("Failed to insert output item!")

    //    Recipe    //

    class NoMatchingRecipe : HTMachineException("Failed to find matching recipe!")
}
