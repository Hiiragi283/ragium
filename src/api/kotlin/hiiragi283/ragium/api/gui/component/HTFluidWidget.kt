package hiiragi283.ragium.api.gui.component

import hiiragi283.ragium.api.extension.addFluidTooltip
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.item.TooltipFlag
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.FluidStack

@OnlyIn(Dist.CLIENT)
class HTFluidWidget(
    var stack: FluidStack,
    private val capacity: Int,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
) : HTSpriteWidget(
        x,
        y,
        width,
        height,
        Component.empty(),
    ) {
    override fun shouldRender(): Boolean = !stack.isEmpty

    override fun getSprite(): TextureAtlasSprite? = Minecraft
        .getInstance()
        .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
        .apply(IClientFluidTypeExtensions.of(stack.fluid).getStillTexture(stack))

    override fun getColor(): Int = IClientFluidTypeExtensions.of(stack.fluid).getTintColor(stack)

    override fun getLevel(): Float = stack.amount / capacity.toFloat()

    override fun collectTooltips(consumer: (Component) -> Unit, flag: TooltipFlag) {
        addFluidTooltip(stack, consumer, flag)
    }
}
