package hiiragi283.ragium.api.stack

import net.minecraft.core.component.DataComponentHolder
import net.minecraft.core.component.DataComponentPatch

interface ImmutableComponentStack<T : Any, STACK : ImmutableComponentStack<T, STACK>> :
    ImmutableStack<T, STACK>,
    DataComponentHolder {
    /**
     * このスタックの[DataComponentPatch]を返します。
     */
    fun componentsPatch(): DataComponentPatch
}
