package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTFluidInteractable
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.storage.fluid.HTFluidStackTank
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.util.variant.HTDrumVariant
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentMap
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.fluids.SimpleFluidContent
import net.neoforged.neoforge.fluids.capability.IFluidHandler

abstract class HTDrumBlockEntity(variant: HTDrumVariant, pos: BlockPos, state: BlockState) :
    HTBlockEntity(variant.blockEntityHolder, pos, state),
    HTFluidInteractable {
    private val tank = HTFluidStackTank(variant.capacity, this)

    override fun onRightClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult = RagiumMenuTypes.DRUM.openMenu(player, name, this, ::writeExtraContainerData)

    //    Save & Load    //

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        writer.write(RagiumConst.TANK, tank)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        reader.read(RagiumConst.TANK, tank)
    }

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        super.applyImplicitComponents(componentInput)
        tank.fluid =
            componentInput.getOrDefault(RagiumAPI.getInstance().getFluidComponent(), SimpleFluidContent.EMPTY).copy()
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        components.set(RagiumAPI.getInstance().getFluidComponent(), SimpleFluidContent.copyOf(tank.fluid))
    }

    override fun reloadUpgrades() {
        super.reloadUpgrades()
        tank
    }

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = true

    //    HTHandlerBlockEntity    //

    override fun getFluidHandler(direction: Direction?): IFluidHandler = tank

    //    HTFluidInteractable    //

    override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult = interactWith(player, hand, tank)

    //    Impl    //

    class Small(pos: BlockPos, state: BlockState) : HTDrumBlockEntity(HTDrumVariant.SMALL, pos, state)

    class Medium(pos: BlockPos, state: BlockState) : HTDrumBlockEntity(HTDrumVariant.MEDIUM, pos, state)

    class Large(pos: BlockPos, state: BlockState) : HTDrumBlockEntity(HTDrumVariant.LARGE, pos, state)

    class Huge(pos: BlockPos, state: BlockState) : HTDrumBlockEntity(HTDrumVariant.HUGE, pos, state)
}
