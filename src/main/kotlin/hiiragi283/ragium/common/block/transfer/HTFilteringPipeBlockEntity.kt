package hiiragi283.ragium.common.block.transfer

import hiiragi283.ragium.api.data.HTNbtCodecs
import hiiragi283.ragium.api.extension.fluidFilterText
import hiiragi283.ragium.api.extension.getStackInActiveHand
import hiiragi283.ragium.api.extension.ifPresent
import hiiragi283.ragium.api.extension.itemFilterText
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.tags.RagiumItemTags
import hiiragi283.ragium.api.util.HTPipeType
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumNetworks
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTFilteringPipeBlockEntity(pos: BlockPos, state: BlockState) :
    HTTransporterBlockEntityBase(RagiumBlockEntityTypes.FILTERING_PIPE, pos, state) {
    init {
        this.tier = HTMachineTier.ADVANCED
    }

    constructor(pos: BlockPos, state: BlockState, type: HTPipeType) : this(pos, state) {
        this.type = type
    }

    private val fluidFilterMap: MutableMap<Direction, RegistryEntryList<Fluid>> = mutableMapOf()
    private val itemFilterMap: MutableMap<Direction, RegistryEntryList<Item>> = mutableMapOf()

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        HTNbtCodecs.SIDED_FLUID_FILTER.writeTo(nbt, fluidFilterMap)
        HTNbtCodecs.SIDED_ITEM_FILTER.writeTo(nbt, itemFilterMap)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        HTNbtCodecs.SIDED_FLUID_FILTER.readAndSet(nbt, fluidFilterMap::putAll)
        HTNbtCodecs.SIDED_ITEM_FILTER.readAndSet(nbt, itemFilterMap::putAll)
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        val side: Direction = hit.side
        val stack: ItemStack = player.getStackInActiveHand()
        val result: Boolean = when {
            stack.isIn(RagiumItemTags.FLUID_EXPORTER_FILTERS) -> {
                stack.ifPresent(RagiumComponentTypes.FLUID_FILTER) { fluidFilterMap[side] = it }
                true
            }

            stack.isIn(RagiumItemTags.ITEM_EXPORTER_FILTERS) -> {
                stack.ifPresent(RagiumComponentTypes.ITEM_FILTER) { itemFilterMap[side] = it }
                true
            }

            else -> {
                if (!world.isClient) {
                    player.sendMessage(Text.literal("Side: $side"))
                    fluidFilterMap[side]?.let { player.sendMessage(fluidFilterText(it), false) }
                    itemFilterMap[side]?.let { player.sendMessage(itemFilterText(it), false) }
                }
                false
            }
        }
        if (result) {
            if (!world.isClient) {
                markDirty()
                RagiumNetworks.sendFloatingItem(
                    player,
                    stack,
                    ParticleTypes.WAX_ON,
                    SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                )
            }
        }
        return ActionResult.success(world.isClient)
    }
}
