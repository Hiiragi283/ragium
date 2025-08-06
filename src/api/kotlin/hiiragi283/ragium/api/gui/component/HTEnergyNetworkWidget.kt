package hiiragi283.ragium.api.gui.component

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.energyText
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.world.item.TooltipFlag
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.energy.IEnergyStorage
import org.slf4j.Logger

@OnlyIn(Dist.CLIENT)
class HTEnergyNetworkWidget(
    private val networkGetter: () -> IEnergyStorage?,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
) : HTSpriteWidget(x, y, width, height, Component.empty()) {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()
    }

    override fun shouldRender(): Boolean {
        val network: IEnergyStorage = networkGetter() ?: return false
        return network.energyStored > 0
    }

    override fun getSprite(): TextureAtlasSprite {
        val rawSprite: TextureAtlasSprite = Minecraft.getInstance().guiSprites.getSprite(RagiumAPI.id("container/energy_gauge"))
        LOGGER.debug("Current atlas {}", rawSprite.atlasLocation())
        LOGGER.debug("Current sprite {}", rawSprite)
        return rawSprite
    }

    override fun getColor(): Int = 0

    override fun getLevel(): Float {
        val network: IEnergyStorage = networkGetter() ?: return 0f
        return network.energyStored / network.maxEnergyStored.toFloat()
    }

    override fun collectTooltips(consumer: (Component) -> Unit, flag: TooltipFlag) {
        val network: IEnergyStorage = networkGetter() ?: return
        consumer(energyText(network))
    }
}
