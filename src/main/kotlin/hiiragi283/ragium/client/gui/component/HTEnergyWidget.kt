package hiiragi283.ragium.client.gui.component

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.storage.HTAmountView
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.text.HTTextUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.world.item.TooltipFlag
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import java.util.function.IntConsumer

@OnlyIn(Dist.CLIENT)
class HTEnergyWidget(
    private val battery: HTEnergyBattery,
    private val amountSetter: IntConsumer,
    x: Int,
    y: Int,
) : HTSpriteWidget(x, y, 16, 18 * 3 - 2, Component.empty()),
    HTAmountView.IntSized {
    override fun renderBackground(guiGraphics: GuiGraphics) {
        guiGraphics.blit(
            RagiumAPI.id("textures", "gui", "energy_gauge.png"),
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

    override fun shouldRender(): Boolean = !battery.isEmpty()

    override fun getSprite(): TextureAtlasSprite? = Minecraft.getInstance().guiSprites.getSprite(RagiumAPI.id("container", "energy_gauge"))

    override fun getColor(): Int = -1

    override fun getLevel(): Float = battery.getStoredLevelAsFloat()

    override fun collectTooltips(consumer: (Component) -> Unit, flag: TooltipFlag) {
        HTTextUtil.addEnergyTooltip(battery, consumer)
    }

    //    HTEnergyWidget    //

    fun setAmount(amount: Int) {
        amountSetter.accept(amount)
    }

    override fun getAmount(): Int = battery.getAmount()

    override fun getCapacity(): Int = battery.getCapacity()
}
