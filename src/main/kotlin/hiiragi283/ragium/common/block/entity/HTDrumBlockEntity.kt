package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTFluidInteractable
import hiiragi283.ragium.api.block.entity.HTHandlerBlockEntity
import hiiragi283.ragium.api.inventory.HTMenuDefinition
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.inventory.HTFluidOnlyMenu
import hiiragi283.ragium.common.storage.fluid.HTFluidStackTank
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.util.variant.HTDrumVariant
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentMap
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.SimpleFluidContent
import net.neoforged.neoforge.fluids.capability.IFluidHandler

abstract class HTDrumBlockEntity(
    capacity: Int,
    variant: HTDrumVariant,
    pos: BlockPos,
    state: BlockState,
) : HTBlockEntity(variant.blockEntityHolder, pos, state),
    HTFluidInteractable,
    HTHandlerBlockEntity,
    MenuProvider {
    private val tank = HTFluidStackTank(capacity, this)

    //    Save & Load    //

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        writer.write(RagiumConst.TANK, tank)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        reader.read(RagiumConst.TANK, tank)
    }

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        super.applyImplicitComponents(componentInput)
        tank.fluid = componentInput.getOrDefault(RagiumDataComponents.FLUID_CONTENT, SimpleFluidContent.EMPTY).copy()
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        components.set(RagiumDataComponents.FLUID_CONTENT, SimpleFluidContent.copyOf(tank.fluid))
    }

    override fun reloadUpgrades() {
        super.reloadUpgrades()
        tank
    }

    override fun getFluidHandler(direction: Direction?): IFluidHandler = tank

    //    HTFluidInteractable    //

    override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult = interactWith(player, hand, tank)

    //    MenuProvider    //

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        HTFluidOnlyMenu(RagiumMenuTypes.DRUM, containerId, playerInventory, blockPos, HTMenuDefinition.empty(0))

    override fun getDisplayName(): Component = blockState.block.name

    //    Impl    //

    class Small(pos: BlockPos, state: BlockState) :
        HTDrumBlockEntity(RagiumAPI.getConfig().getSmallDrumCapacity(), HTDrumVariant.SMALL, pos, state)

    class Medium(pos: BlockPos, state: BlockState) :
        HTDrumBlockEntity(RagiumAPI.getConfig().getMediumDrumCapacity(), HTDrumVariant.MEDIUM, pos, state)

    class Large(pos: BlockPos, state: BlockState) :
        HTDrumBlockEntity(RagiumAPI.getConfig().getLargeDrumCapacity(), HTDrumVariant.LARGE, pos, state)

    class Huge(pos: BlockPos, state: BlockState) :
        HTDrumBlockEntity(RagiumAPI.getConfig().getHugeDrumCapacity(), HTDrumVariant.HUGE, pos, state)
}
