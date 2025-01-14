package hiiragi283.ragium.api.util

import com.google.common.base.Suppliers
import hiiragi283.ragium.api.fluid.LimitedFluidTank
import hiiragi283.ragium.api.item.LimitedItemHandler
import net.neoforged.neoforge.fluids.IFluidTank
import net.neoforged.neoforge.items.IItemHandler

/**
 * ストレージの搬入出を管理するクラス
 */
enum class HTStorageIO(val canInsert: Boolean, val canExtract: Boolean) {
    INPUT(true, false),
    OUTPUT(false, true),
    GENERIC(true, true),
    INTERNAL(false, false),
    ;

    fun wrapItemHandler(handler: IItemHandler): IItemHandler = LimitedItemHandler(
        { this },
        Suppliers.ofInstance(handler),
    )

    fun wrapFluidTank(tank: IFluidTank): IFluidTank = LimitedFluidTank(this) { tank }
}
