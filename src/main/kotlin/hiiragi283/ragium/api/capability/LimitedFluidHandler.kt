package hiiragi283.ragium.api.capability

import com.google.common.base.Functions
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.IFluidTank
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import java.util.function.Function

class LimitedFluidHandler(private val ioProvider: Function<Int, HTStorageIO>, private val delegate: Map<Int, IFluidTank>) :
    IFluidHandler {
    companion object {
        private fun arrayToMap(delegate: Array<out IFluidTank>): Map<Int, IFluidTank> =
            delegate.mapIndexed { index: Int, tank: IFluidTank -> index to tank }.toMap()

        @JvmStatic
        fun small(delegate: Array<out IFluidTank>): LimitedFluidHandler = LimitedFluidHandler(
            mapOf(
                0 to HTStorageIO.INPUT,
                1 to HTStorageIO.OUTPUT,
            ),
            arrayToMap(delegate),
        )

        @JvmStatic
        fun basic(delegate: Array<out IFluidTank>): LimitedFluidHandler = LimitedFluidHandler(
            mapOf(
                0 to HTStorageIO.INPUT,
                1 to HTStorageIO.INPUT,
                2 to HTStorageIO.OUTPUT,
                3 to HTStorageIO.OUTPUT,
            ),
            arrayToMap(delegate),
        )
    }

    constructor(map: Map<Int, HTStorageIO>, array: Array<out IFluidTank>) : this(
        Functions.forMap(map, HTStorageIO.INTERNAL),
        arrayToMap(array),
    )

    constructor(map: Map<Int, HTStorageIO>, delegate: Map<Int, IFluidTank>) : this(
        Functions.forMap(map, HTStorageIO.INTERNAL),
        delegate,
    )

    override fun getTanks(): Int = delegate.values.size

    override fun getFluidInTank(tank: Int): FluidStack = delegate[tank]?.fluid ?: FluidStack.EMPTY

    override fun getTankCapacity(tank: Int): Int = delegate[tank]?.capacity ?: 0

    override fun isFluidValid(tank: Int, stack: FluidStack): Boolean = delegate[tank]?.isFluidValid(stack) == true

    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int {
        for ((index: Int, tank: IFluidTank) in delegate.entries) {
            if (!ioProvider.apply(index).canInsert) continue
            return tank.fill(resource, action)
        }
        return 0
    }

    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack {
        for ((index: Int, tank: IFluidTank) in delegate.entries) {
            if (!ioProvider.apply(index).canExtract) continue
            return tank.drain(resource, action)
        }
        return FluidStack.EMPTY
    }

    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack {
        for ((index: Int, tank: IFluidTank) in delegate.entries) {
            if (!ioProvider.apply(index).canExtract) continue
            return tank.drain(maxDrain, action)
        }
        return FluidStack.EMPTY
    }
}
