package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.math.fraction
import hiiragi283.ragium.client.gui.component.HTEnergyWidget
import hiiragi283.ragium.common.block.entity.storage.HTBatteryBlockEntity
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import org.apache.commons.lang3.math.Fraction

@OnlyIn(Dist.CLIENT)
class HTBatteryScreen(menu: HTBlockEntityContainerMenu<HTBatteryBlockEntity>, inventory: Inventory, title: Component) :
    HTBlockEntityContainerScreen<HTBatteryBlockEntity>(createTexture("battery"), menu, inventory, title) {
    override fun init() {
        super.init()
        addRenderableWidget(object : HTEnergyWidget(
            blockEntity.battery,
            startX + HTSlotHelper.getSlotPosX(4),
            startY + HTSlotHelper.getSlotPosY(0),
        ) {
            private val isCreative: Boolean get() = blockEntity.isCreative()

            override fun getScaledLevel(): Fraction = when (isCreative) {
                true -> fraction(this.height)
                false -> super.getScaledLevel()
            }
        })
    }
}
