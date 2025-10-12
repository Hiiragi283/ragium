package hiiragi283.ragium.api.storage

import net.neoforged.neoforge.fluids.capability.IFluidHandler

/**
 * [HTStackSlot]へのIOを識別するフラグ
 * @param execute 実際に処理を行うかどうか
 * @see [mekanism.api.Action]
 */
@JvmInline
value class HTStorageAction private constructor(val execute: Boolean) {
    companion object {
        /**
         * 実際の処理は行わない[HTStorageAction]
         */

        val SIMULATE = HTStorageAction(false)

        /**
         * 実際の処理を行う[HTStorageAction]
         */
        val EXECUTE = HTStorageAction(true)

        /**
         * [Boolean]から[HTStorageAction]に変換します。
         */
        @JvmStatic
        fun of(simulate: Boolean): HTStorageAction = when (simulate) {
            true -> SIMULATE
            false -> EXECUTE
        }

        /**
         * [IFluidHandler.FluidAction]から[HTStorageAction]に変換します。
         */
        @JvmStatic
        fun of(action: IFluidHandler.FluidAction): HTStorageAction = when (action) {
            IFluidHandler.FluidAction.EXECUTE -> EXECUTE
            IFluidHandler.FluidAction.SIMULATE -> SIMULATE
        }
    }

    /**
     * 処理を仮想で行うかどうか
     */
    val simulate: Boolean get() = !execute

    /**
     * このフラグを[IFluidHandler.FluidAction]に変換します。
     */
    fun toFluid(): IFluidHandler.FluidAction = when (this.execute) {
        true -> IFluidHandler.FluidAction.EXECUTE
        false -> IFluidHandler.FluidAction.SIMULATE
    }
}
