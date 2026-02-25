package hiiragi283.ragium.common.data.holder

import hiiragi283.core.api.util.Ior
import hiiragi283.core.api.util.toIorOrThrow

class HTIorHolder<ITEM : Any, FLUID : Any> {
    private var item: ITEM? = null
    private var fluid: FLUID? = null

    @JvmName("setItem")
    operator fun plusAssign(left: ITEM) {
        check(this.item == null) { "Item value has already initialized" }
        this.item = left
    }

    @JvmName("setFluid")
    operator fun plusAssign(right: FLUID) {
        check(this.fluid == null) { "Fluid value has already initialized" }
        this.fluid = right
    }

    fun toIor(): Ior<ITEM, FLUID> = (item to fluid).toIorOrThrow()
}
