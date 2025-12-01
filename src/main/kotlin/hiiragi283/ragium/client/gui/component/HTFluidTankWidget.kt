package hiiragi283.ragium.client.gui.component

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.gui.component.HTBoundsRenderer
import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.getStillTexture
import hiiragi283.ragium.api.stack.getTintColor
import hiiragi283.ragium.api.storage.fluid.HTFluidView
import hiiragi283.ragium.api.text.HTTextUtil
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.world.item.TooltipFlag
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTFluidTankWidget(
    private val levelGetter: (HTFluidView) -> Float,
    private val background: HTBoundsRenderer,
    private val view: HTFluidView,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
) : HTSpriteWidget(x, y, width, height, Component.empty()),
    HTFluidWidget {
    companion object {
        @JvmStatic
        fun createSlot(view: HTFluidView, x: Int, y: Int): HTFluidTankWidget = HTFluidTankWidget(
            { 1f },
            HTBoundsRenderer.fromSprite(RagiumAPI.id("textures", "gui", "fluid_slot.png")),
            view,
            x,
            y,
            16,
            16,
        )

        @JvmStatic
        fun createTank(view: HTFluidView, x: Int, y: Int): HTFluidTankWidget = HTFluidTankWidget(
            HTFluidView::getStoredLevelAsFloat,
            HTBoundsRenderer.fromSprite(RagiumAPI.id("textures", "gui", "tank.png")),
            view,
            x,
            y,
            16,
            18 * 3 - 2,
        )
    }

    override fun shouldRender(): Boolean = getStack() != null

    override fun getSprite(): TextureAtlasSprite? = getSprite(getStack()?.getStillTexture(), RagiumConst.BLOCK_ATLAS)

    override fun getColor(): Int = getStack()?.getTintColor() ?: -1

    override fun getLevel(): Float = levelGetter(view)

    override fun collectTooltips(consumer: (Component) -> Unit, flag: TooltipFlag) {
        HTTextUtil.addFluidTooltip(getStack(), consumer, flag, true)
    }

    override fun renderBackground(guiGraphics: GuiGraphics) {
        background.render(guiGraphics, getBounds())
    }

    //    HTFluidWidget    //

    override fun getStack(): ImmutableFluidStack? = view.getStack()

    override fun getCapacity(stack: ImmutableFluidStack?): Int = view.getCapacity(stack)
}
