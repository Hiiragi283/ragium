package hiiragi283.ragium.client.gui.component

import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.math.HTBounds
import hiiragi283.ragium.api.registry.vanillaId
import hiiragi283.ragium.api.storage.HTAmountSetter
import hiiragi283.ragium.api.storage.HTAmountView
import hiiragi283.ragium.api.storage.experience.HTExperienceTank
import hiiragi283.ragium.api.text.addExperienceTooltip
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.item.TooltipFlag
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTExperienceWidget(
    private val background: (GuiGraphics, HTBounds) -> Unit,
    private val tank: HTExperienceTank,
    private val amountSetter: HTAmountSetter.LongSized,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
) : HTSpriteWidget(x, y, width, height, Component.empty()),
    HTAmountView.LongSized {
    companion object {
        @JvmStatic
        fun createSlot(
            tank: HTExperienceTank,
            amountSetter: HTAmountSetter.LongSized,
            x: Int,
            y: Int,
        ): HTExperienceWidget = HTExperienceWidget(
            { _, _ -> },
            tank,
            amountSetter,
            x,
            y,
            16,
            16,
        )

        @JvmStatic
        fun createTank(
            tank: HTExperienceTank,
            amountSetter: HTAmountSetter.LongSized,
            x: Int,
            y: Int,
        ): HTExperienceWidget = HTExperienceWidget(
            { guiGraphics: GuiGraphics, bounds: HTBounds ->
                guiGraphics.blit(
                    HTFluidWidget.TANK_ID,
                    bounds.x - 1,
                    bounds.y - 1,
                    0f,
                    0f,
                    bounds.width + 2,
                    bounds.height + 2,
                    bounds.width + 2,
                    bounds.height + 2,
                )
            },
            tank,
            amountSetter,
            x,
            y,
            16,
            18 * 3 - 2,
        )
    }

    override fun shouldRender(): Boolean = !tank.isEmpty()

    override fun getSprite(): TextureAtlasSprite? = getSprite(vanillaId("block", "water_still"), InventoryMenu.BLOCK_ATLAS)

    override fun getColor(): Int = 0x66ff33

    override fun getLevel(): Float = 1f

    override fun collectTooltips(consumer: (Component) -> Unit, flag: TooltipFlag) {
        addExperienceTooltip(tank, consumer)
    }

    override fun renderBackground(guiGraphics: GuiGraphics) {
        background(guiGraphics, getBounds())
    }

    //    HTExperienceWidget    //

    fun setAmount(amount: Long) {
        amountSetter.setAmount(amount)
    }

    override fun getAmount(): Long = tank.getAmount()

    override fun getCapacity(): Long = tank.getCapacity()
}
