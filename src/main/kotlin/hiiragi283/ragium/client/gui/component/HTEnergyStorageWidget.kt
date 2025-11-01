package hiiragi283.ragium.client.gui.component

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.component.HTEnergyWidget
import hiiragi283.ragium.api.storage.HTAmountSetter
import hiiragi283.ragium.api.storage.energy.HTEnergyStorage
import hiiragi283.ragium.api.text.addEnergyTooltip
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.world.item.TooltipFlag
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTEnergyStorageWidget(
    private val storage: HTEnergyStorage,
    private val amountSetter: HTAmountSetter.IntSized,
    x: Int,
    y: Int,
) : HTSpriteWidget(x, y, 16, 18 * 3 - 2, Component.empty()),
    HTEnergyWidget {
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

    override fun shouldRender(): Boolean = !storage.isEmpty()

    override fun getSprite(): TextureAtlasSprite? = Minecraft.getInstance().guiSprites.getSprite(RagiumAPI.id("container/energy_gauge"))

    override fun getColor(): Int = -1

    override fun getLevel(): Float = storage.getStoredLevelAsFloat()

    override fun collectTooltips(consumer: (Component) -> Unit, flag: TooltipFlag) {
        addEnergyTooltip(storage, consumer)
    }

    //    HTEnergyWidget    //

    override fun setAmount(amount: Int) {
        amountSetter.setAmount(amount)
    }

    override fun getAmount(): Int = storage.getAmount()

    override fun getCapacity(): Int = storage.getCapacity()
}
