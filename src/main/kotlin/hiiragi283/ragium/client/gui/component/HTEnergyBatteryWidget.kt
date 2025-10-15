package hiiragi283.ragium.client.gui.component

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.text.energyText
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTEnergyBatteryWidget(private val batteryGetter: () -> HTEnergyBattery?, x: Int, y: Int) :
    HTSpriteWidget(x, y, 16, 18 * 3 - 2, Component.empty()) {
    companion object {
        @JvmStatic
        fun createNetwork(key: ResourceKey<Level>, x: Int, y: Int): HTEnergyBatteryWidget =
            HTEnergyBatteryWidget({ RagiumPlatform.INSTANCE.getEnergyNetwork(key) }, x, y)
    }

    override fun renderBackground(guiGraphics: GuiGraphics) {
        guiGraphics.blit(
            RagiumAPI.id("textures/gui/energy_gauge.png"),
            x - 1,
            y - 1,
            0f,
            0f,
            width + 2,
            height + 2,
            width + 2,
            height + 2,
        )
    }

    override fun shouldRender(): Boolean {
        val battery: HTEnergyBattery = batteryGetter() ?: return false
        return battery.getAmountAsInt() > 0
    }

    override fun getSprite(): TextureAtlasSprite? = Minecraft.getInstance().guiSprites.getSprite(RagiumAPI.id("container/energy_gauge"))

    override fun getColor(): Int = -1

    override fun getLevel(): Float = batteryGetter()?.getStoredLevelAsFloat() ?: 0f

    override fun collectTooltips(consumer: (Component) -> Unit, flag: TooltipFlag) {
        batteryGetter()?.let(::energyText)?.let(consumer)
    }
}
