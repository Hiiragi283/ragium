package hiiragi283.ragium.api.gui.component

import hiiragi283.ragium.api.extension.addFluidTooltip
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.item.TooltipFlag
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.FluidStack

@OnlyIn(Dist.CLIENT)
abstract class HTFluidWidget(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    message: Component,
) : HTSpriteWidget(
        x,
        y,
        width,
        height,
        message,
    ) {
    abstract var stack: FluidStack
    abstract val capacity: Int

    //    HTSpriteWidget    //

    override fun shouldRender(): Boolean = !stack.isEmpty

    override fun getSprite(): TextureAtlasSprite? =
        getSprite(IClientFluidTypeExtensions.of(stack.fluid).getStillTexture(stack), InventoryMenu.BLOCK_ATLAS)

    override fun getColor(): Int = IClientFluidTypeExtensions.of(stack.fluid).getTintColor(stack)

    override fun getLevel(): Float {
        if (capacity <= 0) return 0f
        return stack.amount / capacity.toFloat()
    }

    override fun collectTooltips(consumer: (Component) -> Unit, flag: TooltipFlag) {
        addFluidTooltip(stack, consumer, flag)
    }
}
