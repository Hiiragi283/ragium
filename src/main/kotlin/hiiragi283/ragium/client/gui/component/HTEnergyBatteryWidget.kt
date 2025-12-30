package hiiragi283.ragium.client.gui.component

import hiiragi283.core.api.storage.amount.HTAmountView
import hiiragi283.core.client.gui.component.HTSpriteWidget
import hiiragi283.core.util.HTTooltipHelper
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.world.item.TooltipFlag
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import org.apache.commons.lang3.math.Fraction
import java.util.function.Consumer

@OnlyIn(Dist.CLIENT)
class HTEnergyBatteryWidget(private val view: HTAmountView.IntSized, x: Int, y: Int) :
    HTSpriteWidget(x, y, 7, 18 * 3 - 2, Component.empty()),
    HTAmountView.IntSized by view {
    override fun shouldRender(): Boolean = getAmount() > 0

    override fun getSprite(): TextureAtlasSprite? = Minecraft.getInstance().guiSprites.getSprite(RagiumAPI.id("container", "energy_bar"))

    override fun getColor(): Int = -1

    override fun getLevel(): Fraction = getStoredLevel()

    override fun collectTooltips(consumer: Consumer<Component>, flag: TooltipFlag) {
        HTTooltipHelper.addEnergyTooltip(getAmount(), consumer, false)
    }
}
