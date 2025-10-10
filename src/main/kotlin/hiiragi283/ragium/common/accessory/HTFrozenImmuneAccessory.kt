package hiiragi283.ragium.common.accessory

import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.common.item.base.HTFluidItem
import hiiragi283.ragium.setup.RagiumFluidContents
import io.wispforest.accessories.api.Accessory
import io.wispforest.accessories.api.events.extra.ShouldFreezeEntity
import io.wispforest.accessories.api.slot.SlotReference
import net.fabricmc.fabric.api.util.TriState
import net.minecraft.world.item.ItemStack

class HTFrozenImmuneAccessory :
    Accessory,
    ShouldFreezeEntity {
    override fun shouldFreeze(stack: ItemStack, reference: SlotReference): TriState {
        val tank: HTFluidTank = HTFluidItem.getFluidTank(stack, 0) ?: return TriState.DEFAULT
        if (RagiumFluidContents.CRIMSON_BLOOD.isOf(tank.getStack())) {
            if (tank.extract(10, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAl).amountAsInt() >= 10) {
                tank.extract(10, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAl)
                return TriState.FALSE
            }
        }
        return TriState.DEFAULT
    }
}
