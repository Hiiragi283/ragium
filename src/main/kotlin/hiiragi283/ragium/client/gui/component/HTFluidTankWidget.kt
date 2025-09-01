package hiiragi283.ragium.client.gui.component

import hiiragi283.ragium.api.extension.addFluidTooltip
import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.item.TooltipFlag
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.FluidStack

@OnlyIn(Dist.CLIENT)
class HTFluidTankWidget(private val tank: HTFluidTank, x: Int, y: Int) :
    HTSpriteWidget(
        x,
        y,
        16,
        18 * 3 - 2,
        Component.empty(),
    ),
    HTFluidWidget {
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

    override fun renderBackground(guiGraphics: GuiGraphics) {
        guiGraphics.blit(
            HTFluidWidget.TEXTURE_ID,
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

    //    HTFluidWidget    //

    override var stack: FluidStack
        get() = tank.getStack()
        set(value) {
            tank.setStack(value)
        }
    override val capacity: Int
        get() = tank.capacity
}
