package hiiragi283.ragium.client.gui.component

import hiiragi283.ragium.api.extension.addFluidTooltip
import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.item.TooltipFlag
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.FluidStack

@OnlyIn(Dist.CLIENT)
abstract class HTFluidWidgetBase(
    private val tank: HTFluidTank,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
) : HTSpriteWidget(x, y, width, height, Component.empty()),
    HTFluidWidget {
    final override fun shouldRender(): Boolean = !stack.isEmpty

    final override fun getSprite(): TextureAtlasSprite? =
        getSprite(IClientFluidTypeExtensions.of(stack.fluid).getStillTexture(stack), InventoryMenu.BLOCK_ATLAS)

    final override fun getColor(): Int = IClientFluidTypeExtensions.of(stack.fluid).getTintColor(stack)

    final override fun collectTooltips(consumer: (Component) -> Unit, flag: TooltipFlag) {
        addFluidTooltip(stack, consumer, flag, true)
    }

    //    HTFluidWidget    //

    final override var stack: FluidStack
        get() = tank.getStack()
        set(value) {
            tank.setStack(value)
        }
    final override val capacity: Int
        get() = tank.capacity
}
