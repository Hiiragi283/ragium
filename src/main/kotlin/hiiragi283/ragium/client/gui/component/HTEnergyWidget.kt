package hiiragi283.ragium.client.gui.component

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.component.HTBoundsRenderer
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.text.HTTextUtil
import hiiragi283.ragium.client.gui.component.base.HTSpriteWidget
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.world.item.TooltipFlag
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import org.apache.commons.lang3.math.Fraction
import java.util.function.Consumer

@OnlyIn(Dist.CLIENT)
open class HTEnergyWidget(protected open val battery: HTEnergyBattery, x: Int, y: Int) :
    HTSpriteWidget(x, y, 16, 18 * 3 - 2, Component.empty()),
    HTEnergyBattery by battery {
    companion object {
        @JvmStatic
        private val BACKGROUND: HTBoundsRenderer = HTBoundsRenderer.fromSprite(RagiumAPI.id("textures", "gui", "energy_gauge.png"))
    }

    override fun renderBackground(guiGraphics: GuiGraphics) {
        BACKGROUND.render(guiGraphics, getBounds())
    }

    final override fun shouldRender(): Boolean = !battery.isEmpty()

    override fun getSprite(): TextureAtlasSprite? = Minecraft.getInstance().guiSprites.getSprite(RagiumAPI.id("container", "energy_gauge"))

    override fun getColor(): Int = -1

    override fun getLevel(): Fraction = battery.getStoredLevel()

    override fun collectTooltips(consumer: Consumer<Component>, flag: TooltipFlag) {
        HTTextUtil.addEnergyTooltip(battery.getAmount(), consumer, false)
    }
}
