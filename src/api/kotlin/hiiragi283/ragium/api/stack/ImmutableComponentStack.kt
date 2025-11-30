package hiiragi283.ragium.api.stack

import net.minecraft.core.component.DataComponentHolder
import net.minecraft.core.component.DataComponentPatch

interface ImmutableComponentStack<TYPE : Any, STACK : ImmutableComponentStack<TYPE, STACK>> :
    ImmutableStack<TYPE, STACK>,
    DataComponentHolder {
    /**
     * このスタックの[DataComponentPatch]を返します。
     */
    fun componentsPatch(): DataComponentPatch
}
