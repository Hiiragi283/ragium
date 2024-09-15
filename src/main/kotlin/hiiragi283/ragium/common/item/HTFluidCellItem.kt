package hiiragi283.ragium.common.item

import net.minecraft.fluid.Fluid
import net.minecraft.item.Item

class HTFluidCellItem(fluid: Fluid, settings: Settings) : Item(settings) {
    companion object {
        val instances: Map<Fluid, HTFluidCellItem>
            get() = instances1
        private val instances1: MutableMap<Fluid, HTFluidCellItem> = mutableMapOf()

        @JvmStatic
        operator fun get(fluid: Fluid): HTFluidCellItem? = instances1[fluid]

        @JvmStatic
        fun getOrThrow(fluid: Fluid): HTFluidCellItem = checkNotNull(get(fluid)) { "Fluid; $fluid does not have cell!" }
    }

    init {
        check(fluid !in instances) { "Cell with fluid; $fluid is already registered!" }
        instances1[fluid] = this
    }
}
