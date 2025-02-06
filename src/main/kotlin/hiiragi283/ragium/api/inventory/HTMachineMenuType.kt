package hiiragi283.ragium.api.inventory

import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.MenuType

class HTMachineMenuType<T : HTMachineContainerMenu>(constructor: MenuSupplier<T>) : MenuType<T>(constructor, FeatureFlags.VANILLA_SET)
