package hiiragi283.ragium.client.screen.gui

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.screen.HTContainerScreen
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.common.inventory.HTEnergyNetworkAccessMenu
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.level.Level
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTEnergyNetworkAccessScreen(menu: HTEnergyNetworkAccessMenu, inventory: Inventory, title: Component) :
    HTContainerScreen<HTEnergyNetworkAccessMenu>(menu, inventory, title) {
    override val texture: ResourceLocation = RagiumAPI.id("textures/gui/container/energy_network_access.png")

    override fun init() {
        super.init()
        // Energy Widget
        addRenderableWidget(
            createEnergyWidget({
                menu.usePosition { level: Level, _: BlockPos ->
                    RagiumAPI.getInstance().getEnergyNetworkManager().getNetwork(level)
                }
            }, HTSlotHelper.getSlotPosX(4)),
        )
    }
}
