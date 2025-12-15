package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.math.fraction
import hiiragi283.ragium.api.text.HTTextUtil
import hiiragi283.ragium.client.gui.component.HTFluidTankWidget
import hiiragi283.ragium.common.block.entity.storage.HTTankBlockEntity
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.TooltipFlag
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import org.apache.commons.lang3.math.Fraction
import java.util.function.Consumer

@OnlyIn(Dist.CLIENT)
class HTTankScreen(menu: HTBlockEntityContainerMenu<HTTankBlockEntity>, inventory: Inventory, title: Component) :
    HTBlockEntityContainerScreen<HTTankBlockEntity>(createTexture("tank"), menu, inventory, title) {
    override fun init() {
        super.init()
        addFluidWidget(object : HTFluidTankWidget(
            blockEntity.tank,
            startX + HTSlotHelper.getSlotPosX(4),
            startY + HTSlotHelper.getSlotPosY(0),
        ) {
            private val isCreative: Boolean get() = blockEntity.isCreative()

            override fun getScaledLevel(): Fraction = when (isCreative) {
                true -> fraction(this.height)
                false -> super.getScaledLevel()
            }

            override fun collectTooltips(consumer: Consumer<Component>, flag: TooltipFlag) {
                HTTextUtil.addFluidTooltip(getStack(), consumer, flag, isCreative)
            }
        })
    }
}
