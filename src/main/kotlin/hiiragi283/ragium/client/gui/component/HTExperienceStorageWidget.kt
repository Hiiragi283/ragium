package hiiragi283.ragium.client.gui.component

import hiiragi283.ragium.api.gui.component.HTExperienceWidget
import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.math.HTBounds
import hiiragi283.ragium.api.registry.vanillaId
import hiiragi283.ragium.api.storage.HTAmountSetter
import hiiragi283.ragium.api.storage.experience.HTExperienceStorage
import hiiragi283.ragium.api.text.addExperienceTooltip
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.item.TooltipFlag
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTExperienceStorageWidget(
    private val background: (GuiGraphics, HTBounds) -> Unit,
    private val storage: HTExperienceStorage,
    private val amountSetter: HTAmountSetter.LongSized,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
) : HTSpriteWidget(x, y, width, height, Component.empty()),
    HTExperienceWidget {
    companion object {
        @JvmStatic
        fun createSlot(
            storage: HTExperienceStorage,
            amountSetter: HTAmountSetter.LongSized,
            x: Int,
            y: Int,
        ): HTExperienceStorageWidget = HTExperienceStorageWidget(
            { _, _ -> },
            storage,
            amountSetter,
            x,
            y,
            16,
            16,
        )

        @JvmStatic
        fun createTank(
            storage: HTExperienceStorage,
            amountSetter: HTAmountSetter.LongSized,
            x: Int,
            y: Int,
        ): HTExperienceStorageWidget = HTExperienceStorageWidget(
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
            storage,
            amountSetter,
            x,
            y,
            16,
            18 * 3 - 2,
        )
    }

    override fun shouldRender(): Boolean = !storage.isEmpty()

    override fun getSprite(): TextureAtlasSprite? = getSprite(vanillaId("block", "water_still"), InventoryMenu.BLOCK_ATLAS)

    override fun getColor(): Int = 0x66ff3300

    override fun getLevel(): Float = 1f

    override fun collectTooltips(consumer: (Component) -> Unit, flag: TooltipFlag) {
        addExperienceTooltip(storage, consumer)
    }

    override fun renderBackground(guiGraphics: GuiGraphics) {
        background(guiGraphics, getBounds())
    }

    //    HTExperienceWidget    //

    override fun setAmount(amount: Long) {
        amountSetter.setAmount(amount)
    }

    override fun getAmount(): Long = storage.getAmount()

    override fun getCapacity(): Long = storage.getCapacity()
}
