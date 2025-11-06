package hiiragi283.ragium.api.inventory.container.type

import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.network.IContainerFactory

/**
 * Ragiumで使用する[MenuType]の拡張クラス
 * @see mekanism.common.inventory.container.type.BaseMekanismContainerType
 */
abstract class HTMenuType<MENU : AbstractContainerMenu, FACTORY>(protected val factory: FACTORY, constructor: IContainerFactory<MENU>) :
    MenuType<MENU>(constructor, FeatureFlags.VANILLA_SET)
