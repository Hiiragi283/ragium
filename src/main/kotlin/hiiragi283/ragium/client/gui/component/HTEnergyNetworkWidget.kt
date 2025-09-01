package hiiragi283.ragium.client.gui.component

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.energyText
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.energy.IEnergyStorage

@OnlyIn(Dist.CLIENT)
class HTEnergyNetworkWidget(private val key: ResourceKey<Level>, x: Int, y: Int) :
    HTSpriteWidget(x, y - 1, 16, 18 * 3 - 2, Component.empty()) {
    fun getNetwork(): IEnergyStorage? = RagiumAPI.getInstance().getEnergyNetwork(key)

    override fun renderBackground(guiGraphics: GuiGraphics) {
        guiGraphics.blit(
            RagiumAPI.id("textures/gui/energy_gauge.png"),
            x - 1,
            y,
            0f,
            0f,
            width + 2,
            height + 2,
            width + 2,
            height + 2,
        )
    }

    override fun shouldRender(): Boolean {
        val network: IEnergyStorage = getNetwork() ?: return false
        return network.energyStored > 0
    }

    override fun getSprite(): TextureAtlasSprite? = Minecraft.getInstance().guiSprites.getSprite(RagiumAPI.id("container/energy_gauge"))

    override fun getColor(): Int = -1

    override fun getLevel(): Float {
        val network: IEnergyStorage = getNetwork() ?: return 0f
        return network.energyStored / network.maxEnergyStored.toFloat()
    }

    override fun collectTooltips(consumer: (Component) -> Unit, flag: TooltipFlag) {
        val network: IEnergyStorage = getNetwork() ?: return
        consumer(energyText(network))
    }
}
