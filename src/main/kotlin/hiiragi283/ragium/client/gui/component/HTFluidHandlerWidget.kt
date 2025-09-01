package hiiragi283.ragium.client.gui.component

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.addFluidTooltip
import hiiragi283.ragium.api.gui.component.HTFluidWidget
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.item.TooltipFlag
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler

@OnlyIn(Dist.CLIENT)
class HTFluidHandlerWidget(
    private val handler: IFluidHandler?,
    private val index: Int,
    x: Int,
    y: Int,
) : HTSpriteWidget(
        x,
        y,
        16,
        18 * 3 - 2,
        Component.empty(),
    ),
    HTFluidWidget {
    companion object {
        @JvmField
        val TEXTURE_ID: ResourceLocation = RagiumAPI.id("textures/gui/tank.png")
    }

    override fun getWidget(): HTAbstractWidget = this

    override var stack: FluidStack
        get() = handler?.getFluidInTank(index) ?: FluidStack.EMPTY
        set(value) {}
    override val capacity: Int
        get() = handler?.getTankCapacity(index) ?: 0

    //    HTSpriteWidget    //

    override fun renderBackground(guiGraphics: GuiGraphics) {
        guiGraphics.blit(
            TEXTURE_ID,
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

    override fun shouldRender(): Boolean = !stack.isEmpty

    override fun getSprite(): TextureAtlasSprite? =
        getSprite(IClientFluidTypeExtensions.of(stack.fluid).getStillTexture(stack), InventoryMenu.BLOCK_ATLAS)

    override fun getColor(): Int = IClientFluidTypeExtensions.of(stack.fluid).getTintColor(stack)

    override fun getLevel(): Float {
        if (capacity <= 0) return 0f
        return stack.amount / capacity.toFloat()
    }

    override fun collectTooltips(consumer: (Component) -> Unit, flag: TooltipFlag) {
        addFluidTooltip(stack, consumer, flag, true)
    }
}
