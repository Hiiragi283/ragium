package hiiragi283.ragium.common.integration.mekanism.storage

import hiiragi283.ragium.api.storage.HTStackView
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.capability.HTViewCapability
import hiiragi283.ragium.impl.storage.capability.HTViewCapabilityBase
import mekanism.api.Action
import mekanism.api.chemical.IChemicalHandler
import mekanism.common.capabilities.Capabilities
import net.minecraft.core.Direction

/**
 * @see [hiiragi283.ragium.api.storage.capability.RagiumCapabilities]
 */
object MekanismCapabilities {
    @JvmField
    val CHEMICAL: HTViewCapability<IChemicalHandler, IChemicalHandler, ImmutableChemicalStack> = HTViewCapabilityBase(
        Capabilities.CHEMICAL.block,
        Capabilities.CHEMICAL.item,
    ) { handler: IChemicalHandler, side: Direction? ->
        if (handler is HTChemicalHandler) {
            handler.getChemicalTanks(side)
        } else {
            (0..<handler.chemicalTanks).map { tank: Int ->
                object : HTStackView<ImmutableChemicalStack> {
                    override fun getStack(): ImmutableChemicalStack = handler.getChemicalInTank(tank).toImmutable()

                    override fun getCapacityAsLong(stack: ImmutableChemicalStack): Long = handler.getChemicalTankCapacity(tank)
                }
            }
        }
    }

    //    Wrapper    //
    /*@JvmStatic
    private fun wrapHandler(handler: IChemicalHandler): HTChemicalHandler = handler as? HTChemicalHandler
        ?: HTChemicalHandler { (0..<handler.chemicalTanks).mapNotNull { index: Int -> createTank(handler, index) } }

    @JvmStatic
    private fun createTank(handler: IChemicalHandler, index: Int): HTChemicalTank? = if (handler is HTChemicalHandler) {
        handler.getChemicalTank(index, handler.sideFor)
    } else {
        object : HTChemicalTank, HTValueSerializable.Empty {
            override fun getStack(): ImmutableChemicalStack = handler.getChemicalInTank(index).toImmutable()

            override fun getCapacityAsLong(stack: ImmutableChemicalStack): Long = handler.getChemicalTankCapacity(index)

            override fun isValid(stack: ImmutableChemicalStack): Boolean = handler.isValid(index, stack.stack)

            override fun insert(stack: ImmutableChemicalStack, action: HTStorageAction, access: HTStorageAccess): ImmutableChemicalStack =
                handler.insertChemical(index, stack.stack, convert(action)).toImmutable()

            override fun extract(stack: ImmutableChemicalStack, action: HTStorageAction, access: HTStorageAccess): ImmutableChemicalStack =
                when {
                    this.isSameStack(stack, this.getStack()) ->
                        handler.extractChemical(stack.stack, convert(action)).toImmutable()
                    else -> ImmutableChemicalStack.EMPTY
                }

            override fun onContentsChanged() {
                (handler as? IContentsListener)?.onContentsChanged()
            }
        }
    }*/

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
