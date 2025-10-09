package hiiragi283.ragium.api.storage

import net.neoforged.neoforge.fluids.capability.IFluidHandler

fun IFluidHandler.FluidAction.wrapAction(): HTStorageAction = HTStorageAction.of(this.simulate())

@JvmInline
value class HTStorageAction private constructor(val execute: Boolean) {
    companion object {
        val SIMULATE = HTStorageAction(false)

        val EXECUTE = HTStorageAction(true)

        @JvmStatic
        fun of(simulate: Boolean): HTStorageAction = when (simulate) {
            true -> SIMULATE
            false -> EXECUTE
        }
    }
}
