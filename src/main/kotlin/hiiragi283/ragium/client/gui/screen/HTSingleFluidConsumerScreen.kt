package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.gui.screen.HTFluidScreen
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.client.gui.component.HTFluidTankWidget
import hiiragi283.ragium.common.block.entity.machine.HTChancedItemOutputBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTConsumerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTMelterBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTSingleFluidConsumerScreen<BE : HTConsumerBlockEntity>(
    private val factory: HTSingleFluidConsumerScreen<BE>.(Int) -> HTFluidTankWidget,
    texture: ResourceLocation,
    menu: HTBlockEntityContainerMenu<BE>,
    inventory: Inventory,
    title: Component,
) : HTConsumerScreen<BE>(texture, menu, inventory, title),
    HTFluidScreen {
    companion object {
        @JvmStatic
        fun chancedItemOutput(
            menu: HTBlockEntityContainerMenu<HTChancedItemOutputBlockEntity<*, *>>,
            inventory: Inventory,
            title: Component,
        ): HTSingleFluidConsumerScreen<HTChancedItemOutputBlockEntity<*, *>> = HTSingleFluidConsumerScreen(
            { index: Int -> createFluidSlot(index, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2)) },
            RagiumAPI.id("textures/gui/container/crusher.png"),
            menu,
            inventory,
            title,
        )

        @JvmStatic
        fun melter(
            menu: HTBlockEntityContainerMenu<HTMelterBlockEntity>,
            inventory: Inventory,
            title: Component,
        ): HTSingleFluidConsumerScreen<HTMelterBlockEntity> = HTSingleFluidConsumerScreen(
            { index: Int -> createFluidTank(index, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(0)) },
            RagiumAPI.id("textures/gui/container/melter.png"),
            menu,
            inventory,
            title,
        )
    }

    private lateinit var fluidWidget: HTFluidWidget

    override fun init() {
        super.init()
        fluidWidget = this.factory(0)
    }

    //    HTFluidScreen    //

    override fun getFluidWidgets(): List<HTFluidWidget> = listOf(fluidWidget)
}
