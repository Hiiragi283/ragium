package hiiragi283.ragium.common.integration.mekanism.storage

import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.capability.HTMultiCapability
import hiiragi283.ragium.api.storage.capability.HTMultiCapabilityBase
import mekanism.api.Action
import mekanism.api.IContentsListener
import mekanism.api.chemical.IChemicalHandler
import mekanism.common.capabilities.Capabilities
import net.minecraft.core.Direction

/**
 * @see [hiiragi283.ragium.api.storage.capability.RagiumCapabilities]
 */
object MekanismCapabilities {
    @Suppress("UNCHECKED_CAST")
    @JvmField
    val CHEMICAL: HTMultiCapability<IChemicalHandler, IChemicalHandler, HTChemicalHandler, HTChemicalTank> = HTMultiCapabilityBase(
        Capabilities.CHEMICAL.block,
        Capabilities.CHEMICAL.item,
        ::wrapHandler,
        HTChemicalHandler::getChemicalTanks,
    )

    //    Wrapper    //
    @JvmStatic
    private fun wrapHandler(handler: IChemicalHandler): HTChemicalHandler = when (handler) {
        is HTChemicalHandler -> handler
        else -> object : HTChemicalHandler {
            override fun getChemicalTanks(side: Direction?): List<HTChemicalTank> =
                (0..<handler.chemicalTanks).mapNotNull { index: Int -> createTank(handler, index) }

            override fun onContentsChanged() {
                (handler as? IContentsListener)?.onContentsChanged()
            }
        }
    }

    @JvmStatic
    private fun createTank(handler: IChemicalHandler, index: Int): HTChemicalTank? = if (handler is HTChemicalHandler) {
        handler.getChemicalTank(index, handler.sideFor)
    } else {
        object : HTChemicalTank, HTValueSerializable.Empty {
            override fun getStack(): ImmutableChemicalStack = handler.getChemicalInTank(index).let(ImmutableChemicalStack::of)

            override fun getCapacityAsLong(stack: ImmutableChemicalStack): Long = handler.getChemicalTankCapacity(index)

            override fun isValid(stack: ImmutableChemicalStack): Boolean = handler.isValid(index, stack.stack)

            override fun insert(stack: ImmutableChemicalStack, action: HTStorageAction, access: HTStorageAccess): ImmutableChemicalStack =
                handler.insertChemical(index, stack.stack, convert(action)).let(ImmutableChemicalStack::of)

            override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): ImmutableChemicalStack =
                handler.extractChemical(index, amount.toLong(), convert(action)).let(ImmutableChemicalStack::of)

            override fun onContentsChanged() {
                (handler as? IContentsListener)?.onContentsChanged()
            }
        }
    }

    @JvmStatic
    fun convert(action: Action): HTStorageAction = when (action.execute()) {
        true -> HTStorageAction.EXECUTE
        false -> HTStorageAction.SIMULATE
    }

    @JvmStatic
    fun convert(action: HTStorageAction): Action = when (action.execute) {
        true -> Action.EXECUTE
        false -> Action.SIMULATE
    }
}
