# Changelog

## v0.7.0

- Added recipes and usages for Deep Steel!

### Added

- API: `HTIntrinsicEnchantment` to handle static enchantments on specific items

- Block: Resonant Debris
    - Generated in `Deep Dark` biome, or killing `Warden` around `Deepslate`

- Enchantment: Sonic Protection
    - Invulnerable to warden's sonic boom attack

- Game Play: Added information tooltips on items
- Game Play: Added `ragium` command to interact energy network

- Item: Deep Scrap
    - Ingredient for `Deep Steel` Ingot like `Netherite Scrap`
- Item: Deep Steel Armors, Tools, and Weapon
    - Crafted from `Diamond equipments` and `Deep Steel Upgrade` in `Smithing Table`
    - Pickaxe: Always has at least `Fortune` V
    - Sword: Always has at least `Noise Canceling` V
    - Chestplate: Always has at least `Sonic Protection`
- Item: Iron/Diamond/Netherite Forge Hammer
- Item: Plastic Plate for future usage

- Integration: Crushing and Alloying recipe supports common materials
- Integration: Recipe integration for `Oritech`
- Integration: Recipe output now unified if `Almost Unified` loaded

### Fixed

- Render: Incorrect render type on Machine Frames
- Render: Show stored energy in energy network on machine's gui

- Tag: Fixed incorrect tag appending for `Azure Steel armors`

### Changed

- API: `HTCookingRecipeBuilder` generates multiple recipes based on `AbstractFurnaceRecipe`
- API: `HTDefinitionRecipeBuilder` supports `ICondition`
- API: `HTItemOutput` and `HTFluidOutput` supports tag-based entry
- API: Integrated `RagiumXXTags` into `RagiumCommonTags` and `RagiumModTags`
- API: Reconstructed `HTMaterialFalimy` for enhancement

- Game Play: Restored `Raginite Ore` generation
- Game Play: Looting of `Trader Catalog` and `Elder Heart` are now affected by `Looting` enchantment
- Game Play: `Azure Steel equipments `are crafted from `Iron equipment` in `Smithing Table`
- Game Play: Increased effect length of `Health Boost` when `Fever Cherry` eaten

- Item: Renamed `Item Magnet` into `Ragi-Magnet`
- Item: Renamed `Exp Magnet` into `Advanced Ragi-Manget`
- Item: Restored functions of `Ragi-Ticket`, use to roll loots in specific structure

- Render: Changed some textures a little

- Translation: Renamed translation keys around tooltips

### Removed

- Block: Removed `Cauldrons`
- Block: Removed `Charger`
- Block: Removed `Tree Tap`

- Game Play: Removing lava traps in Nether
    -j Use [LavaSweeper](https://www.curseforge.com/minecraft/mc-mods/lavasweeper)instead of this :)

- Item: Removed `Ragi-Ticket (Fake)`. Find out `Raginite Ore` in underground

- Recipes: `HTCauldrondroppingRecipe`

- Integration: Removed EMI information entry. Use tooltip instead of this

## v0.6.0

- initial release
