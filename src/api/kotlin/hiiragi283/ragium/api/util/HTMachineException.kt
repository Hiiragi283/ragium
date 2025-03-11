package hiiragi283.ragium.api.util

import hiiragi283.ragium.api.storage.HTStorageIO
import net.minecraft.network.chat.Component

sealed class HTMachineException(private val component: Component) : RuntimeException(component.string) {
    constructor(translationKey: String, vararg args: Any) : this(Component.translatable(translationKey, *args))

    override fun getLocalizedMessage(): String? = component.string

    //    Impl    //

    class Custom(message: String) : HTMachineException(message)

    class InvalidMultiblock : HTMachineException("Invalid Multiblock!")

    class MissingSlot(storageIO: HTStorageIO, index: Int) :
        HTMachineException(RagiumTranslationKeys.EXCEPTION_MISSING_SLOT, storageIO.serializedName, index)

    class MissingTank(storageIO: HTStorageIO, index: Int) :
        HTMachineException(RagiumTranslationKeys.EXCEPTION_MISSING_TANK, storageIO.serializedName, index)

    //    Energy    //

    class ConsumeEnergy : HTMachineException(RagiumTranslationKeys.EXCEPTION_CONSUME_ENERGY)

    class GenerateEnergy : HTMachineException(RagiumTranslationKeys.EXCEPTION_GENERATE_ENERGY)

    //    Fluid    //

    class ShrinkFluid : HTMachineException("Failed to extract input fluid!")

    class GrowFluid : HTMachineException("Failed to insert output fluid!")

    //    Item    //

    class ShrinkItem : HTMachineException("Failed to extract input item!")

    class GrowItem : HTMachineException("Failed to insert output item!")

    //    Recipe    //

    class NoMatchingRecipe : HTMachineException(RagiumTranslationKeys.EXCEPTION_NO_MATCHING_RECIPE)
}
