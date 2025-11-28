package hiiragi283.ragium.client.gui.screen.generator

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.client.gui.screen.HTBlockEntityContainerScreen
import hiiragi283.ragium.client.gui.screen.HTBlockEntityScreenFactory
import hiiragi283.ragium.common.block.entity.generator.HTGeneratorBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
open class HTGeneratorScreen<BE : HTGeneratorBlockEntity> : HTBlockEntityContainerScreen<BE> {
    companion object {
        @JvmStatic
        fun <BE : HTGeneratorBlockEntity> createFactory(texture: String): HTBlockEntityScreenFactory<BE> =
            createFactory(createTexture(texture))

        @JvmStatic
        fun <BE : HTGeneratorBlockEntity> createFactory(texture: ResourceLocation): HTBlockEntityScreenFactory<BE> =
            HTBlockEntityScreenFactory { menu: HTBlockEntityContainerMenu<BE>, inventory: Inventory, title: Component ->
                HTGeneratorScreen(texture, menu, inventory, title)
            }
    }

    constructor(
        texture: ResourceLocation,
        menu: HTBlockEntityContainerMenu<BE>,
        inventory: Inventory,
        title: Component,
    ) : super(texture, menu, inventory, title)

    constructor(menu: HTBlockEntityContainerMenu<BE>, inventory: Inventory, title: Component) : super(
        menu,
        inventory,
        title,
    )

    override fun init() {
        super.init()
        // Energy Widget
        createEnergyWidget(blockEntity.battery, HTSlotHelper.getSlotPosX(4))
    }
}
