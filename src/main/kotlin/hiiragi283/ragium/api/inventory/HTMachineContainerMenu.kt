package hiiragi283.ragium.api.inventory

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.extension.createMenuAccess
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.items.IItemHandler
import java.util.function.Supplier

abstract class HTMachineContainerMenu(
    menuType: Supplier<out MenuType<*>>,
    containerId: Int,
    playerInv: Inventory,
    itemHandler: IItemHandler,
    machineEntity: HTMachineBlockEntity?,
) : HTContainerMenu(
        menuType,
        containerId,
        playerInv,
        itemHandler,
        machineEntity.createMenuAccess(),
    )
