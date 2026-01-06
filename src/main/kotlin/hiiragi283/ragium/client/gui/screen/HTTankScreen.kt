package hiiragi283.ragium.client.gui.screen

import hiiragi283.core.api.math.fraction
import hiiragi283.core.client.gui.screen.HTBlockEntityContainerScreen
import hiiragi283.core.common.inventory.HTSlotHelper
import hiiragi283.ragium.client.gui.component.HTFluidTankWidget
import hiiragi283.ragium.common.block.entity.storage.HTTankBlockEntity
import hiiragi283.ragium.common.inventory.HTTankMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import org.apache.commons.lang3.math.Fraction

class HTTankScreen(menu: HTTankMenu, inventory: Inventory, title: Component) :
    HTBlockEntityContainerScreen<HTTankBlockEntity, HTTankMenu>(menu, inventory, title) {
    override fun init() {
        super.init()

        addFluidWidget(HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(0)) { x: Int, y: Int ->
            object : HTFluidTankWidget(blockEntity.tank, x, y) {
                private val isCreative: Boolean get() = blockEntity.isCreative()

                override fun getScaledLevel(): Fraction = when (isCreative) {
                    true -> fraction(this.height)
                    false -> super.getScaledLevel()
                }
            }
        }
    }
}
