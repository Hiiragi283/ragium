package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.client.gui.screens.MenuScreens

fun interface HTBlockEntityScreenFactory<BE : HTBlockEntity> :
    MenuScreens.ScreenConstructor<HTBlockEntityContainerMenu<BE>, HTBlockEntityContainerScreen<BE>>
